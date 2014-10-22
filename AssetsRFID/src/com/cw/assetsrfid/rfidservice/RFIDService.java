package com.cw.assetsrfid.rfidservice;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.cw.assetsrfid.CWDriver.CWReaderCmd;
import com.cw.assetsrfid.CWDriver.CWReaderParser;
import com.cw.assetsrfid.CWDriver.RFIDConstCode;
import com.cw.assetsrfid.common.HexUtil;


public class RFIDService extends Service
{
	public static final int DEVICE_WORK_MODE_RFID=0;
	public static final int DEVICE_WORK_MODE_BARCODE=1;
	// 调试用
	private static String				TAG						= "RFIDService";
	private static boolean				D						= true;

	private final static int			REQUEST_CONNECT_DEVICE	= 1;										// 宏定义查询设备句柄

	private final static String			MY_UUID					= "00001101-0000-1000-8000-00805F9B34FB";	// SPP服务UUID号

	private InputStream					isReader;															// 输入流，用来接收蓝牙数据

	private String						smsgEPC					= "";
	private String						smsg					= "";

	BluetoothDevice						_device					= null;									// 蓝牙设备
	BluetoothSocket						_socket					= null;									// 蓝牙通信socket
	boolean								_discoveryFinished		= false;
	boolean								bRun					= true;
	boolean								bThread					= false;
	boolean								isConnected				= false;

	// ////配置文件对应变量
	SharedPreferences					gspConfigFile;														// 全局配置文件

	private BluetoothAdapter			_bluetooth				= BluetoothAdapter.getDefaultAdapter();	// 获取本地蓝牙适配器，即蓝牙设备

	FileOutputStream					fpLog;

	HashMap<String, String>				gConfigMap;

	private static ArrayList<String[]>	gRFIDDataList			= new ArrayList<String[]>();

	// ///////////////////////
	String								gBarCode				= "";

	private final IBinder				binder					= new LocalBinder();


	public class LocalBinder extends Binder
	{
		public RFIDService getService()
		{
			return RFIDService.this;// 返回当前服务的实例
		}
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.v(TAG, TAG + " onCreate() called");

		// //////////////////读取模式配置信息//////////////////////////
		InitConfig();

		// ////////////////////初始化蓝牙/////////////////////////////
		// 如果打开本地蓝牙设备不成功，提示信息，结束程序
		if (_bluetooth == null)
		{
			if (D)
				Log.d(TAG, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！");
			return;
		}

		// 设置设备可以被搜索
		new Thread()
		{
			public void run()
			{
				if (_bluetooth.isEnabled() == false)
				{
					_bluetooth.enable();
				}
			}
		}.start();

		// 打开日志文件
		try
		{

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
				File saveFile = new File(sdCardDir, "cwdemo.txt");

				fpLog = new FileOutputStream(saveFile);

			}

		} catch (Exception e)
		{
			if (D)
				Log.d(TAG, "打开日志文件失败！" + e.toString());
		}

		isConnected = false;

	}


	private void InitConfig()
	{
		gspConfigFile = getSharedPreferences("CWORLD", MODE_PRIVATE);// 第一个参数是存储时的名称，第二个参数则是文件的打开方式

		gConfigMap = new HashMap<String, String>();
		gConfigMap.put("SYS_MUSIC_OPEN", gspConfigFile.getString("SYS_MUSIC_OPEN", "0"));// 系统-音效开关
		gConfigMap.put("CPU_WORKMODE", gspConfigFile.getString("CPU_WORKMODE", "R")); // CPU-RFID参数工作模式
		gConfigMap.put("CPU_READNUM", gspConfigFile.getString("CPU_READNUM", "4")); // CPU-RFID参数按键读卡次数
		gConfigMap.put("CPU_BEEP", gspConfigFile.getString("CPU_BEEP", "1")); // CPU-RFID参数蜂鸣器
		gConfigMap.put("CPU_SLEEP", gspConfigFile.getString("CPU_SLEEP", "15")); // CPU-RFID参数SLEEP时间
		gConfigMap.put("RFID_REGION", gspConfigFile.getString("RFID_REGION", "0")); // RFID-REGION工作地区
		gConfigMap.put("RFID_RFCHANNEL", gspConfigFile.getString("RFID_RFCHANNEL", "0"));// RFID-RFCHANNEL工作信道
		gConfigMap.put("RFID_FHSS", gspConfigFile.getString("RFID_FHSS", "0")); // RFID-FHSS自动跳频
		gConfigMap.put("RFID_PAPOWER", gspConfigFile.getString("RFID_PAPOWER", "1900")); // RFID-PAPOWER发射功率
		gConfigMap.put("RFID_CW", gspConfigFile.getString("RFID_CW", "0")); // RFID-CW连续载波
		gConfigMap.put("RFID_SLEEP", gspConfigFile.getString("RFID_SLEEP", "0")); // RFID-SLEEP
	}


	@Override
	public IBinder onBind(Intent arg0)
	{
		Log.v(TAG, TAG + " onBind() called");
		return binder;
	}


	/*
	 * 摘要: 关闭程序掉用处理部分
	 * 
	 * @param 无
	 * 
	 * 
	 * @return 无
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG, TAG + " onDestory() called");

		if (_socket != null) // 关闭连接socket
			try
			{
				_socket.close();
				_bluetooth.disable();

			} catch (IOException e)
			{
				if (D)
					Log.d(TAG, "关闭socket失败！" + e.toString());
			}

		try
		{
			fpLog.close();
		} catch (Exception e)
		{
			if (D)
				Log.d(TAG, "关闭日志失败！" + e.toString());
		}

		// _bluetooth.disable(); //关闭蓝牙服务
	}


	/*
	 * 摘要: 检查蓝牙通讯状态
	 * 
	 * @param 无
	 * 
	 * 
	 * @return true-已连接， false-断开
	 */
	public boolean checkCommStat()
	{
		return isConnected;
	}


	/*
	 * 摘要: 接收活动结果，响应startActivityForResult()
	 * 
	 * @param savedInstanceState:
	 * 
	 * 
	 * @return 无
	 */
	public int connectBlueTooth(String address)
	{

		if (isConnected == true)
		{
			return 0;
		}
		// 得到蓝牙设备句柄
		if (D)
			Log.d(TAG, "address:[" + address + "]");
		_device = _bluetooth.getRemoteDevice(address);

		// 用服务号得到socket
		try
		{
			_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
		} catch (IOException e)
		{
			if (D)
				Log.d(TAG, "建立socket失败：" + e.toString());
			isConnected = false;
			return -1;
		}

		try
		{
			_socket.connect();
			if (D)
				Log.d(TAG, "连接" + _device.getName() + "成功！");
		} catch (IOException e)
		{
			try
			{
				if (D)
					Log.d(TAG, "连接socket失败：" + e.toString());
				_socket.close();
				_socket = null;
			} catch (Exception ee)
			{
				if (D)
					Log.d(TAG, "关闭socket失败：" + ee.toString());
				isConnected = false;

				return -3;
			}
			isConnected = false;

			return -2;
		}

		// 打开接收线程
		try
		{
			isReader = _socket.getInputStream(); // 得到蓝牙数据输入流
		} catch (Exception e)
		{
			if (D)
				Log.d(TAG, "得到蓝牙数据输入流：" + e.toString());
			isConnected = false;
			return -4;
		}
		if (bThread == false)
		{
			ReadThread.start();

			bThread = true;
		} else
		{
			bRun = true;
		}

		isConnected = true;
		return 0;

	}


	/*
	 * 摘要: 连接按钮处理程序
	 * 
	 * @param View:
	 * 
	 * 
	 * @return 无
	 */
	public int disconnectBlueTooth()
	{
		if (_bluetooth.isEnabled() == false)
		{ // 如果蓝牙服务不可用则提示
			if (D)
				Log.d(TAG, " 打开蓝牙失败");
			return -1;
		}

		try
		{

			// is.close();
			_socket.close();
			_socket = null;
			bRun = false;
			// btn.setText("连接");
			if (D)
				Log.d(TAG, " 关闭蓝牙!!!");
		} catch (Exception e)
		{

			if (D)
				Log.d(TAG, " 关闭蓝牙错误：" + e.toString());
			return -2;
		}
		isConnected = false;
		return 0;
	}


	/*
	 * 摘要: 发送指令到读卡器
	 * 
	 * @param String strFrame
	 * 
	 * 
	 * @return 无
	 */
	private void SendToReader(String strFrame)
	{

		if (_socket == null)
		{

			if (D)
				Log.d(TAG, "尚未与读卡器建立连接，请先连接读卡器!");
			return;
		}

		// ////////////////////////////////////////////////
		// //////////////发送指令到读卡器//////////////////
		// /////////////////////////////////////////////////

		try
		{
			OutputStream osReader = _socket.getOutputStream(); // 蓝牙连接输出流

			byte[] bos = HexUtil.hexStringToBytes(strFrame);
			if (D)
				Log.d(TAG, "send cmd:" + HexUtil.bytesToHexStringWithSpace(bos, bos.length));

			osReader.write(bos);
			osReader.flush();

			String sTemp = "";
			sTemp = "->" + Integer.toString(bos.length) + ":" + HexUtil.bytesToHexStringWithSpace(bos, bos.length) + "\n";
			smsg = sTemp + smsg;
			fpLog.write(sTemp.getBytes());
			fpLog.flush();

			// Thread.sleep(200);

		} catch (Exception e)
		{

			if (D)
				Log.d(TAG, "发送指令失败：" + e.toString());
			return;
		}

	}// end SendToReader


	private static void addRFIDDataList(String PC, String EPC, String CRC, String RSSI, String PER, String TIME)
	{
		// 0:PC, 1:EPC, 2:CRC 3:RSSI 4:PER 5:TIME 6:CNT

		for (String x[] : gRFIDDataList)
		{
			if (x[1].equals(EPC))
			{
				x[0] = PC;
				x[2] = CRC;
				x[3] = RSSI;
				x[4] = PER;
				x[5] = TIME;
				x[6] = String.format("%d", Integer.valueOf(x[6]) + 1);

				return;
			}
		}
		gRFIDDataList.add(new String[] { PC, EPC, CRC, RSSI, PER, TIME, "1" });

	}


	// //////////////////////////////////////////////////////////
	// 接收数据线程
	// //////////////////////////////////////////////////////////
	private CWReaderParser	gParser		= new CWReaderParser();

	private Thread			ReadThread	= new Thread()
										{

											public void run()
											{
												int num = 0;
												byte[] buffer = new byte[1024];// modified
																				// by
																				// koogle
												byte[] buffer_return_data = new byte[2056];
												int nFullReturnDataLen = 0;

												bRun = true;
												// 接收线程
												while (true)
												{
													try
													{
														while (isReader.available() == 0)
														{
															while (bRun == false)
															{

															}
														}

														while (true)
														{
															num = isReader.read(buffer); // 读入数据

															if (D)
																Log.d(TAG, "rcv raw data:" + String.valueOf(num) + "#" + HexUtil.bytesToHexStringWithSpace(buffer, num));
															for (int xxx = 0; xxx < num; xxx++)
															{
																// 将收到的应答报文惊醒分析，如果是开始055则初始化，如果是末尾则完成

																if (buffer[xxx] == (byte) 0xAA) // //帧尾
																{
																	nFullReturnDataLen++;
																	buffer_return_data[nFullReturnDataLen - 1] = buffer[xxx];

																	String sTemp = "";
																	sTemp = "<-" + Integer.toString(nFullReturnDataLen) + ":" + HexUtil.bytesToHexStringWithSpace(buffer_return_data, nFullReturnDataLen) + "\n";
																	fpLog.write(sTemp.getBytes());
																	fpLog.flush();
																	smsg = sTemp + smsg;
																	if (D)
																		Log.d(TAG, "full return data:" + String.valueOf(nFullReturnDataLen) + "#####" + HexUtil.bytesToHexStringWithSpace(buffer_return_data, nFullReturnDataLen));

																	gParser.PreparePackage(buffer_return_data, nFullReturnDataLen);
																	gParser.parsePacket();
																	if (gParser.getErrCode() < 0)
																	{
																		Log.d(TAG, "e<0:S");
																		sTemp = String.format("errcode=%d", gParser.getErrCode()) + "." + gParser.getErrDesc() + "\n";
																		smsg = sTemp + smsg;

																		Intent intent = new Intent();
																		intent.setAction("CWREADERRESPONSE");
																		intent.putExtra("ERRCODE", gParser.getErrCode());
																		intent.putExtra("DESC", gParser.getErrDesc());
																		intent.putExtra("COMMAND", gParser.RFIDCOMMAND);
																		sendBroadcast(intent);
																		Log.d(TAG, "e<0:E");
																	} else
																	{
																		Log.d(TAG, "e>0:0");
																		sTemp = gParser.getOKDesc() + "." + gParser.getReturnData() + "\n";
																		smsg = sTemp + smsg;

																		// 设置返回数据
																		Intent intent = new Intent();
																		intent.setAction("CWREADERRESPONSE");
																		intent.putExtra("ERRCODE", gParser.getErrCode());
																		intent.putExtra("DESC", gParser.getOKDesc());
																		intent.putExtra("COMMAND", gParser.RFIDCOMMAND);

																		if (gConfigMap.get("CPU_WORKMODE").equals("R"))
																		{
																			Log.d(TAG, "e>0:R");


																			if (gParser.RFIDCOMMAND.equals(RFIDConstCode.CMD_INVENTORY) && gParser.mapData.get("EPC").length() > 0)
																			{
																				Log.d(TAG, "e>0:R inv s");
																				String PC = gParser.mapData.get("PC");
																				String EPC = gParser.mapData.get("EPC");
																				String CRC = gParser.mapData.get("CRC");
																				String RSSI = gParser.mapData.get("RSSI");
																				String PER = gParser.mapData.get("PER");
																				String TIME = gParser.mapData.get("TIME");
																				// addRFIDDataList(PC,
																				// EPC,
																				// CRC,
																				// RSSI,
																				// PER,
																				// TIME);
																				// gRFIDDataList.add(new
																				// String[]{
																				// EPC,
																				// PC,
																				// CRC,
																				// RSSI,
																				// PER,
																				// TIME});
																				// smsg="";


																				intent.putExtra("EPC", EPC);
																				sendBroadcast(intent);
																				Log.d(TAG, "e>0:R inv e");

																			} else if (gParser.RFIDCOMMAND.equals(RFIDConstCode.CMD_READ_DATA))
																			{
																				Log.d(TAG, "e>0:R read s");
																				String READ_EPC = gParser.mapData.get("READ_EPC");
																				String READ_DATA = gParser.mapData.get("READ_DATA");
																				if (D)
																					Log.d(TAG, "READ_EPC:" + READ_EPC);
																				if (D)
																					Log.d(TAG, "READ_DATA:" + READ_DATA);

																				// 设置返回数据
																				intent.putExtra("READ_EPC", READ_EPC);
																				intent.putExtra("READ_DATA", READ_DATA);
																				sendBroadcast(intent);
																				Log.d(TAG, "e>0:R read e");

																			}
																		} else if (gConfigMap.get("CPU_WORKMODE").equals("Q") && gParser.mapData.get("BARCODE").length() > 0)
																		{
																			Log.d(TAG, "e>0:Q  s");
																			String BARCODE = gParser.mapData.get("BARCODE");
																			smsgEPC = BARCODE;

																			// 设置返回数据

																			intent.putExtra("BARCODE", BARCODE);
																			sendBroadcast(intent);
																			Log.d(TAG, "e>0:Q  e");

																		} else
																		{
																			Log.d(TAG, "e>0: other cmd s");
																			

																			sendBroadcast(intent);
																			Log.d(TAG, "e>0: other cmd e");
																		}

																	}

																	fpLog.write(sTemp.getBytes());
																	fpLog.flush();

																} else if (buffer[xxx] == 0x55) // 帧头
																{
																	for (int j = 0; j < buffer_return_data.length; j++)
																	{
																		buffer_return_data[j] = 0;
																	}
																	nFullReturnDataLen = 0;
																	nFullReturnDataLen++;
																	buffer_return_data[nFullReturnDataLen - 1] = buffer[xxx];
																} else
																// 其他
																{
																	nFullReturnDataLen++;
																	buffer_return_data[nFullReturnDataLen - 1] = buffer[xxx];
																}
																// /////////////////////////////////////////
															}

															if (isReader.available() == 0)
																break; // 短时间没有数据才跳出进行显示
														}// end while loop read
															// from reader

													} catch (IOException e)
													{
														if (D)
															Log.d(TAG, "读取数据错误：" + e.toString());
													}

												}// main loop
											}// end tread run()
										};						// end Receive
																// data from
																// reader thread


	/*
	 * 摘要: 设置读卡器工作模式为RFID
	 * 
	 * @param 无
	 * 
	 * 
	 * @return 无
	 */
	public void SetWorkModeR()
	{
		String strFrame = CWReaderCmd.CPU_SetWordModeFrame("R");
		SendToReader(strFrame);
	}


	/*
	 * 摘要: 设置读卡器工作模式为条码
	 * 
	 * @param 无
	 * 
	 * 
	 * @return 无
	 */
	public void SetWorkModeQ()
	{
		String strFrame = CWReaderCmd.CPU_SetWordModeFrame("Q");
		SendToReader(strFrame);
	}


	/*
	 * 摘要: 发送寻卡指令
	 * 
	 * @param savedInstanceState:
	 * 
	 * 
	 * @return 无
	 */
	public void invetory()
	{

		String strFrame = CWReaderCmd.RFID_ReadSingleFrame();
		SendToReader(strFrame);
	}


	/*
	 * 摘要: 发送读标签数据指令
	 * 
	 * @param String accessPwd, int memBank, int sa, int dl:
	 * 
	 * 
	 * @return 无
	 */
	public void ReadData(String accessPwd, int memBank, int sa, int dl)
	{

		String strFrame = CWReaderCmd.RFID_ReadDataFrame(accessPwd, memBank, sa, dl);// "00000000",3,0,32
																						// );
		SendToReader(strFrame);
	}


	/*
	 * 摘要: 发送写标签数据指令
	 * 
	 * @param String accessPwd, int memBank, int sa, int dl, String dt:
	 * 
	 * 
	 * @return 无
	 */
	public void WriteData(String accessPwd, int memBank, int sa, int dl, String dt)
	{

		String strFrame = CWReaderCmd.RFID_WriteDataFrame(accessPwd, memBank, sa, dl, dt);
		SendToReader(strFrame);
	}

	/*
	 * 摘要: 发送设置发射功率指令
	 * 
	 * @param 
	 * 
	 * 
	 * @return 无
	 */
	public void SetPaPower(short powerdBm)
	{
	
		String strFrame = CWReaderCmd.RFID_SetPaPowerFrame(powerdBm);
		SendToReader(strFrame);
	}
	
	
	/*
	 * 摘要: 清除
	 * 
	 * @param View:
	 * 
	 * 
	 * @return 无
	 */
	public void onClear()
	{

		smsgEPC = "";

		Iterator<String[]> iterator = gRFIDDataList.iterator();
		while (iterator.hasNext())
		{
			iterator.next();

			iterator.remove();
		}

		return;
	}

}
