package com.cw.assetsrfid.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.cw.assetsrfid.R;
import com.neil.myandroidtools.log.Log;


public class Utils {

	/**
	 * MD5加密转码
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			Log.e("getMD5Str1:", e);
			return "";
		} catch (UnsupportedEncodingException e) {
			Log.e("getMD5Str2:", e);
			return "";
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		//
		return md5StrBuff.toString().toUpperCase();
	}

	/**
	 * MD5加密转码
	 * 
	 * @param srcFile
	 * @return
	 */
	public static String getMD5Str(File srcFile) {
		String fileMd5 = "md5err";
		if (srcFile != null && srcFile.exists()) {
			FileInputStream fis = null;
			try {
				int buffLength = 1024;
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();

				fis = new FileInputStream(srcFile);
				byte[] buffer = new byte[buffLength];
				int read = fis.read(buffer, 0, buffLength);

				while (read > -1) {
					messageDigest.update(buffer, 0, read);
					read = fis.read(buffer, 0, buffLength);
				}

				byte[] byteArray = messageDigest.digest();

				StringBuffer md5StrBuff = new StringBuffer();

				for (int i = 0; i < byteArray.length; i++) {
					if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
						md5StrBuff.append("0").append(
								Integer.toHexString(0xFF & byteArray[i]));
					} else {
						md5StrBuff.append(Integer
								.toHexString(0xFF & byteArray[i]));
					}
				}

				fileMd5 = md5StrBuff.toString().toLowerCase(Locale.CHINA);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return fileMd5;
			} catch (IOException e) {
				e.printStackTrace();
				return fileMd5;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return fileMd5;
			}
		}
		return fileMd5;
	}
	
	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = "err";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return versionName;
		}
		return versionName;
	}
	
}
