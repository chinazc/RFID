package com.cw.assetsrfid.cardreder.ui;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.rfidservice.RFIDService;
import com.cw.assetsrfid.rfidservice.RFIDService.LocalBinder;


public class CardReaderSettingActivity extends AbstractBaseActivity {

	private static String TAG = "DemoSet";
	private TextView txtDemoResult;
	private RFIDService cts;
	boolean bound;
	private BroadcastReceiver batteryLevelRcvr;
	private IntentFilter batteryLevelFilter;
	private ServiceConnection sc = new ServiceConnection() {
	public void onServiceDisconnected(ComponentName name) {
			bound = false;
		}
	public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;// ����Զ����LocalBinder����
			cts = binder.getService();// ���CurrentTimeService����
			bound = true;
		}
	};
	
	SharedPreferences gspConfigFile; //ȫ�������ļ�
	HashMap<String , String> gConfigMap ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_reder_set);
		// ��������ʾ����
		txtDemoResult = (TextView) findViewById(R.id.txtDemoResult); // �õ���ʾ�����

		Intent intent = new Intent(this, RFIDService.class);
		boolean bReturn = getApplicationContext().bindService(intent, sc, BIND_AUTO_CREATE);// �󶨷���
		if(bReturn == false)
			Log.d(TAG, TAG + " bindService false");
		batteryLevelRcvr = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				// ���ӳɹ�����DeviceListActivity���÷���
				// MAC��ַ����DeviceListActivity���÷���
				String DESC = intent.getExtras().getString("DESC");
				String EPC = intent.getExtras().getString("EPC");
				String BARCODE = intent.getExtras().getString("BARCODE");

				txtDemoResult.setText("desc=" + DESC + " + EPC= " + EPC
						+ " BARCODE=" + BARCODE);
			}
		};
		batteryLevelFilter = new IntentFilter("CWREADERRESPONSE");
		registerReceiver(batteryLevelRcvr, batteryLevelFilter);
		
		InitConfig();

	}

	
    private void InitConfig()
    {
        gspConfigFile = getSharedPreferences("CWORLD", MODE_PRIVATE);//��һ�������Ǵ洢ʱ�����ƣ��ڶ������������ļ��Ĵ򿪷�ʽ
        
        gConfigMap = new HashMap<String , String>();   
        gConfigMap.put("SYS_MUSIC_OPEN" , 	gspConfigFile.getString("SYS_MUSIC_OPEN", "0"));//ϵͳ-��Ч����
        gConfigMap.put("CPU_WORKMODE" , 	gspConfigFile.getString("CPU_WORKMODE", "R"));  //CPU-RFID��������ģʽ
        gConfigMap.put("CPU_READNUM" , 		gspConfigFile.getString("CPU_READNUM", "4"));	//CPU-RFID����������������
        gConfigMap.put("CPU_BEEP" , 		gspConfigFile.getString("CPU_BEEP", "1"));		//CPU-RFID����������
        gConfigMap.put("CPU_SLEEP" , 		gspConfigFile.getString("CPU_SLEEP", "15")); 	//CPU-RFID����SLEEPʱ��
        gConfigMap.put("RFID_REGION" , 		gspConfigFile.getString("RFID_REGION", "0")); 	//RFID-REGION�������� 
        gConfigMap.put("RFID_RFCHANNEL" , 	gspConfigFile.getString("RFID_RFCHANNEL", "0"));//RFID-RFCHANNEL�����ŵ�
        gConfigMap.put("RFID_FHSS" , 		gspConfigFile.getString("RFID_FHSS", "0")); 	//RFID-FHSS�Զ���Ƶ  
        gConfigMap.put("RFID_PAPOWER" , 	gspConfigFile.getString("RFID_PAPOWER", "1900")); 	//RFID-PAPOWER���书��
        gConfigMap.put("RFID_CW" , 			gspConfigFile.getString("RFID_CW", "0")); 		//RFID-CW�����ز�
        gConfigMap.put("RFID_SLEEP" , 		gspConfigFile.getString("RFID_SLEEP", "0")); 	//RFID-SLEEP
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// cts.disconnectBlueTooth();
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	public void onDemoConnectButtonClicked(View v) {
		//if (bound) {
			
			
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //��ת��������
    		startActivityForResult(serverIntent, 9999);  //���÷��غ궨��

		//}
	}

	public void onInvetory(View v) {
		if (bound) {
			cts.invetory();

		}
	}
	public void setRFIDmode(View v) {
		if (bound) {
			cts.SetWorkModeR();

		}
	}
	public void setBarCodemode(View v) {
		if (bound) {
			cts.SetWorkModeQ();
		}
	}
	


	public void onDisconnectButtonClicked(View v) {
		if (bound) {
			int nReturn = cts.disconnectBlueTooth();
			if (nReturn < 0)
				Log.d(TAG, TAG + " disconnect failed, return=" + nReturn);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 9999 : // ���ӽ������DeviceListActivity���÷���
				// ��Ӧ���ؽ��
				if (resultCode == Activity.RESULT_OK) {
					// ���ӳɹ�����DeviceListActivity���÷���
					// MAC��ַ����DeviceListActivity���÷���

					
	                String address = data.getExtras()
                            .getString(ServerConfigPreference.EXTRA_DEVICE_ADDRESS);
					int nReturn = cts.connectBlueTooth(address);
					if (nReturn < 0)
						Log.d(TAG, TAG + " connect failed, return=" + nReturn);
				}
		}
	}
    private class ChoiceOnClickListener implements DialogInterface.OnClickListener 
    {  
  	  
        private int which = 0;  
        @Override  
        public void onClick(DialogInterface dialogInterface, int which) {  
            this.which = which;  
        }  
          
        public int getWhich() {  
            return which;  
        }  
    } 
    private int getStringIndex(String[] strArr, String strValue)
    {
    	for(int i=0;i< strArr.length;i++)
    	{
    		if (strArr[i].equals(strValue)){
    			return i;
    		}
    	}
    	return -1;
    }
    /*
    * ժҪ:
    *     ��ʾ��OK��Cancel��ť���б��ѡ��Ự��
    *
    *  @param   strTitle:  �Ự�����
    *  @param   items:  ѡ��������
    *  @param   checkedItemIndex:  ѡ���ѡ������������
    *  @param   onSelectClickListener:   ���ѡ����������
    *  @param   onOKClickListtener:   ���OK��ť�������
    *  
    *
    *  @return    ��
    *     
    *     
    */
	public void ShowSingleSelectDialog(String strTitle, 
									String[] items,
									int checkedItemIndex,
									DialogInterface.OnClickListener onSelectClickListener ,
									DialogInterface.OnClickListener onOKClickListtener)
	//android.content.DialogInterface.OnClickListener
	{
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(R.drawable.logo);
		alert.setTitle(strTitle); 
		alert.setSingleChoiceItems( items,checkedItemIndex, onSelectClickListener);
		alert.setPositiveButton("ȷ��", onOKClickListtener);
		alert.setNegativeButton("ȡ��", null);
		alert.create().show();

	
	}
    public void onSetPaPowerButtonClicked(View v)
    {

    	
    	
    	int nChoice=0;
		

    	final String[] strArrayValue = {"19 dBm", "20 dBm", "21 dBm", "22 dBm", "23 dBm" , "24 dBm", "25 dBm", "26 dBm", "27 dBm", "28 dBm", "29 dBm", "30 dBm"};
    	final String[] shtArrayPower = { "1900","2000","2100","2200","2300", "2400" , "2500", "2600","2700","2800","2900","3000" };
    	
    	nChoice = getStringIndex(strArrayValue,	gConfigMap.get("RFID_PAPOWER"));
    	
		final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener(); 
		ShowSingleSelectDialog("���÷��书��", 
							strArrayValue ,
							nChoice,
							choiceListener ,
							new DialogInterface.OnClickListener()
							 {
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									
									int choiceWhich = choiceListener.getWhich();
									
    								Editor editor=gspConfigFile.edit();
    								editor.putString("RFID_PAPOWER", strArrayValue[choiceWhich]);
    								editor.commit();
    								gConfigMap.put("RFID_PAPOWER", strArrayValue[choiceWhich]);
    								
									
									short powerdBm  =  Short.valueOf(shtArrayPower[choiceWhich]);
									if (bound) {
										cts.SetPaPower(powerdBm);

									}
									
	
									
									
								}
							 }
						 );
	}
	
/*
	@Override
	protected void onStop() {
		super.onStop();
		if (bound) {
			bound = false;
			unbindService(sc);// ���
		}
	}
*/
}
