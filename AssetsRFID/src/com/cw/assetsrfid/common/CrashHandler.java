package com.cw.assetsrfid.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.cw.assetsrfid.SFAAPPInfo;
import com.neil.myandroidtools.log.Log;

/**
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�и��� ���ӹܳ���,����¼ ���ʹ��󱨸�.
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * �Ƿ�����־���,��Debug״̬�¿���, ��Release״̬�¹ر�����ʾ��������
	 * */
	public static final boolean DEBUG = true;
	/** ϵͳĬ�ϵ�UncaughtException������ */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandlerʵ�� */
	private static CrashHandler INSTANCE;
	/** �����Context���� */
	private Context mContext;

	/** ʹ��Properties�������豸����Ϣ�ʹ����ջ��Ϣ */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";

	/** ��ֻ֤��һ��CrashHandlerʵ�� */
	private CrashHandler() {
	}

	/** ��ȡCrashHandlerʵ�� ,����ģʽ */
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * ��ʼ��,ע��Context����, ��ȡϵͳĬ�ϵ�UncaughtException������, ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * ��UncaughtException����ʱ��ת��ú���������
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// ����û�û�д�������ϵͳĬ�ϵ��쳣������������
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleepһ����������
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, e);
				return;
			}

			SFAAPPInfo appInfo = (SFAAPPInfo) mContext.getApplicationContext();
			// ɱ����ǰ����
			appInfo.exit();
		}

	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		// ʹ��Toast����ʾ�쳣��Ϣ
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "������ִ���:" + msg, Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}

		}.start();
		// �ռ��豸��Ϣ
		collectCrashDeviceInfo(mContext);
		// ������󱨸��ļ�
		saveCrashInfoToFile(ex);
		
		return true;
	}

	/**
	 * ���������Ϣ���ļ���
	 * 
	 * @param ex
	 * @return
	 */
	private void saveCrashInfoToFile(Throwable ex) {
		Set<Entry<Object, Object>> set = mDeviceCrashInfo.entrySet();
		for (Entry<Object, Object> entry : set) {
			Log.d(entry.getKey().toString());
		}
		
		Log.e("saveCrashInfoToFile:", ex);
	}

	/**
	 * �ռ�����������豸��Ϣ
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, e);
			return;
		}
		// ʹ�÷������ռ��豸��Ϣ.��Build���а��������豸��Ϣ,
		// ����: ϵͳ�汾��,�豸������ �Ȱ������Գ����������Ϣ
		// ������Ϣ��ο�����Ľ�ͼ
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				if (DEBUG) {
					Log.d(field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(TAG, e);
				return;
			}
		}
	}
}
