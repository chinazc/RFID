package com.cw.assetsrfid.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.neil.myandroidtools.log.Log;

public class GetDate {

	public static final String getFirstOfWeekDate(Calendar calendar) {
		// Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		if (dayOfWeek == -1) {
			calendar.add(Calendar.DATE, -6);
		} else {
			calendar.add(Calendar.DATE, -dayOfWeek);
		}
		SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormatDB.format(calendar.getTime());
	}

	public static final String getLastOfWeekDate() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		if (dayOfWeek == -1) {
			calendar.add(Calendar.DATE, -6);
		} else {
			calendar.add(Calendar.DATE, -dayOfWeek);
		}
		calendar.add(Calendar.DATE, 6);
		SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormatDB.format(calendar.getTime());
	}

	public static final String getServerTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			String time = ServerConfigPreference.SERVER_TIME1;
			if ("".equals(ServerConfigPreference.SERVER_TIME1))
				time = GenerateDate();
			time = time + "000";
			date = sf.parse(time);
		} catch (ParseException e) {
			Log.e("", e);
			return GenerateDate();
		}
		String a = ServerConfigPreference.RUN_TIME1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, (int) ((SystemClock.elapsedRealtime() - Long
				.parseLong(ServerConfigPreference.RUN_TIME1)) / 1000));
		return sf2.format(cal.getTime());
	}

	public static final String getServerTomorrowTime(SharedPreferences settings) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = sf.parse(settings.getString(
					ServerConfigPreference.SERVER_TIME, GenerateDate()));
		} catch (ParseException e) {
			Log.e("", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			return GenerateDate();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND,
				(int) ((SystemClock.elapsedRealtime() - settings.getLong(
						ServerConfigPreference.RUN_TIME, 0)) / 1000));
		cal.add(Calendar.DATE, 1);

		return sf2.format(cal.getTime());
	}

	public static final String getBeforeDate(String dateString, int beforeDay) {
		String result = "";
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date inputDate = sf.parse(dateString);

			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			cal.add(Calendar.DATE, -beforeDay);

			result = sf.format(cal.getTime());
		} catch (ParseException e) {
			Log.e("", e);
		}
		return result;
	}

	/**
	 * �õ����ɷ���֮���ʱ��
	 * 
	 * @param dateString
	 * @param beforeDay
	 * @return
	 */
	public static final String getTimeAfterMins(String dateString, int delayMins) {
		String result = "";
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date inputDate = sf.parse(dateString);

			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			cal.add(Calendar.MINUTE, delayMins);

			result = sf.format(cal.getTime());
		} catch (ParseException e) {
			Log.e("", e);
		}
		return result;
	}

	public static final String GenerateDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time = sf.format(date);
		return time;
	}

	public static final String GenerateTime4Date() {
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		String time = sf.format(date);
		return time;
	}

	public static final String GenerateTimeDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time = sf.format(date);
		return time;
	}

	public static final String getServerTime4Order(SharedPreferences settings) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyMMddHHmmssSSS");
		Date date = null;
		try {
			date = sf.parse(settings.getString(
					ServerConfigPreference.SERVER_TIME, GenerateDate()));
		} catch (ParseException e) {
			Log.e("", e);
			return GenerateDate();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MILLISECOND,
				(int) (SystemClock.elapsedRealtime() - settings.getLong(
						ServerConfigPreference.RUN_TIME, 0)));

		return sf2.format(cal.getTime());
	}

	public static String GenerateDate(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(time);
		return sf.format(date);
	}

	public static String getSelectsDate(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp = strDate.substring(0, 4);
			temp += "��";
			temp += strDate.substring(4, 6);
			temp += "��";
			temp += strDate.substring(6, 8);
			temp += "��";
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
			temp += ":";
			temp += strDate.substring(12, 14);
		}
		return temp;
	}

	public static String getSelectsDate1(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp = strDate.substring(0, 4);
			temp += "-";
			temp += strDate.substring(4, 6);
			temp += "-";
			temp += strDate.substring(6, 8);
			temp += " ";
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
			temp += ":";
			temp += strDate.substring(12, 14);
		}
		return temp;
	}

	public static String getSelects12Date(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp = strDate.substring(0, 4);
			temp += "��";
			temp += strDate.substring(4, 6);
			temp += "��";
			temp += strDate.substring(6, 8);
			temp += "��";
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
		}
		return temp;
	}

	public static String getSelectsTime(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
			temp += ":";
			temp += strDate.substring(12, 14);
		}
		return temp;
	}

	public static String getSelects4Time(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
		}
		return temp;
	}

	public static String getSelectsDate() {
		String systime = GetDate.GenerateDate();
		String temp = "";
		if (systime.length() >= 14) {
			temp = systime.substring(0, 4);
			temp += "��";
			temp += systime.substring(4, 6);
			temp += "��";
			temp += systime.substring(6, 8);
			temp += "��";
			temp += systime.substring(8, 10);
			temp += ":";
			temp += systime.substring(10, 12);
			temp += ":";
			temp += systime.substring(12, 14);
		}
		return temp;
	}

	public static String getSelectsDate1() {
		String systime = GetDate.GenerateDate();
		String temp = "";
		if (systime.length() >= 14) {
			temp = systime.substring(0, 4);
			temp += "��";
			temp += systime.substring(4, 6);
			temp += "��";
			temp += systime.substring(6, 8);
			temp += "��";
		}
		return temp;
	}

	public static String getSelectsDateString(String strDate) {
		StringBuilder temp = new StringBuilder();
		if (strDate.length() >= 14) {
			temp.append(strDate.substring(0, 4));
			temp.append("��");
			temp.append(strDate.substring(4, 6));
			temp.append("��");
			temp.append(strDate.substring(6, 8));
			temp.append("��");
		}
		return temp.toString();
	}

	public static String getOrderDateString(String strDate) {
		StringBuilder temp = new StringBuilder();
		if (strDate.length() >= 14) {
			temp.append(strDate.substring(0, 4));
			temp.append("/");
			temp.append(strDate.substring(4, 6));
			temp.append("/");
			temp.append(strDate.substring(6, 8));
		}
		return temp.toString();
	}

	public static String getEditTextDate(int year, int monthOfYear,
			int dayOfMonth) {
		return year
				+ "-"
				+ (monthOfYear < 10 ? "0" + String.valueOf((monthOfYear))
						: (monthOfYear))
				+ "-"
				+ (dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth)
						: dayOfMonth);
	}

	// dateStr = 2011-12-02
	// return 20111202
	public static String getChar14Date(String dateStr) {
		if (null != dateStr && !"".equals(dateStr)) {
			dateStr = dateStr.replaceAll("-", "");
			return dateStr;
		} else {
			return "";
		}

	}

	public static String getEditTextDate() {
		String date = GetDate.GenerateDate();
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);

	}

	public static int getEditTextDate1() {
		String date = GetDate.GenerateDate();
		return Integer.parseInt(date.substring(0, 4) + date.substring(4, 6)
				+ date.substring(6, 8));

	}

	public static String getEditTextDate(String date) {
		if (date.trim().equals("")) {
			return "";
		} else {
			return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
					+ date.substring(6, 8);

		}

	}

	/**
	 * ������ʱ��ת��Ϊָ����ʽ
	 * 
	 * @param type
	 *            ��yyyyMMddHHmmss
	 */
	public static String nowDate2String(String pattern) {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = new Date();
		result = sdf.format(date);
		return result;
	}

	/**
	 * ������ʱ��ת��Ϊָ����ʽ
	 * 
	 * @param type
	 *            ��yyyyMMddHHmmss
	 */
	public static String date2String(Date date, String pattern) {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		result = sdf.format(date);
		return result;
	}

	/**
	 * ȡ�ü���������
	 */
	public static String getSpecvDay(Calendar oldDay, int days) {
		// Calendar oldDay = Calendar.getInstance();
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = ""
				+ oldDay.get(Calendar.YEAR)
				+ "��"
				+ ((oldDay.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH) + 1) : ""
						+ (oldDay.get(Calendar.MONTH) + 1))
				+ "��"
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH)) + "��";
		return prevDay;
	}

	public static int getSpecvDay1(Calendar oldDay, int days) {
		// Calendar oldDay = Calendar.getInstance();
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		int prevDay = Integer.parseInt(oldDay.get(Calendar.YEAR)
				+ ((oldDay.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH) + 1) : ""
						+ (oldDay.get(Calendar.MONTH) + 1))
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH)));
		return prevDay;
	}

	public static String getSpecvDay2(Calendar oldDay, int days) {
		// Calendar oldDay = Calendar.getInstance();
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = oldDay.get(Calendar.YEAR)
				+ ((oldDay.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH) + 1) : ""
						+ (oldDay.get(Calendar.MONTH) + 1))
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH));
		return prevDay;
	}

	public static String getSpecvDay(int days) {
		Calendar oldDay = Calendar.getInstance();
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = ""
				+ oldDay.get(Calendar.YEAR)
				+ "-"
				+ ((oldDay.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH) + 1) : ""
						+ (oldDay.get(Calendar.MONTH) + 1))
				+ "-"
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH));
		return prevDay;
	}

	public static int getSpecvDay3(int days) {
		Calendar oldDay = Calendar.getInstance();
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = ""
				+ oldDay.get(Calendar.YEAR)
				+ ((oldDay.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH) + 1) : ""
						+ (oldDay.get(Calendar.MONTH) + 1))
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH));
		return Integer.parseInt(prevDay);
	}

	public static String getSpecvDay4(int days, String date) {
		Calendar oldDay = Calendar.getInstance();
		oldDay.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		oldDay.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)));
		oldDay.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(date.substring(6, 8)) + 1);
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = ""
				+ oldDay.get(Calendar.YEAR)
				+ ((oldDay.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH)) : ""
						+ (oldDay.get(Calendar.MONTH)))
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH)) + "000000";
		return prevDay;
	}

	public static String getSpecvDay5(int days, String date) {
		Calendar oldDay = Calendar.getInstance();
		oldDay.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		oldDay.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)));
		oldDay.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(date.substring(6, 8)) + 1);
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = ""
				+ oldDay.get(Calendar.YEAR)
				+ ((oldDay.get(Calendar.MONTH)) < 10 ? "0"
						+ (oldDay.get(Calendar.MONTH)) : ""
						+ (oldDay.get(Calendar.MONTH)))
				+ (oldDay.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ oldDay.get(Calendar.DAY_OF_MONTH) : ""
						+ oldDay.get(Calendar.DAY_OF_MONTH)) + "000000";
		return prevDay;
	}

	// ��2014-03-23 ����������20140323000000
	public static String six2fourteen(String dateStr) {
		if (!dateStr.equals("")) {
			dateStr = dateStr.replace("-", "");
			dateStr += "000000";
		}
		return dateStr;
	}

	// ��ȡһ�쿪ʼʱ��
	public static String getTodayBegin() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String time = sf.format(date);
		time += "000000";
		return time;
	}

	// ��ȡһ�����ʱ��
	public static String getTodayEnd() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String time = sf.format(date);
		time += "235959";
		return time;
	}

	/**
	 * ���ݴ����ʱ��õ�ǰ���ʱ�䣬����Ϊ������Ϊǰ
	 * 
	 * @param count
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String precedingDateAbout(int count, String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar now = Calendar.getInstance();
			now.setTime(sdf.parse(date));
			now.add(Calendar.DAY_OF_WEEK, count);
			return sdf.format(now.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
