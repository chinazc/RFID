package com.cw.assetsrfid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.os.Looper;

import com.cw.assetsrfid.common.CrashHandler;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.entity.LoginUserEntity;
import com.cw.assetsrfid.entity.SyncInfoEntity;
import com.cw.assetsrfid.login.ui.LoginActivity;
import com.neil.myandroidtools.log.Log;
import com.zerowire.framework.sync.config.ConfigSync;
import com.zerowire.framework.sync.config.ConfigSync.EncryptPasswordMethod;
import com.zerowire.framework.sync.xml.FileUtils;

public class SFAAPPInfo extends Application {

	// ����activitylist
	public List<Activity> activityManager = new ArrayList<Activity>();
	public boolean save_log = false;
	public Looper looper = null;

	/**
	 * SharedPreferences
	 */
	public SharedPreferences settings = null;
	
	// ͼƬ��SDCARD·��
	public final static String IMAGE_UNSPECIFIED_CHILDREN = "/ZeroGlobal/Ondemand/Media/";
	
	public static String ws_IpAddressHost = null;

    
    public SyncInfoEntity syncInfoEntity = null;
    
	/**
	 * ͨ��WS�ӿ�
	 */
	public String ws_NormalInterface = "/AssetsRFID_Service/services/MobileService?wsdl";
	/**
	 * ͬ���ӿ�
	 */
	public String ws_SyncServiceURL = "/AssetsRFID_SyncService/services/AxisService?wsdl";
	/**
	 * ͬ�����ݽӿ�
	 */
	public String ws_TransputServiceURL = "/AssetsRFID_SyncService/services/AxisService?wsdl";
	/**
	 * �����Ƿ񱣴���SD����
	 */
	public boolean db_saved_in_sdcard = true;
	// Assets Ŀ¼�ļ��� *����һ��
	public String database_source_name = "cw.sqlite";
	private LoginUserEntity loginUser;
	
	@Override
	public void onCreate() {
		super.onCreate();
		addCrashHandler();
//		addLog();

		HandlerThread threadGroup = new HandlerThread("threadGroup");
		threadGroup.start();
		looper = threadGroup.getLooper();
		
		ws_IpAddressHost = getString(R.string.ws_sync_service_IP);//����

		init();
		
		ArrayList<String> mList = new ArrayList<String>();
		mList.add(this.ws_IpAddressHost);
		ConfigSync.getIpDefault = settings.getString(
				ServerConfigPreference.IP_ADDRESS, this.ws_IpAddressHost);
//		if ("".equals(ConfigSync.getIpDefault)) {
//			ConfigSync.getIpDefault = this.ws_IpAddressHost;
//		}
		ConfigSync.getIPs = mList;
		ConfigSync.ws_SyncServiceURL = this.ws_SyncServiceURL;
		ConfigSync.ws_TransputServiceURL = this.ws_TransputServiceURL;
		ConfigSync.db_create_from_server = false;
		ConfigSync.database_source_name = this.database_source_name;
		ConfigSync.encryptPasswordMethod = EncryptPasswordMethod.md5;
		ConfigSync.database_current_name = "CwAssets.sqlite";
		ConfigSync.db_saving_log = this.save_log;
	}
	
	public void init(){
		settings = getSharedPreferences(ServerConfigPreference.SETTING_INFO, MODE_PRIVATE|MODE_MULTI_PROCESS);
		loginUser = null;
	}
	
	//����ȫ���쳣�������
	private void addCrashHandler(){
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
	
	/**
	 * �����־֧��
	 */
	private void addLog(){
		String logFileName = FileUtils.pathDir(getApplicationContext(), R.string.key_path_log) + File.separator + getString(R.string.key_file_name_log);
		Log log = new Log(getApplicationContext());
		log.setArgument(logFileName, true, true);
	}
	
	/**
	 * @return ������¼�����û���Ϣ
	 */
	public LoginUserEntity getLoginUser() {
		if (loginUser == null) {
			String empID = settings.getString(ServerConfigPreference.EmpID, null);
			String empCode = settings.getString(ServerConfigPreference.EMP_CODE, null);
			String empName = settings.getString(ServerConfigPreference.EMP_NAME, null);
			String password = settings.getString(ServerConfigPreference.PASSWORD, null);
			String dept_code = settings.getString(ServerConfigPreference.DEPT_CODE, null);
			String companyCode = settings.getString(ServerConfigPreference.companyCode, null);
			
			loginUser = new LoginUserEntity();
			loginUser.setEmpID(empID);
			loginUser.setEmpCode(empCode);
			loginUser.setEmpName(empName);
			loginUser.setPassword(password);
			loginUser.setDept_code(dept_code);
			loginUser.setCompanyCode(companyCode);
		}
		return loginUser;
	}
	
	public void setLoginUser(LoginUserEntity loginUser) {
		this.loginUser = loginUser;
	}
	
	/**
	 * �˳�Ӧ��
	 */
	public void exit() {
		removeAPP();
		
		if (activityManager != null && !activityManager.isEmpty()) {
			for (Activity activity : new ArrayList<Activity>(activityManager)) {
				if (activity != null && !activity.isFinishing()) {
					activity.finish();
				}
			}
			activityManager.clear();
		}
		
		activityManager = null;
		looper = null;
		settings = null;
		
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

	//���½������������˳�
	private String key_save_app = "key_save_app";
	private void saveAPP(){
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key_save_app, this.toString());
		editor.commit();
	}
	
	private void removeAPP(){
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key_save_app);
		editor.commit();
	}
	
	/**
	 * �жϳ����Ƿ��Ƿ������˳���
	 * @return
	 */
	public boolean checkAPP(Activity activity){
		boolean result = false;
		String tempAPP = this.toString();
		
		String saveAPP = settings.getString(key_save_app, null);
		if (saveAPP != null && !tempAPP.equals(saveAPP)) {
			activity.finish();
			activityManager.clear();
			removeAPP();
			launch();
			result = true;
		} else {
			saveAPP();
		}
		return result;
	}
	
	/**
	 * �����ʷActivity
	 */
	private void launch(){
		Intent launchIntent = new Intent(getApplicationContext(), LoginActivity.class);
		launchIntent.setAction(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(launchIntent);
	}
	
}
