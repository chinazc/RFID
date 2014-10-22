package com.cw.assetsrfid.login.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.common.FileUtils;
import com.cw.assetsrfid.common.NetworkConnection;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.common.Utils;
import com.cw.assetsrfid.entity.CheckApp;
import com.cw.assetsrfid.entity.LoginEntity;
import com.cw.assetsrfid.entity.LoginUserEntity;
import com.cw.assetsrfid.login.logic.LoginManager;
import com.cw.assetsrfid.login.logic.LoginManager.LoginResult;
import com.cw.assetsrfid.main.MainActivity;

public class LoginActivity extends AbstractBaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private EditText edit_login_username, edit_login_password;

	// 以下是版本更相关的东西，
	private final int DIALOG_UPDATE = 1000;
	private CheckApp checkApp;
	private ProgressBar pb_download;
	private DialogOnClickListener dialogOnClickListener = new DialogOnClickListener();
	private TextView tv_dialog_msg;

	
	private CheckBox saveCheckBox;
	private TextView user,pw;
	private Button buttonLogin,setButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		
	}

	private void init() {
		// TODO Auto-generated method stub
		edit_login_username = (EditText)findViewById(R.id.editTextUsername);
		edit_login_password = (EditText)findViewById(R.id.editTextPassword);
		
		saveCheckBox = (CheckBox)findViewById(R.id.checkBoxSaveUser);
		saveCheckBox.setOnCheckedChangeListener(this);
		setButton=(Button)findViewById(R.id.buttonSetserver);
		setButton.setOnClickListener(this);
		buttonLogin=(Button)findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		showLoginInfo();
	}

	/**
	 * 显示登录信息，如用户名，密码
	 */
	private void showLoginInfo() {
//		/**
//		 * 配置参数
//		 */
//		ArrayList<String> mList = new ArrayList<String>();
//		mList.add(appInfo.ws_IpAddressHost);
//		ConfigSync.getIpDefault = settings.getString(
//				ServerConfigPreference.IP_ADDRESS, "");
//		if ("".equals(ConfigSync.getIpDefault)) {
//			ConfigSync.getIpDefault = appInfo.ws_IpAddressHost;
//		} else {
//			ConfigSync.getIpDefault = ConfigSync.getIpDefault
//					+ appInfo.syncPort;
//		}
//		ConfigSync.getIPs = mList;
//		ConfigSync.ws_SyncServiceURL = appInfo.ws_SyncServiceURL;
//		ConfigSync.ws_TransputServiceURL = appInfo.ws_TransputServiceURL;
//		ConfigSync.db_create_from_server = false;
//		ConfigSync.database_source_name = appInfo.database_source_name;
//		ConfigSync.encryptPasswordMethod = EncryptPasswordMethod.md5;
//		ConfigSync.database_current_name = "MEAM.sqlite";
//		ConfigSync.db_saving_log = appInfo.save_log;

//		boolean cbu = settings.getBoolean(
//				getString(R.string.sp_key_save_username_state), false);
		boolean cbp = settings.getBoolean(
				getString(R.string.sp_key_save_password_state), false);
//		cb_username.setChecked(cbu);
		saveCheckBox.setChecked(cbp);

		String userName = null;
		String password = null;
//		if (cb_username.isChecked()) {
			userName = settings.getString(getString(R.string.sp_key_username),
					"");
//		}

		if (saveCheckBox.isChecked()) {
			password = settings.getString(getString(R.string.sp_key_password),
					"");
		}

		edit_login_username.setText(userName);
		edit_login_password.setText(password);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String key = null;
		String removeKey = null;
		int viewId = buttonView.getId();
		switch (viewId) {
		case R.id.checkBoxSaveUser:// 记住密码
			key = getString(R.string.sp_key_save_password_state);
			removeKey = getString(R.string.sp_key_password);
//			if (isChecked) {
//				cb_username.setChecked(true);
//			}
			break;
		default:
			break;
		}
		if (key != null) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(key, isChecked);
			if (!isChecked && removeKey != null) {
				editor.remove(removeKey);
			}
			editor.commit();
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.buttonLogin:
			login();
			break;
		case R.id.buttonSetserver:
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, FrmLoginSyncActivity.class);
		intent.putExtra("syncType", 1);
		startActivity(intent);
		break;
		case R.id.app_title_left:
			exitApp();
			break;
		default:
			break;
		}
	}

	/**
	 * 登录
	 */
	private void login() {
		String userName = edit_login_username.getText().toString();
		String password = edit_login_password.getText().toString();
		if ("".equals(userName)) {
			showDevMsg("请输入用户名！");
			return;
		}
		if ("".equals(password)) {
			showDevMsg("请输入密码！");
			return;
				}
		LoginEntity loginInfo = new LoginEntity();
		loginInfo.setUserName(userName);
		loginInfo.setPassword(password);

		boolean needInit = settings.getBoolean(
				ServerConfigPreference.FIRST_SYNC, false);// 是否已经同步
		loginInfo.setNeedInitData(!needInit);

		new LoginTask().execute(loginInfo);
	}

	/**
	 * 保存登录信息，如用户名，密码
	 */
	private void saveLoginInfo() {
//		cb_username.setChecked(true);
		String userName = edit_login_username.getText().toString();
		String password = edit_login_password.getText().toString();
		SharedPreferences.Editor editor = settings.edit();
		if (saveCheckBox.isChecked()) {
			editor.putString(getString(R.string.sp_key_password), password);
			editor.putString(ServerConfigPreference.PASSWORD, password);
		}
		editor.commit();
	}

	/**
	 * 登录
	 * 
	 * @author lee
	 * 
	 */
	class LoginTask extends AsyncTask<LoginEntity, Void, LoginEntity> {

		private ProgressDialog waitDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			waitDialog = (ProgressDialog) getDialog(DialogType.WaitDialog);
			waitDialog.setMessage("登录系统中……");
			waitDialog.show();
		}

		@Override
		protected LoginEntity doInBackground(LoginEntity... params) {
			LoginEntity login = params[0];
			LoginManager lm = new LoginManager(getApplicationContext());
			boolean needSync = login.isNeedInitData();
			if (needSync) { // 第一次登录，需要做在线登录
				// 检查手机网络
				if (!NetworkConnection.isNetworkAvailable(LoginActivity.this)) {
					showBuilder("当前网络不可用，请打开网络后再试。");
				} else {
					login = lm.loginOnline(login);
				}

			} else {
				login = lm.loginLocal(login);
			}
			return login;
		}

		@Override
		protected void onPostExecute(LoginEntity result) {
			super.onPostExecute(result);
			if (waitDialog != null && waitDialog.isShowing()) {
				waitDialog.cancel();
			}

			String msg = null;
			LoginUserEntity loginUser = appInfo.getLoginUser();
			LoginResult loginResult = result.getResult();
			switch (loginResult) {
			case LoginOK:
				saveLoginInfo();
				if (result.isNeedInitData()) {
					startInitSync(loginUser);
				} else {
					loginUser.setLogin(true);
					Intent mainIntent = new Intent();
					mainIntent.setClass(getApplicationContext(), MainActivity.class);
					startActivity(mainIntent);
					finish();
				}
				break;
			case UserOrPWDErr:
				msg = "用户名或者密码错误！";
				break;
			case PWDErr:
				msg = "密码错误！";
				break;
			case UserErr:
				msg = "用户名不存在！";
				break;
			case Local_data_err:
				msg = "数据异常";
				break;
			case LoginFail:
				msg = "登录失败";
				break;
			case NetErr:
				msg = "网络异常";
				break;
			case ServerErr:
				msg = "服务器异常";
				break;
			case UnKnownErr:
				msg = "未知异常";
				break;
			default:
				msg = "未知异常";
				break;
			}

			if (msg != null) {
				AlertDialog loginDialog = (AlertDialog) getDialog(DialogType.WarnDialog);
				loginDialog.setMessage(msg);
				loginDialog.show();
			}
		}
	}

	private void startInitSync(LoginUserEntity loginUser) {
		Intent syncIntent = new Intent();
		syncIntent.putExtra("user", loginUser.getEmpID());
		syncIntent.putExtra("pw", loginUser.getPassword());
		syncIntent.putExtra("syncType", 4);
		syncIntent
				.setClass(getApplicationContext(), FrmLoginSyncActivity.class);
		startActivity(syncIntent);
		finish();
	}

	@Override
	public void onBackPressed() {
		exitApp();
	}

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		menu.setHeaderIcon(android.R.drawable.ic_dialog_info);
//		menu.setHeaderTitle(R.string.text_menu);
//		getMenuInflater().inflate(R.menu.menu_login_more, menu);
//		super.onCreateContextMenu(menu, v, menuInfo);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		Intent nextIntent = new Intent();
//		int itemId = item.getItemId();
//		switch (itemId) {
//		case R.id.menu_sync_settings:// 同步设置
//			nextIntent.setClass(getApplicationContext(),
//					SyncSettingsActivity.class);
//			startActivity(nextIntent);
//			break;
//		case R.id.menu_app_update:// 版本更新
//			nextIntent.setClass(getApplicationContext(), UpdateAPP.class);
//			nextIntent.putExtra("isManaul", true);
//			startActivity(nextIntent);
//			break;
//		case R.id.menu_data_backup:// 数据备份
//			new BRDataTask(itemId).execute();
//			break;
//		case R.id.menu_data_restore:// 数据还原
//			new BRDataTask(itemId).execute();
//			break;
//		case R.id.menu_data_init:// 系统初始
//			nextIntent.setClass(Login.this, FrmLoginSyncActivity.class);
//			nextIntent.putExtra("syncType", 1);
//			startActivityForResult(nextIntent, FrmSettingActivity.OpenSettings);
//			break;
//		default:
//			break;
//		}
//		return super.onContextItemSelected(item);
//	}

//	private class BRDataTask extends AsyncTask<Integer, String, ResultMsg> {
//		private int type = 0;
//		private ProgressDialog waitDialog;
//
//		private BRDataTask(int type) {
//			this.type = type;
//			waitDialog = (ProgressDialog) getDialog(DialogType.WaitDialog);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			String msg = null;
//			if (type == R.id.menu_data_backup) {
//				msg = "正在备份数据……";
//			} else if (type == R.id.menu_data_restore) {
//				msg = "正在还原数据……";
//			}
//			waitDialog.setMessage(msg);
//			waitDialog.show();
//		}
//
//		@Override
//		protected ResultMsg doInBackground(Integer... params) {
//			ResultMsg resultMsg = new ResultMsg();
//			resultMsg.setFlag(false);
//			resultMsg.setMsg(getString(R.string.text_unknown_err));
//			LoginManager lm = new LoginManager(getApplicationContext());
//			if (type == R.id.menu_data_backup) {
//				resultMsg = lm.backupData();
//			} else if (type == R.id.menu_data_restore) {
//				resultMsg = lm.restoreData();
//			}
//			return resultMsg;
//		}
//
//		@Override
//		protected void onPostExecute(ResultMsg result) {
//			super.onPostExecute(result);
//			if (waitDialog != null && waitDialog.isShowing()) {
//				waitDialog.cancel();
//			}
//
//			int iconResId = android.R.drawable.ic_dialog_alert;
//			int titleId = R.string.text_backup;
//			String msg = getString(R.string.text_unknown_err);
//			DialogInterface.OnClickListener OKClick = null;
//			if (type == R.id.menu_data_backup) {
//				titleId = R.string.text_backup;
//				msg = result.getMsg();
//				if (result.isFlag()) {
//					iconResId = android.R.drawable.ic_dialog_info;
//				}
//			} else if (type == R.id.menu_data_restore) {
//				titleId = R.string.text_restore;
//				msg = result.getMsg();
//
//				if (result.isFlag()) {
//					iconResId = android.R.drawable.ic_dialog_info;
//					OKClick = new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							appInfo.init();
//							finish();
//							Intent reIntent = new Intent(
//									getApplicationContext(), Login.class);
//							startActivity(reIntent);
//						}
//					};
//				}
//			}
//
//			AlertDialog dialog = new AlertDialog.Builder(Login.this).create();
//			dialog.setIcon(iconResId);
//			dialog.setTitle(titleId);
//			dialog.setMessage(msg);
//			dialog.setButton(DialogInterface.BUTTON_POSITIVE,
//					getString(R.string.yes), OKClick);
//			dialog.show();
//		}
//
//	}

	public void showBuilder(String msg) {
		Builder builder = new Builder(LoginActivity.this);
		builder.setTitle("提示");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", null);
		builder.create().show();
	}

	/**
	 * @ClassName: CheckVersion
	 * @Description: TODO(由于自动检测新版本调用webservise时间不稳定,就把版本自动更新搬过来了)
	 * @author chao.liu
	 * @date 2014-9-16 下午4:37:28
	 */
	private class CheckVersion extends AsyncTask<Void, Void, CheckApp> {

		@Override
		protected CheckApp doInBackground(Void... params) {
			CheckApp checkApp = null;
			// 当前版本号
			String localVersion = Utils.getVersionName(getApplicationContext());
			// 检查是否有更新
			LoginManager lm = new LoginManager(getApplicationContext());
			checkApp = lm.getAppCheck(localVersion);
			return checkApp;
		}

		@Override
		protected void onPostExecute(CheckApp result) {
			super.onPostExecute(result);
			// 判断处理结果
			String msg = "不需要升级";
			Bundle bundle = new Bundle();
			boolean resultBool = false;
			if (result == null) {
				msg = "服务器连接失败，请检查网络连接。";
			} else if (result != null && result.getStatus()) {// 需要升级
				StringBuffer msgSB = new StringBuffer();
				msgSB.append("发现新版本：" + result.getAppVersion());
				msg = msgSB.toString();
				resultBool = result.getStatus();
				bundle.putBoolean("resultBool", resultBool);
			}
			bundle.putString("msg", msg);
			// 成功，有新版本，显示确定按钮
			boolean[] showBtn = { true, resultBool };// 取消，确定
			bundle.putBooleanArray("showBtn", showBtn);
			if (!msg.equals("不需要升级")) {
				showDialog(DIALOG_UPDATE, bundle);
			}
			checkApp = result;
		}
	}

	private void installApp(File appFile) {
		FileUtils.runCommand("chmod 777 " + appFile.getAbsolutePath());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(appFile),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();
	}

	private class DownloadApp extends AsyncTask<CheckApp, Integer, Object[]> {

		private File appFile = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			removeDialog(DIALOG_UPDATE);
			Bundle bundle = new Bundle();
			bundle.putString("msg", "正在下载：");
			boolean[] showBtn = { false, false };// 按钮无法点击
			bundle.putBooleanArray("showBtn", showBtn);
			bundle.putBoolean("showPB", true);
			showDialog(DIALOG_UPDATE, bundle);

			pb_download.setMax(100);

			String downloadDir = FileUtils.getCacheDir(getApplicationContext());
			FileUtils.runCommand("chmod 777 " + downloadDir);
			appFile = new File(downloadDir, "philips.apk");
			if (appFile.exists()) {
				appFile.delete();
			}
		}

		@Override
		protected Object[] doInBackground(CheckApp... params) {
			Object[] resultObj = new Object[2];
			boolean resultBool = false;
			String resultMsg = null;

			CheckApp checkApp = params[0];
			long fileSize = checkApp.getFileSize();
			String realURl = checkApp.getUrl();

			HttpURLConnection httpURLConnection = null;
			FileOutputStream out = null;
			InputStream inputStream = null;
			// 如果有新版本，下载新的app
			try {
				URL url = new URL(realURl);
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setConnectTimeout(30 * 1000);
				httpURLConnection.connect();

				out = new FileOutputStream(appFile);
				inputStream = httpURLConnection.getInputStream();
				byte[] buffer = new byte[2048];
				int len = 0;
				int total = 0;

				while ((len = inputStream.read(buffer)) != -1) {
					out.write(buffer, 0, len);
					total = total + len;
					publishProgress(total, (int) fileSize);
				}
				out.flush();
				out.close();
				inputStream.close();
				httpURLConnection.disconnect();
				resultBool = true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				resultMsg = "地址错误";
				return resultObj;
			} catch (IOException e) {
				e.printStackTrace();
				resultMsg = "下载失败！";
				return resultObj;
			} finally {
				resultObj[0] = resultBool;
				resultObj[1] = resultMsg;
			}
			return resultObj;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			int index = values[0];// 当前值
			int max = values[1];// 最大值
			if (pb_download != null) {
				pb_download.setMax(max);
				pb_download.setProgress(index);
			}
		}

		@Override
		protected void onPostExecute(Object[] result) {
			super.onPostExecute(result);
			boolean resultBool = (Boolean) result[0];
			String msg = (String) result[1];

			if (resultBool) {
				// 成功，安装app
				removeDialog(DIALOG_UPDATE);
				installApp(appFile);
			} else {
				// 失败显示提示
				Bundle bundle = new Bundle();
				bundle.putBoolean("resultBool", resultBool);
				bundle.putString("msg", msg);
				boolean[] showBtn = { true, resultBool };// 取消，确定
				bundle.putBooleanArray("showBtn", showBtn);

				showDialog(DIALOG_UPDATE, bundle);
			}

		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_UPDATE:

			AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
					.create();
			alertDialog.setCancelable(false);
			alertDialog.setTitle(R.string.text_update_rogram);
			alertDialog.setIcon(android.R.drawable.ic_dialog_info);
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					getString(R.string.no), dialogOnClickListener);
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getString(R.string.yes), dialogOnClickListener);

			LayoutInflater inflater = alertDialog.getLayoutInflater();
			View view = inflater.inflate(R.layout.dialog_download_progress,
					null);

			tv_dialog_msg = (TextView) view.findViewById(R.id.tv_dialog_msg);
			pb_download = (ProgressBar) view.findViewById(R.id.pb_download);

			alertDialog.setView(view);
			dialog = alertDialog;
			break;

		}
		return dialog;
	}

	private class DialogOnClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_NEGATIVE:
				removeDialog(DIALOG_UPDATE);
				// finish();
				overridePendingTransition(0, 0);
				break;

			case DialogInterface.BUTTON_POSITIVE:
				new DownloadApp().execute(checkApp);
				break;
			}
		}

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		super.onPrepareDialog(id, dialog, args);
		switch (id) {
		case DIALOG_UPDATE:
			AlertDialog alertDialog = (AlertDialog) dialog;
			Button negative = alertDialog
					.getButton(DialogInterface.BUTTON_NEGATIVE);
			Button positive = alertDialog
					.getButton(DialogInterface.BUTTON_POSITIVE);
			// 设置显示的消息
			String msg = args.getString("msg");
			tv_dialog_msg.setText(msg);

			// 设置按钮显示
			boolean[] showBtn = args.getBooleanArray("showBtn");
			negative.setEnabled(showBtn[0]);// 取消

			int visibility = View.GONE;
			if (showBtn[1]) {
				visibility = View.VISIBLE;
			}
			positive.setVisibility(visibility);
			// positive.setEnabled(showBtn[1]);

			// 设置是否显示下载进度条
			boolean showPB = args.getBoolean("showPB");
			if (showPB) {
				pb_download.setVisibility(View.VISIBLE);
			} else {
				pb_download.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
}
