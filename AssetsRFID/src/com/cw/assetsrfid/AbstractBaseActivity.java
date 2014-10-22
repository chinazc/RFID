package com.cw.assetsrfid;

import java.util.Calendar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.assetsrfid.entity.LoginUserEntity;
import com.cw.assetsrfid.login.logic.LoginManager;

public class AbstractBaseActivity extends Activity {

	/**
	 * Application SFAAPPInfo
	 */
	public SFAAPPInfo appInfo;

	private Context context;
	/**
	 * SharedPreferences
	 */
	public SharedPreferences settings;
	String activityName = "";
	/**
	 * DisplayMetrics
	 */
	public DisplayMetrics dm;

	protected TextView titleName;
	protected TextView titleOther;
	protected TextView btnLeft, btnRight;
	protected Calendar c;
	protected LoginUserEntity loginUser;
	private LoginManager logic = null;
	ActivityManager manager;
	Intent intent;
	protected final int pageSize = 20;
	public enum LoadModel {
		// 默认加载
		FIRST_LOAD,
		// 根据条件查询加载
		QUERY_LOAD,
		// 增量加载
		MORE_LOAD;
	}
	
	// 状态返回码
	public final int OPREATION_ADD_OK = 1;// 增加成功
	public final int OPREATION_edit_OK = 2;// 修改成功
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInfo = (SFAAPPInfo) getApplication();
		appInfo.activityManager.add(this);
		appInfo.checkAPP(this);
		settings = appInfo.settings;
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		loginUser = appInfo.getLoginUser();
		logic = new LoginManager(this);
		context = AbstractBaseActivity.this;
		manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		appInfo.activityManager.remove(this);
		appInfo = null;
		settings = null;
		dm = null;
		super.onDestroy();
	}

	/**
	 * 退出应用，警告用户
	 */
	protected void exitApp() {
		AlertDialog exitDialog = new AlertDialog.Builder(this).create();
		exitDialog.setIcon(android.R.drawable.ic_dialog_alert);
		exitDialog.setTitle(R.string.warn);
		exitDialog.setMessage(getString(R.string.MSG_002));
		exitDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.yes), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exit();
					}
				});
		exitDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.no), (DialogInterface.OnClickListener) null);
		exitDialog.show();
	}

	/**
	 * 退出应用
	 */
	public void exit() {
		appInfo.exit();
	}

	/**
	 * 开发时临时显示文字信息
	 */
	protected void showDevMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public enum DialogType {
		AlertDialog, WaitDialog, WarnDialog
	}

	protected Dialog getDialog(DialogType dialogType) {
		return getDialog(dialogType, null);
	}

	protected Dialog getDialog(DialogType dialogType, Bundle args) {
		Dialog dialog = null;
		switch (dialogType) {
		case AlertDialog:
			break;
		case WaitDialog:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载数据......");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			dialog = progressDialog;
			break;
		case WarnDialog:
			String msg = null;
			if (args != null) {
				msg = args.getString("msg");
			}
			AlertDialog warnDialog = new AlertDialog.Builder(this).create();
			warnDialog.setIcon(android.R.drawable.ic_dialog_alert);
			warnDialog.setTitle(R.string.warn);
			warnDialog.setMessage(msg);
			warnDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getString(R.string.yes),
					(DialogInterface.OnClickListener) null);
			dialog = warnDialog;
			break;
		default:
			break;
		}

		return dialog;
	}

    public ProgressDialog getProgressDialog()
    {
    	ProgressDialog mdlgProcess = new ProgressDialog(this);  
    	mdlgProcess.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条  
       	mdlgProcess.setCancelable(true);// 设置是否可以通过点击Back键取消  
       	mdlgProcess.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条  
       	mdlgProcess.setIcon(R.drawable.logo);//  
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的  
       	mdlgProcess.setTitle("");  
        //设置可点击的按钮，最多有三个(默认情况下)  
       	mdlgProcess.setMessage("正在发送请求...");  
       	return mdlgProcess;
            //dialog.show(); 
    }
	public class MyDatePickerDialog extends DatePickerDialog {

		public MyDatePickerDialog(Context context, OnDateSetListener callBack,
				int year, int monthOfYear, int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
		}

		@Override
		protected void onStop() {
			// super.onStop();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		activityName = manager.getRunningTasks(1).get(0).topActivity
				.getClassName();
	}

}
