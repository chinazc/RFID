package com.cw.assetsrfid.login.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.SFAAPPInfo;
import com.cw.assetsrfid.common.GetDate;
import com.cw.assetsrfid.common.GetGuid;
import com.cw.assetsrfid.common.MessageBox;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.entity.SyncInfoEntity;
import com.cw.assetsrfid.login.logic.LoginManager;
import com.cw.assetsrfid.main.MainActivity;
import com.neil.myandroidtools.log.Log;
import com.zerowire.framework.sync.SyncOperation;
import com.zerowire.framework.sync.config.ConfigSync;
import com.zerowire.framework.sync.entity.SyncInfo;
import com.zerowire.framework.sync.ws.WebAttrKV;
import com.zerowire.framework.sync.ws.WebServiceAttribute;
import com.zerowire.framework.sync.ws.WebServiceConn;

/**
 * 系统设置界面类
 * 
 */
public class FrmLoginSyncActivity extends AbstractBaseActivity implements OnClickListener{

	private SharedPreferences settings;
	// 表格组件
	private TextView txSynAddress;
	private EditText etUserName;
	private EditText etPassword;
	private RadioButton rgSync;
	// 同步状态显示
	private TextView tvElapseTimeValue;
	private TextView tvSyncResultValue, tvSyncProgressValue;

	public static final int OpenSettings = 0x999;
	public static final int FullSyncType = 1;
	public static final int IncrementSyncType = 2;
	public static final int FirstSync = 4;
	public static int SyncType = 0;
	public static boolean isCansel;

	private Context context;
	private LoginManager loginManager;
	private String user = "", pw = "";
	SharedPreferences.Editor editor;
	private Button btSaveSynAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_sync);
		initTitle();

		SyncType = getIntent().getIntExtra("syncType", 0);
		user = getIntent().getStringExtra("user");
		pw = getIntent().getStringExtra("pw");
		this.setResult(1000);
		context = this;
		// 初始化组件
		init();
		// 安装系统后强制初始化同步
		rgSync.setChecked(true);
		if (SyncType == FullSyncType || SyncType == FirstSync) {
			rgSync.setText("初始化");
			titleName.setText("初始化同步");
			etUserName.setEnabled(true);
			etPassword.setEnabled(true);
		} else if (SyncType == IncrementSyncType) {
			rgSync.setText("增量");
			titleName.setText("数据同步");
			etUserName.setEnabled(false);
			etPassword.setEnabled(false);
		}
		if (SyncType == FirstSync) {
			StartSync();
		}
	}

	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		btnRight.setText("同步");
		btnLeft.setText("返回");
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
	}
	/**
	 * 初始化页面组件
	 */
	public void init() {
		loginManager = new LoginManager(this);
		settings = getSharedPreferences(ServerConfigPreference.SETTING_INFO, 0);
		editor = settings.edit();
		Resources resources = this.getResources();

		// ArrayList<OrderTypeEntity> list = logic.getSyncAddr();
		// if (list.size()==0) {
		// editor.putString(ServerConfigPreference.SYNC_ADDRESS,
		// resources.getString(R.string.DEFAULT_SYNC_ADDRESS));
		// editor.commit();
		// }

		// 取得系统设置中的值
		String strSynAddress = settings.getString(
				ServerConfigPreference.IP_ADDRESS, SFAAPPInfo.ws_IpAddressHost);
		// 初始化文本框的值
		txSynAddress = (TextView) this.findViewById(R.id.etSynAddress);
		btSaveSynAddress = (Button) this.findViewById(R.id.btSaveSynAddress);
		txSynAddress.setText(strSynAddress);

		appInfo.ws_IpAddressHost = strSynAddress ;
		// 公司账号
//		etCompany = (EditText) this.findViewById(R.id.etCompany);
//
//		etCompany.setText(settings.getString(
//				ServerConfigPreference.companyCode, "001"));

		// 用户账号
		etUserName = (EditText) this.findViewById(R.id.etUserID);
		etPassword = (EditText) this.findViewById(R.id.etpassword);
		if (SyncType == FullSyncType) {
//			if (settings.getBoolean(
//					getString(R.string.sp_key_save_username_state), false)) {
//				etUserName.setText(settings.getString(
//						getString(R.string.sp_key_username), ""));
				
				etUserName.setText(settings.getString(ServerConfigPreference.EmpID, ""));
//			}
			if (settings.getBoolean(
					getString(R.string.sp_key_save_password_state), false)) {
//				etPassword.setText(settings.getString(
//						getString(R.string.sp_key_password), ""));
				etPassword.setText(settings.getString(ServerConfigPreference.PASSWORD, ""));
				
			}
		} else {
			etUserName.setText(settings.getString(ServerConfigPreference.EmpID,
					""));
			etPassword.setText(settings.getString(
					ServerConfigPreference.PASSWORD, ""));
		}

		// 同步方式
		rgSync = (RadioButton) this.findViewById(R.id.rbSync);

		tvElapseTimeValue = (TextView) this
				.findViewById(R.id.tvElapseTimeValue);
		tvSyncResultValue = (TextView) this
				.findViewById(R.id.tvSyncResultValue);
		tvSyncProgressValue = (TextView) this
				.findViewById(R.id.tvSyncProgressValue);
	}

	/**
	 * 时钟触发器，为了更新耗费时间
	 */
	private Timer elapseTimer;

	/**
	 * 配合时钟触发器更新耗费时间
	 */
	private Handler mHandler = new Handler();

	/**
	 * 记录同步开始时间
	 */
	private long startTime;
	/**
	 * 耗费时间
	 */
	private String elapseTime;

	private boolean isSync = false;

	private boolean isTimerRun = false;

	/**
	 * 时钟触发器
	 */
	class Elapse_Timer extends TimerTask {
		@Override
		public void run() {
			// 获取当前时间
			long diff = System.currentTimeMillis() - startTime;
			diff = diff / 1000;
			int min = (int) diff / 60; // 分钟
			int sec = (int) diff % 60; // 秒
			elapseTime = min + "分" + sec + "秒";
			mHandler.post(new Runnable() {
				public void run() {
					isTimerRun = true;
					if (null != tvElapseTimeValue)
						tvElapseTimeValue.setText(elapseTime);
				}
			});
		}
	};

	/**
	 * 开始同步
	 */
	private void StartSync() {
		final MessageBox msg = new MessageBox(this);
		if (null == etUserName.getText()
				|| "".equals(etUserName.getText().toString())) {
			msg.ShowMsg(null, "请输入账号名称!",
					MessageBox.MessageBoxButton.Button_Confirm, null);
			return;
		}
		if (null == etPassword.getText()
				|| "".equals(etPassword.getText().toString())) {
			msg.ShowMsg(null, "请输入密码名称!",
					MessageBox.MessageBoxButton.Button_Confirm, null);
			return;
		}
		tvSyncProgressValue.setText("正在连接服务器!");
		tvSyncResultValue.setText("");
		elapseTimer = new Timer();
		startTime = System.currentTimeMillis();
		elapseTimer.schedule(new Elapse_Timer(), 0, 1000);
		isSync = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = delete_update_handler.obtainMessage(1,
						"连接服务器成功！");
				delete_update_handler.sendMessage(message);
			}
		}).start();
	}

	private Handler delete_update_handler = new Handler() {
		public void handleMessage(Message message) {
			final MessageBox msg = new MessageBox(FrmLoginSyncActivity.this);
			if (message.what == 1) {
				int loginStatus = 0;
				if (loginStatus == 0) {
					tvSyncProgressValue.setText(message.obj.toString());
					SharedPreferences.Editor editor = settings.edit();

					editor.putString(ServerConfigPreference.companyCode, "001");
					editor.putString(ServerConfigPreference.EmpID, etUserName
							.getText().toString());
					editor.putString(ServerConfigPreference.PASSWORD,
							etPassword.getText().toString());
					editor.putBoolean(ServerConfigPreference.FIRST_SYNC, true);
					editor.commit();
					isCansel = false;

					/**
					 * 开始同步
					 */
					SyncInfo loginInfo = new SyncInfo();
					loginInfo.setCompanyName(settings.getString(
							ServerConfigPreference.companyCode, "001"));
					loginInfo.setEmpId(etUserName.getText().toString());
					loginInfo.setPwd(etPassword.getText().toString());
					loginInfo.setEmpCode(etUserName.getText().toString());
					loginInfo.setPackageName("AssetsRFID");
					appInfo.syncInfoEntity = new SyncInfoEntity();
					appInfo.syncInfoEntity.setSyncId(GetGuid.GenerateGUID());
					appInfo.syncInfoEntity.setStartDt(GetDate
							.GenerateTimeDate());

					if (SyncType == FullSyncType || SyncType == FirstSync) {
						tvSyncProgressValue.setText("服务器正在准备数据，请等待...");
						appInfo.syncInfoEntity.setSyncVersion("2");// 存储同步信息
						SyncOperation syncOperation = new SyncOperation(
								getApplicationContext(), handler, true,
								loginInfo);
						syncOperation.execute();
					} else if (SyncType == IncrementSyncType) {
						tvSyncProgressValue.setText("正在生成需上传的数据文件,请等待....");
						appInfo.syncInfoEntity.setOrderNum(loginManager
								.getOrderNum());
						appInfo.syncInfoEntity.setForecastNum(loginManager
								.getForecastNum());
						appInfo.syncInfoEntity.setVisitNum(loginManager
								.getVisitNum());
						appInfo.syncInfoEntity.setSyncVersion("1");
						SyncOperation syncOperation = new SyncOperation(
								getApplicationContext(), handler, false,
								loginInfo);
						syncOperation.execute();
					} 
				}
				if (loginStatus == 1) {
					msg.ShowMsg(null,
							getResources().getString(R.string.FAILD_LOGIN),
							MessageBox.MessageBoxButton.Button_Confirm, null);
					return;
				}
				if (loginStatus == 2) {
					msg.ShowMsg(null,
							getResources().getString(R.string.No_Network),
							MessageBox.MessageBoxButton.Button_Confirm, null);
					return;
				}
				if (loginStatus == 3) {
					msg.ShowMsg(null,
							getResources().getString(R.string.SERVER_ERROR),
							MessageBox.MessageBoxButton.Button_Confirm, null);
					return;
				}
				if (loginStatus == 4) {
					msg.ShowMsg(null, "网络异常",
							MessageBox.MessageBoxButton.Button_Confirm, null);
					return;
				}
			} else if (message.what == 2) {
				tvSyncProgressValue.setText(message.obj.toString());
				msg.ShowMsg(null, "连接服务器失败",
						MessageBox.MessageBoxButton.Button_Confirm, null);
				isSync = false;
				elapseTimer.cancel();
			}
		}
	};

	private Handler handler = new Handler() {
		//
		public void handleMessage(Message msg) {
			// 当用户按取消后，不进行任何同步结果相关提示
			if (msg.what == 2) {
				// removeDialog(DIALOG_LOGIN);
				String msg1 = msg.getData().getString("msg");
				tvSyncProgressValue.setText(msg1);
				// Bundle args = new Bundle();
				// args.putString("msg", msg1);
//				 showDialog(DIALOG_LOGIN, args);
			} else {
				ConfigSync.specialUploadTables = null;
//				if (!FrmSettingActivity.isCansel) {
					if (msg != null) {
//						appInfo.syncInfoEntity.setEndDt(GetDate
//								.GenerateTimeDate());
						Bundle bundle = msg.getData();
						if (bundle.getBoolean("SUCCESSFUL")) {
//							if (SyncType == IncrementSyncType) {
//							new Thread(new Runnable() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									ws_delete_updata_data("updateGMAll");// ws_updata_data
//								}
//							}).start();
//							}
							showDevMsg("同步数据成功");

							if (SyncType == FullSyncType) {
								editor.putBoolean(
										getString(R.string.sp_key_save_username_state),
										true);
								editor.putString(getString(R.string.sp_key_username),
										etUserName.getText().toString());
								if (settings.getBoolean(
												getString(R.string.sp_key_save_password_state),
												true)) {
									editor.putString(
											getString(R.string.sp_key_password),
											etPassword.getText().toString());
								}
								editor.commit();
							}

							appInfo.syncInfoEntity.setSyncResult("1");
							appInfo.syncInfoEntity.setErrorMsg("");
							// 停止时钟触发器
							elapseTimer.cancel();
							tvSyncResultValue.setText("同步数据成功");
							tvSyncProgressValue.setText("同步数据成功");
							editor.putBoolean(
									ServerConfigPreference.FIRST_SYNC, false);
							if (loginManager.loginLocal(etUserName.getText()
									.toString(), etPassword.getText()
									.toString())) {
								editor.putBoolean(
										ServerConfigPreference.FIRST_SYNC, true);
								Intent intent = new Intent(
										FrmLoginSyncActivity.this, MainActivity.class);
								startActivity(intent);
								intent = null;
								finish();
							}
							editor.commit();
							isSync = false;
						} else {
							String ssusMessage = bundle.getString("ERROR");
							elapseTimer.cancel();
							tvSyncResultValue.setText("同步失败:" + ssusMessage);
							tvSyncProgressValue.setText("同步失败:" + ssusMessage);
							isSync = false;
						}

//						appInfo.syncInfoEntity = null;
					}
//				}
			}
		}
	};

	private boolean ws_delete_updata_data(String MethodName) {
		boolean deleteData = false;
		String tables = "ORDER_TABLE;ORDER_PRESENT;ORDER_FORECAST_PDA;VISIT_TASK";
		String EmpCode = settings.getString(ServerConfigPreference.EMP_CODE,
				"001");
		Object result = null;
		WebServiceAttribute wsAttribute = new WebServiceAttribute();
		wsAttribute.setNameSpace(context
				.getString(R.string.ws_changePassword_nameSpace));
		wsAttribute.setServiceURL(LoginManager
				.getServiceIPAddress(getApplicationContext())
				+ context.getString(R.string.ws_path_changePassword));
		wsAttribute.setMethodName(MethodName);
		List<WebAttrKV> myList = new ArrayList<WebAttrKV>();
		WebAttrKV myKV = new WebAttrKV();
		myKV.setKey("args0");
		myKV.setValue(EmpCode);
		myList.add(myKV);
		WebAttrKV myKV2 = new WebAttrKV();
		myKV2.setKey("args1");
		myKV2.setValue(tables);
		myList.add(myKV2);
		try {
			WebServiceConn conn = new WebServiceConn(context);
			result = conn.getSoapObject(wsAttribute, myList,
					WebServiceConn.NORMAL);
			conn.setTimeOut(30000);
			if (result != null) {
				SoapObject resultSoap = (SoapObject) result;
				result = resultSoap.getProperty("return");

				deleteData = Boolean.parseBoolean(result.toString());
			}
		} catch (Exception e) {
			Log.e("服务器同步前的数据处理", e);
			deleteData = false;
		}

		return deleteData;
	}


	private void releaseResources() {
		etUserName = null;
		etPassword = null;
		settings = null;
		txSynAddress = null;
		rgSync = null;
		tvElapseTimeValue = null;
		tvSyncResultValue = null;
		context = null;
		elapseTimer = null;
		mHandler = null;
		elapseTime = null;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseResources();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.app_title_left:
			backClick();
			break;
		case R.id.app_title_right:
			satrtSyncClicked();
			break;
		case R.id.btSaveSynAddress:
			backClick();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		backClick();
	}

	public void satrtSyncClicked(){
		// 如果正在同步则返回
		if (isSync) {
			MessageBox msg = new MessageBox(context);
			msg.ShowMsg(null, "同步已运行!",
					MessageBox.MessageBoxButton.Button_Confirm, null);
			return;
		}
		String msg = "";
		if (SyncType == FullSyncType || SyncType == FirstSync) {
			msg = "初始化同步将清空数据并重新下载,是否继续?";
		} else if (SyncType == IncrementSyncType) {
			msg = "增量同步将上传本地数据并下载服务器数据,是否继续?";
		} 
		if (FrmLoginSyncActivity.this.getIntent().getFlags() != Intent.FLAG_ACTIVITY_NO_USER_ACTION
				&& rgSync.isChecked()) {
			new AlertDialog.Builder(FrmLoginSyncActivity.this)
					.setTitle(msg)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									StartSync();
								}
							})
					.setNegativeButton(R.string.CANCEL,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									// finish();
								}
							}).show();
		} else {
			StartSync();
		}
	}
	public void backClick(){
		if (isSync) {
		MessageBox msg = new MessageBox(context);
		msg.ShowMsg(null, "同步正在进行,不可返回",
				MessageBox.MessageBoxButton.Button_Confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
					}
				});
		} else {
			if (isTimerRun)
				elapseTimer.cancel();
			finish();
		}
	}
}
