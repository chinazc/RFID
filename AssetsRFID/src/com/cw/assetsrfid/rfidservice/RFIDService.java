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
	// ������
	private static String				TAG						= "RFIDService";
	private static boolean				D						= true;

	private final static int			REQUEST_CONNECT_DEVICE	= 1;										// �궨���ѯ�豸���

	private final static String			MY_UUID					= "00001101-0000-1000-8000-00805F9B34FB";	// SPP����UUID��

	private InputStream					isReader;															// ������������������������

	private String						smsgEPC					= "";
	private String						smsg					= "";

	BluetoothDevice						_device					= null;									// �����豸
	BluetoothSocket						_socket					= null;									// ����ͨ��socket
	boolean								_discoveryFinished		= false;
	boolean								bRun					= true;
	boolean								bThread					= false;
	boolean								isConnected				= false;

	// ////�����ļ���Ӧ����
	SharedPreferences					gspConfigFile;														// ȫ�������ļ�

	private BluetoothAdapter			_bluetooth				= BluetoothAdapter.getDefaultAdapter();	// ��ȡ�����������������������豸

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
			return RFIDService.this;// ���ص�ǰ�����ʵ��
		}
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.v(TAG, TAG + " onCreate() called");

		// //////////////////��ȡģʽ������Ϣ//////////////////////////
		InitConfig();

		// ////////////////////��ʼ������/////////////////////////////
		// ����򿪱��������豸���ɹ�����ʾ��Ϣ����������
		if (_bluetooth == null)
		{
			if (D)
				Log.d(TAG, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�");
			return;
		}

		// �����豸���Ա�����
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

		// ����־�ļ�
		try
		{

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				File sdCardDir = Environment.getExternalStorageDirectory();// ��ȡSDCardĿ¼
				File saveFile = new File(sdCardDir, "cwdemo.txt");

				fpLog = new FileOutputStream(saveFile);

			}

		} catch (Exception e)
		{
			if (D)
				Log.d(TAG, "����־�ļ�ʧ�ܣ�" + e.toString());
		}

		isConnected = false;

	}


	private void InitConfig()
	{
		gspConfigFile = getSharedPreferences("CWORLD", MODE_PRIVATE);// ��һ�������Ǵ洢ʱ�����ƣ��ڶ������������ļ��Ĵ򿪷�ʽ

		gConfigMap = new HashMap<String, String>();
		gConfigMap.put("SYS_MUSIC_OPEN", gspConfigFile.getString("SYS_MUSIC_OPEN", "0"));// ϵͳ-��Ч����
		gConfigMap.put("CPU_WORKMODE", gspConfigFile.getString("CPU_WORKMODE", "R")); // CPU-RFID��������ģʽ
		gConfigMap.put("CPU_READNUM", gspConfigFile.getString("CPU_READNUM", "4")); // CPU-RFID����������������
		gConfigMap.put("CPU_BEEP", gspConfigFile.getString("CPU_BEEP", "1")); // CPU-RFID����������
		gConfigMap.put("CPU_SLEEP", gspConfigFile.getString("CPU_SLEEP", "15")); // CPU-RFID����SLEEPʱ��
		gConfigMap.put("RFID_REGION", gspConfigFile.getString("RFID_REGION", "0")); // RFID-REGION��������
		gConfigMap.put("RFID_RFCHANNEL", gspConfigFile.getString("RFID_RFCHANNEL", "0"));// RFID-RFCHANNEL�����ŵ�
		gConfigMap.put("RFID_FHSS", gspConfigFile.getString("RFID_FHSS", "0")); // RFID-FHSS�Զ���Ƶ
		gConfigMap.put("RFID_PAPOWER", gspConfigFile.getString("RFID_PAPOWER", "1900")); // RFID-PAPOWER���书��
		gConfigMap.put("RFID_CW", gspConfigFile.getString("RFID_CW", "0")); // RFID-CW�����ز�
		gConfigMap.put("RFID_SLEEP", gspConfigFile.getString("RFID_SLEEP", "0")); // RFID-SLEEP
	}


	@Override
	public IBinder onBind(Intent arg0)
	{
		Log.v(TAG, TAG + " onBind() called");
		return binder;
	}


	/*
	 * ժҪ: �رճ�����ô�����
	 * 
	 * @param ��
	 * 
	 * 
	 * @return ��
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG, TAG + " onDestory() called");

		if (_socket != null) // �ر�����socket
			try
			{
				_socket.close();
				_bluetooth.disable();

			} catch (IOException e)
			{
				if (D)
					Log.d(TAG, "�ر�socketʧ�ܣ�" + e.toString());
			}

		try
		{
			fpLog.close();
		} catch (Exception e)
		{
			if (D)
				Log.d(TAG, "�ر���־ʧ�ܣ�" + e.toString());
		}

		// _bluetooth.disable(); //�ر���������
	}


	/*
	 * ժҪ: �������ͨѶ״̬
	 * 
	 * @param ��
	 * 
	 * 
	 * @return true-�����ӣ� false-�Ͽ�
	 */
	public boolean checkCommStat()
	{
		return isConnected;
	}


	/*
	 * ժҪ: ���ջ�������ӦstartActivityForResult()
	 * 
	 * @param savedInstanceState:
	 * 
	 * 
	 * @return ��
	 */
	public int connectBlueTooth(String address)
	{

		if (isConnected == true)
		{
			return 0;
		}
		// �õ������豸���
		if (D)
			Log.d(TAG, "address:[" + address + "]");
		_device = _bluetooth.getRemoteDevice(address);

		// �÷���ŵõ�socket
		try
		{
			_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
		} catch (IOException e)
		{
			if (D)
				Log.d(TAG, "����socketʧ�ܣ�" + e.toString());
			isConnected = false;
			return -1;
		}

		try
		{
			_socket.connect();
			if (D)
				Log.d(TAG, "����" + _device.getName() + "�ɹ���");
		} catch (IOException e)
		{
			try
			{
				if (D)
					Log.d(TAG, "����socketʧ�ܣ�" + e.toString());
				_socket.close();
				_socket = null;
			} catch (Exception ee)
			{
				if (D)
					Log.d(TAG, "�ر�socketʧ�ܣ�" + ee.toString());
				isConnected = false;

				return -3;
			}
			isConnected = false;

			return -2;
		}

		// �򿪽����߳�
		try
		{
			isReader = _socket.getInputStream(); // �õ���������������
		} catch (Exception e)
		{
			if (D)
				Log.d(TAG, "�õ�����������������" + e.toString());
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
	 * ժҪ: ���Ӱ�ť�������
	 * 
	 * @param View:
	 * 
	 * 
	 * @return ��
	 */
	public int disconnectBlueTooth()
	{
		if (_bluetooth.isEnabled() == false)
		{ // ����������񲻿�������ʾ
			if (D)
				Log.d(TAG, " ������ʧ��");
			return -1;
		}

		try
		{

			// is.close();
			_socket.close();
			_socket = null;
			bRun = false;
			// btn.setText("����");
			if (D)
				Log.d(TAG, " �ر�����!!!");
		} catch (Exception e)
		{

			if (D)
				Log.d(TAG, " �ر���������" + e.toString());
			return -2;
		}
		isConnected = false;
		return 0;
	}


	/*
	 * ժҪ: ����ָ�������
	 * 
	 * @param String strFrame
	 * 
	 * 
	 * @return ��
	 */
	private void SendToReader(String strFrame)
	{

		if (_socket == null)
		{

			if (D)
				Log.d(TAG, "��δ��������������ӣ��������Ӷ�����!");
			return;
		}

		// ////////////////////////////////////////////////
		// //////////////����ָ�������//////////////////
		// /////////////////////////////////////////////////

		try
		{
			OutputStream osReader = _socket.getOutputStream(); // �������������

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
				Log.d(TAG, "����ָ��ʧ�ܣ�" + e.toString());
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
	// ���������߳�
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
												// �����߳�
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
															num = isReader.read(buffer); // ��������

															if (D)
																Log.d(TAG, "rcv raw data:" + String.valueOf(num) + "#" + HexUtil.bytesToHexStringWithSpace(buffer, num));
															for (int xxx = 0; xxx < num; xxx++)
															{
																// ���յ���Ӧ���ľ��ѷ���������ǿ�ʼ055���ʼ���������ĩβ�����

																if (buffer[xxx] == (byte) 0xAA) // //֡β
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

																		// ���÷�������
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

																				// ���÷�������
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

																			// ���÷�������

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

																} else if (buffer[xxx] == 0x55) // ֡ͷ
																{
																	for (int j = 0; j < buffer_return_data.length; j++)
																	{
																		buffer_return_data[j] = 0;
																	}
																	nFullReturnDataLen = 0;
																	nFullReturnDataLen++;
																	buffer_return_data[nFullReturnDataLen - 1] = buffer[xxx];
																} else
																// ����
																{
																	nFullReturnDataLen++;
																	buffer_return_data[nFullReturnDataLen - 1] = buffer[xxx];
																}
																// /////////////////////////////////////////
															}

															if (isReader.available() == 0)
																break; // ��ʱ��û�����ݲ�����������ʾ
														}// end while loop read
															// from reader

													} catch (IOException e)
													{
														if (D)
															Log.d(TAG, "��ȡ���ݴ���" + e.toString());
													}

												}// main loop
											}// end tread run()
										};						// end Receive
																// data from
																// reader thread


	/*
	 * ժҪ: ���ö���������ģʽΪRFID
	 * 
	 * @param ��
	 * 
	 * 
	 * @return ��
	 */
	public void SetWorkModeR()
	{
		String strFrame = CWReaderCmd.CPU_SetWordModeFrame("R");
		SendToReader(strFrame);
	}


	/*
	 * ժҪ: ���ö���������ģʽΪ����
	 * 
	 * @param ��
	 * 
	 * 
	 * @return ��
	 */
	public void SetWorkModeQ()
	{
		String strFrame = CWReaderCmd.CPU_SetWordModeFrame("Q");
		SendToReader(strFrame);
	}


	/*
	 * ժҪ: ����Ѱ��ָ��
	 * 
	 * @param savedInstanceState:
	 * 
	 * 
	 * @return ��
	 */
	public void invetory()
	{

		String strFrame = CWReaderCmd.RFID_ReadSingleFrame();
		SendToReader(strFrame);
	}


	/*
	 * ժҪ: ���Ͷ���ǩ����ָ��
	 * 
	 * @param String accessPwd, int memBank, int sa, int dl:
	 * 
	 * 
	 * @return ��
	 */
	public void ReadData(String accessPwd, int memBank, int sa, int dl)
	{

		String strFrame = CWReaderCmd.RFID_ReadDataFrame(accessPwd, memBank, sa, dl);// "00000000",3,0,32
																						// );
		SendToReader(strFrame);
	}


	/*
	 * ժҪ: ����д��ǩ����ָ��
	 * 
	 * @param String accessPwd, int memBank, int sa, int dl, String dt:
	 * 
	 * 
	 * @return ��
	 */
	public void WriteData(String accessPwd, int memBank, int sa, int dl, String dt)
	{

		String strFrame = CWReaderCmd.RFID_WriteDataFrame(accessPwd, memBank, sa, dl, dt);
		SendToReader(strFrame);
	}

	/*
	 * ժҪ: �������÷��书��ָ��
	 * 
	 * @param 
	 * 
	 * 
	 * @return ��
	 */
	public void SetPaPower(short powerdBm)
	{
	
		String strFrame = CWReaderCmd.RFID_SetPaPowerFrame(powerdBm);
		SendToReader(strFrame);
	}
	
	
	/*
	 * ժҪ: ���
	 * 
	 * @param View:
	 * 
	 * 
	 * @return ��
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
