package com.cw.assetsrfid.common;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkConnection {

	/**
	 * �??判断网络连接是否可用 二�?判断GPS是否打开 三�?判断WIFI是否打开 四�?判断是否�?G网络
	 * 五�?判断是wifi还是3g网络,用户的体现�?在这里了，wifi就可以建议下载或者在线播放�?
	 * 
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// 如果仅仅是用来判断网络连�?
			// 则可以使�?cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * �?��网络是否连接
	 * 
	 * @return
	 */
	private boolean isNetConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
