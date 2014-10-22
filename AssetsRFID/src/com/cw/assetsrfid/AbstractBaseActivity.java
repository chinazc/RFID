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
		// Ĭ�ϼ���
		FIRST_LOAD,
		// ����������ѯ����
		QUERY_LOAD,
		// ��������
		MORE_LOAD;
	}
	
	// ״̬������
	public final int OPREATION_ADD_OK = 1;// ���ӳɹ�
	public final int OPREATION_edit_OK = 2;// �޸ĳɹ�
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
	 * �˳�Ӧ�ã������û�
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
	 * �˳�Ӧ��
	 */
	public void exit() {
		appInfo.exit();
	}

	/**
	 * ����ʱ��ʱ��ʾ������Ϣ
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
			progressDialog.setMessage("���ڼ�������......");
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
    	mdlgProcess.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���ý���������ʽΪԲ��ת���Ľ�����  
       	mdlgProcess.setCancelable(true);// �����Ƿ����ͨ�����Back��ȡ��  
       	mdlgProcess.setCanceledOnTouchOutside(false);// �����ڵ��Dialog���Ƿ�ȡ��Dialog������  
       	mdlgProcess.setIcon(R.drawable.logo);//  
        // ������ʾ��title��ͼ�꣬Ĭ����û�еģ����û������title�Ļ�ֻ����Icon�ǲ�����ʾͼ���  
       	mdlgProcess.setTitle("");  
        //���ÿɵ���İ�ť�����������(Ĭ�������)  
       	mdlgProcess.setMessage("���ڷ�������...");  
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
