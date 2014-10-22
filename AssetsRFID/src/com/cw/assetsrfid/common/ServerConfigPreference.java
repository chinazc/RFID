package com.cw.assetsrfid.common;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.cw.assetsrfid.R;

public class ServerConfigPreference extends
		android.preference.PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static final String SETTING_INFO = "com.zerowire.dairy.brightDairy.UI.android_preferences";
	public static final String IP_ADDRESS = "IP_ADDRESS";
	public static final String COMPANY_ACCOUNT = "COMPANY_ACCOUNT";
	public static final String USER_ACCOUNT = "USER_ACCOUNT";
	public static final String EmpID = "EmpID";
	public static final String PROVINCE = "PROVINCE";
	public static final String MODULE = "MODULE";
	public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
	public static final String PRODUCT = "PRODUCT";
	public static final String SERVER_ADDRESS = "SERVER_ADDRESS";
	public static final String LOGIN_MODEL = "LOGIN_MODEL";
	public static final String FIRST_SYNC = "FIRST_SYNC";
	public static final String LAST_DATABASE = "LAST_DATABASE";
	public static final String ADDRESS_PORT = "ADDRESS_PORT";
	public static final String URL = "URL";
	public static final String companyCode = "companyCode";
	// 在线登录
	public static final String WEB_SERVER_URL = "WEB_SERVER_URL";
	// 检查程序更新
	public static final String CHECK_PROGRAM = "CHECK_PROGRAM";
	// 安装包下载地址
	public static final String DOWNLOAD_NEW_PROGRAM = "DOWNLOAD_NEW_PROGRAM";
	// 多媒体上传
	public static final String MULTIMEDIA_UPLOAD = "MULTIMEDIA_UPLOAD";
	// 部门编号
	public static final String DEPT_CODE = "DEPT_CODE";
	// 人员Code
	public static final String EMP_CODE = "EMP_CODE";
	// 登陆前修改时间前
	public static final String UPDATE_DT_BEFORE = "UPDATE_DT_BEFORE";
	// 登陆前修改时间后
	public static final String UPDATE_DT_AFTER = "UPDATE_DT_AFTER";
	//登陆前修改时间标记
	public static final String IS_UPDATE_DT = "IS_UPDATE_DT";
	//版本ID
	public static final String VERSION_ID = "VERSION_ID";
	//密码
	public static final String PASSWORD = "PASSWORD";
	//登录成功标记
	public static final String IS_PWD_CORRECT = "IS_PWD_CORRECT";
	//empNmae
	public static final String EMP_NAME = "EMP_NAME";
	//同步ID
	public static final String SYNC_ID = "SYNC_ID";
	//登录时间
	public static final String CurrentTime = "CurrentTime";
	//用户ID
	public static final String USER_ID = "USER_ID";
	//用户名称
	public static final String USER_NAME = "USER_NAME";
	//服务器时间
	public static final String SERVER_TIME = "SERVER_TIME";
	//开机时间
	public static final String RUN_TIME = "RUN_TIME";
	
	//服务器时间
		public static  String SERVER_TIME1 = "";
		//开机时间和当前时间差
		public static  String RUN_TIME1 = "";
		public static  String RUN_TIME2 = "";
	//是否经理
	public static final String IS_MANAGER = "IS_MANAGER";
	//权限
	public static final String ROLE = "ROLE";
	
    // 返回时数据标签
    public static String EXTRA_DEVICE_ADDRESS = "设备地址";
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		InitConfig();
		InitForm();
		// this.setContentView(R.xml.serverconfig);
	}

	private void InitForm() {
//		this.addPreferencesFromResource(R.xml.serverconfig);
		// SynchConfig config = SynchConfig.Instance(this);
		PreferenceScreen screen = this.getPreferenceScreen();
		SharedPreferences settings = getSharedPreferences(SETTING_INFO, 0);
		for (int i = 0; i < screen.getPreferenceCount(); i++) {

			Preference preference = screen.getPreference(i);
			preference.setPersistent(true);
			String key = preference.getKey();
			if (key.equals("SERVER_ADDRESS")) {
				preference.setTitle(R.string.SERVER_ADDRESS);
				preference.setSummary(settings.getString(SERVER_ADDRESS, ""));

			} else if (key.equals("SYNC_ADDRESS")) {
				preference.setTitle(R.string.SYNC_ADDRESS);
				preference.setSummary(settings.getString(IP_ADDRESS, ""));

			} else if (key.equals("COMPANY_ACCOUNT")) {
				preference.setTitle(R.string.COMPANY_ACCOUNT);
				preference.setSummary(settings.getString(COMPANY_ACCOUNT, ""));
			}

			else if (key.equals("USER_ACCOUNT")) {
				preference.setTitle(R.string.USER_ACCOUNT);
				preference.setSummary(settings.getString(USER_ACCOUNT, ""));
			} else if (key.equals("PROVINCE")) {
				preference.setTitle(R.string.PROVINCE);
				preference.setSummary(settings.getString(PROVINCE, ""));
				preference.setOrder(-1);
			} else if (key.equals("MODULE")) {
				preference.setTitle(R.string.MODULE);
				preference.setSummary(settings.getString(MODULE, ""));
			} else if (key.equals("PRODUCT_TYPE")) {
				preference.setTitle(R.string.PRODUCT_TYPE);
				preference.setSummary(settings.getString(PRODUCT_TYPE, ""));
			} else if (key.equals("PRODUCT")) {
				preference.setTitle(R.string.PRODUCT);
				preference.setSummary(settings.getString(PRODUCT, ""));
			} else if (key.equals("LOGIN_MODEL")) {
				preference.setTitle(R.string.LOGIN_MODEL);
				preference.setSummary(settings.getString(LOGIN_MODEL, ""));

			} else if (key.equals("companyCode")) {
				preference.setTitle("");
				preference.setSummary(settings.getString(companyCode, ""));

			}

		}
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (!key.equalsIgnoreCase(FIRST_SYNC))
			findPreference(key)
					.setSummary(sharedPreferences.getString(key, ""));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.PreferenceActivity#onSaveInstanceState(android.os.
	 * Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	/**
	 * ��ʼ�������ļ�
	 */
//	private void InitConfig() {
//		SharedPreferences settings = getSharedPreferences(SETTING_INFO, 0);
//		String serveraddress = settings.getString(SERVER_ADDRESS, "");
//		if (serveraddress.equals("")) {
//			settings.edit().putString(SERVER_ADDRESS, "121.33.249.28:2439");
//		}
//
//		String syncaddress = settings.getString(IP_ADDRESS, "");
//		if (syncaddress.equals("")) {
//			settings.edit().putString(IP_ADDRESS,
//					this.getString(R.string.DEFAULT_SYNC_ADDRESS));
//		}
//
//		String companyaccount = settings.getString(COMPANY_ACCOUNT, "");
//		if (companyaccount.equals("")) {
//			settings.edit().putString(COMPANY_ACCOUNT,
//					this.getString(R.string.DEFAULT_COMPANY_ACCOUNT));
//		}
//
//		String useraccount = settings.getString(USER_ACCOUNT, "");
//		if (useraccount.equals("")) {
//			settings.edit().putString(USER_ACCOUNT,
//					this.getString(R.string.DEFAULT_USER_ACCOUNT));
//		}
//
//		String province = settings.getString(PROVINCE, "");
//		if (province.equals("")) {
//			settings.edit().putString(PROVINCE,
//					this.getString(R.string.DEFAULT_PROVINCE));
//		}
//
//		String module = settings.getString(MODULE, "");
//		if (module.equals("")) {
//			settings.edit().putString(MODULE,
//					this.getString(R.string.DEFAULT_MODEL));
//		}
//
//		String producttype = settings.getString(PRODUCT_TYPE, "");
//		if (producttype.equals("")) {
//			settings.edit().putString(PRODUCT_TYPE,
//					this.getString(R.string.DEFAULT_PRODUCT_TYPE));
//		}
//
//		String product = settings.getString(PRODUCT, "");
//		if (product.equals("")) {
//			settings.edit().putString(PRODUCT,
//					this.getString(R.string.DEFAULT_PRODUCT));
//		}
//
//		String loginmodel = settings.getString(LOGIN_MODEL, "");
//		if (loginmodel.equals("")) {
//			settings.edit().putString(LOGIN_MODEL,
//					this.getString(R.string.DEFAULT_LOGIN_MODEL));
//		}
//
//		settings.edit().commit();
//	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}

}
