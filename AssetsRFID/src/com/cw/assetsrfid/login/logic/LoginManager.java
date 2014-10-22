package com.cw.assetsrfid.login.logic;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.SystemClock;

import com.cw.assetsrfid.R;
import com.cw.assetsrfid.SFAAPPInfo;
import com.cw.assetsrfid.common.FileUtils;
import com.cw.assetsrfid.common.GetDate;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.common.Utils;
import com.cw.assetsrfid.entity.CheckApp;
import com.cw.assetsrfid.entity.LoginEntity;
import com.cw.assetsrfid.entity.ResultMsg;
import com.cw.assetsrfid.entity.SyncInfoEntity;
import com.neil.myandroidtools.log.Log;
import com.zerowire.framework.db.DBManager;
import com.zerowire.framework.sync.config.ConfigSync;
import com.zerowire.framework.sync.config.ConfigSync.DbConnType;
import com.zerowire.framework.sync.ws.WebAttrKV;
import com.zerowire.framework.sync.ws.WebServiceAttribute;
import com.zerowire.framework.sync.ws.WebServiceConn;
import com.zerowire.framework.sync.xml.ZipUtils;

public class LoginManager {

	private Context context;
	private SharedPreferences settings;
	private SFAAPPInfo appInfo;

	public LoginManager(Context context) {
		this.context = context;
		this.appInfo = (SFAAPPInfo) context.getApplicationContext();
		this.settings = ((SFAAPPInfo) context.getApplicationContext()).settings;
		// ArrayList<String> mList = new ArrayList<String>();
		// mList.add(appInfo.ws_IpAddressHost);
		// ConfigSync.getIpDefault = settings.getString(
		// ServerConfigPreference.IP_ADDRESS, "");
		// if ("".equals(ConfigSync.getIpDefault)) {
		// ConfigSync.getIpDefault = appInfo.ws_IpAddressHost;
		// } else {
		// ConfigSync.getIpDefault = ConfigSync.getIpDefault
		// + appInfo.syncPort;
		// }
		// ConfigSync.getIPs = mList;
		// ConfigSync.ws_SyncServiceURL = appInfo.ws_SyncServiceURL;
		// ConfigSync.ws_TransputServiceURL = appInfo.ws_TransputServiceURL;
		// ConfigSync.db_create_from_server = false;
		// ConfigSync.database_source_name = appInfo.database_source_name;
		// ConfigSync.encryptPasswordMethod = EncryptPasswordMethod.md5;
		// ConfigSync.database_current_name = "BrightDairy.sqlite";
		// ConfigSync.db_saving_log = appInfo.save_log;
	}

	/**
	 * 登录结果
	 * 
	 * @author lee
	 * 
	 */
	public enum LoginResult {
		/**
		 * 登录成功
		 */
		LoginOK,

		/**
		 * 本地数据异常,输入的数据不在本地数据库
		 */
		Local_data_err,

		/**
		 * 用户名或者密码错误
		 */
		UserOrPWDErr,
		UserErr,
		PWDErr,
		/**
		 * 登录失败
		 */
		LoginFail,
		/**
		 * 网络异常
		 */
		NetErr,
		/**
		 * 服务器异常
		 */
		ServerErr,
		/**
		 * 未知异常
		 */
		UnKnownErr
	}

	/**
	 * 在线登录
	 * 
	 * @param loginEntity
	 * @return
	 */
	public LoginEntity loginOnline(LoginEntity loginEntity) {
		LoginResult result = LoginResult.LoginFail;
		String userName = loginEntity.getUserName();
		String password = loginEntity.getPassword();
		String pwdMD5 = loginEntity.getMD5PassWord();
		int loginState = LoginOnline("001", userName, password, pwdMD5);
		switch (loginState) {
		case 0:// 成功
			result = LoginResult.LoginOK;
			break;
		case 1:// 用户名不正确
			result = LoginResult.UserErr;
			break;
		case 2:// 密码不正确
			result = LoginResult.PWDErr;
			break;
		case 3:// 未知异常
			result = LoginResult.UnKnownErr;
			break;
		case 4:// 未知异常
			result = LoginResult.UnKnownErr;
			break;
		default:
			result = LoginResult.UnKnownErr;
			break;
		}
		loginEntity.setResult(result);
		return loginEntity;
	}

	/**
	 * 本地登陆验证并为全局信息对象赋值, 验证登录时使用的数据在数据库中是否已经存在
	 * 
	 * @param loginEntity
	 * @return
	 */
	public LoginEntity loginLocal(LoginEntity loginEntity) {
		LoginResult result = LoginResult.UserOrPWDErr;
		String userName = loginEntity.getUserName();
		String password = loginEntity.getPassword();
		boolean dataOK = loginLocal(userName, password);
		if (dataOK) {
			result = LoginResult.LoginOK;
		}
		loginEntity.setResult(result);
		return loginEntity;
	}

	/**
	 * checkLocal 本地登陆验证并为全局信息对象赋值, 验证登录时使用的数据在数据库中是否已经存在
	 * 
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public boolean loginLocal(String loginID, String pwd) {
		boolean flag = false;
		Cursor result = null;
		DBManager dbManager = null;
		try {
			
			String pw = Utils.getMD5Str(pwd);
			String sql = "SELECT * FROM FND_USER WHERE USER_ID = '"+loginID+"' AND PASSWORD = '"+pw+"'";
			dbManager = new DBManager(context);
			result = dbManager.rawQuery(sql,null);
			while (result.moveToNext()) {
//				String empCode = result.getString(result
//						.getColumnIndex("EMP_CODE")) == null ? "" : result
//						.getString(result.getColumnIndex("EMP_CODE"));
//				String empName = result.getString(result
//						.getColumnIndex("EMP_NAME")) == null ? "" : result
//						.getString(result.getColumnIndex("EMP_NAME"));
//				String deptCode = result.getString(result
//						.getColumnIndex("DEPT_CODE")) == null ? "" : result
//						.getString(result.getColumnIndex("DEPT_CODE"));
//				String companyCode = result.getString(result
//						.getColumnIndex("COMPANY_CODE")) == null ? "" : result
//						.getString(result.getColumnIndex("COMPANY_CODE"));
//				String role = result
//						.getString(result.getColumnIndex("ROLE_ID")) == null ? ""
//						: result.getString(result.getColumnIndex("ROLE_ID"));

				SharedPreferences.Editor editor = settings.edit();
				editor.putString(ServerConfigPreference.EmpID, loginID);
				editor.putString(ServerConfigPreference.PASSWORD, pwd);
//				editor.putString(ServerConfigPreference.EMP_CODE, empCode);
//				editor.putString(ServerConfigPreference.EMP_NAME, empName);
//				editor.putString(ServerConfigPreference.companyCode,
//						companyCode);
//				editor.putString(ServerConfigPreference.DEPT_CODE, deptCode);
//				editor.putString(ServerConfigPreference.ROLE, role);
				editor.commit();

				appInfo.setLoginUser(null);// 更新登录信息

				flag = true;
				break;
			}
		} catch (Exception e) {
			Log.e("本地登录验证：", e);
			return false;
		} finally {
			if (result != null) {
				result.close();
			}
			if (dbManager != null) {
				dbManager.close();
			}
		}
		return flag;
	}

	/**
	 * loginOnLine
	 * 
	 * @param companySName
	 * @param empID
	 * @param pwd
	 * @return int 0.成功,1.登陆失败,2.网络不通,3.服务器异常,4.未知异常
	 */
	public int LoginOnline(String companySName, String empID, String pwd,
			String pwdMD5) {
		int loginSucess = 1;
		try {
			SoapObject result;
			WebServiceAttribute wsAttribute = new WebServiceAttribute();
			wsAttribute.setNameSpace(context.getString(R.string.ws_nameSpace));
			wsAttribute.setServiceURL(getServiceIPAddress(context)
					+ context.getString(R.string.ws_path_Login));
			wsAttribute.setMethodName(context
					.getString(R.string.ws_method_Ws_Login));
			List<WebAttrKV> myList = new ArrayList<WebAttrKV>();
			WebAttrKV myKV1 = new WebAttrKV();
			myKV1.setKey("args0");
			myKV1.setValue(companySName);
			myList.add(myKV1);
			WebAttrKV myKV2 = new WebAttrKV();
			myKV2.setKey("args1");
			myKV2.setValue(empID);
			myList.add(myKV2);
			WebAttrKV myKV3 = new WebAttrKV();
			myKV3.setKey("args2");
			myKV3.setValue(pwdMD5);
			myList.add(myKV3);
			WebServiceConn conn = new WebServiceConn(context);
			result = (SoapObject) conn.getSoapObject(wsAttribute, myList,
					WebServiceConn.NORMAL);
			if (result == null) {
				return 1;
			}
			loginSucess = Integer.parseInt(result.getProperty("return").toString());

			SharedPreferences.Editor editor = settings.edit();
//			if (null != result.getProperty("EMP_NAME")
//					&& !"".equals(result.getProperty("EMP_NAME"))) {
//				editor.putString(ServerConfigPreference.EMP_NAME, result
//						.getProperty("EMP_NAME").toString());
//				loginSucess = 0;
//			} else {
//				return 1;
//			}
////			if (null != result.getProperty("userId")
////					&& !"".equals(result.getProperty("userId"))) {
//				editor.putString(ServerConfigPreference.USER_ID,empID);
////			}
//			if (null != result.getProperty("DEPT_CODE")
//					&& !"".equals(result.getProperty("DEPT_CODE"))) {
//				editor.putString(ServerConfigPreference.DEPT_CODE, result
//						.getProperty("DEPT_CODE").toString());
//			}
////			if (null != result.getProperty("userName")
////					&& !"".equals(result.getProperty("userName"))) {
//				editor.putString(ServerConfigPreference.USER_NAME, result
//						.getProperty("EMP_NAME").toString());
////			}
////			if (null != result.getProperty("sysTime")
////					&& !"".equals(result.getProperty("sysTime"))) {
////				editor.putString(ServerConfigPreference.SERVER_TIME, result
////						.getProperty("sysTime").toString());
////			}
//			if (null != result.getProperty("EMP_CODE")
//					&& !"".equals(result.getProperty("EMP_CODE"))) {
//				editor.putString(ServerConfigPreference.EMP_CODE, result
//						.getProperty("EMP_CODE").toString());
//			}
//
			editor.putLong(ServerConfigPreference.RUN_TIME,
					SystemClock.elapsedRealtime());
//
//			if (null != result.getProperty("ROLE_ID")
//					&& !"".equals(result.getProperty("ROLE_ID"))
//					&& !"anyType{}".equals(result.getProperty("ROLE_ID")
//							.toString())) {
//				System.out.println(result.getProperty("ROLE_ID"));
//				editor.putString(ServerConfigPreference.ROLE, result
//						.getProperty("ROLE_ID").toString());
//			} else {
//				editor.putString(ServerConfigPreference.ROLE, "");
//			}

			editor.putString(ServerConfigPreference.COMPANY_ACCOUNT,
					companySName);
			editor.putString(ServerConfigPreference.EmpID, empID);
			editor.putString(ServerConfigPreference.PASSWORD, pwd);
			editor.commit();
		} catch (NotFoundException e) {
			Log.e("在线登录验证1：", e);
			return 4;
		} catch (SoapFault e) {
			Log.e("在线登录验证2：", e);
			return 4;
		} catch (ConnectException e) {
			Log.e("在线登录验证3：", e);
			return 2;// 网络连接异常
		} catch (IOException e) {
			Log.e("在线登录验证4：", e);
			return 4;
		} catch (Exception e) {
			Log.e("在线登录验证5：", e);
			return 4;
		}
		return loginSucess;

	}

	/**
	 * 检查是否存在新的更新程序 有下载路径表示存在新的更新，否则为无更新。
	 * 
	 * @throws IOException
	 */
	public CheckApp getAppCheck(String senderNowVersion) {
		CheckApp checkApp = null;
		WebServiceAttribute wsAttribute = new WebServiceAttribute();
		wsAttribute.setNameSpace(context.getString(R.string.ws_nameSpace));
		wsAttribute.setServiceURL(getServiceIPAddress(context)
				+ context.getString(R.string.ws_downloadNewApk));
		wsAttribute.setMethodName(context
				.getString(R.string.ws_method_getAndroidVersion));
		List<WebAttrKV> myList = new ArrayList<WebAttrKV>();
		try {
			WebServiceConn conn = new WebServiceConn(context);
			Object result;
			result = conn.getSoapObject(wsAttribute, myList,
					WebServiceConn.NORMAL);
			conn.setTimeOut(30000);
			if (result != null && !result.toString().equals("anyType{}")) {
				SoapObject resultSoap = (SoapObject) result;
				Object newVersionObj = resultSoap.getProperty("return");
				String versionInfo = null;
				if (newVersionObj != null
						&& !newVersionObj.toString().equals("anyType{}")) {
					versionInfo = newVersionObj.toString();
					String[] versionInfos = versionInfo.split(",");// 版本，文件大小
					String newVersion = versionInfos[0];
					String fileSize = versionInfos[1];
					if (Double.parseDouble(newVersion) > Double
							.parseDouble(senderNowVersion)) {
						String retVal = getServiceIPAddress(context)
								+ context.getResources().getString(
										R.string.ws_UrlApk);
						checkApp = new CheckApp();
						checkApp.setStatus(true);
						checkApp.setAppVersion(newVersion);
						checkApp.setUrl(retVal);
						checkApp.setFileSize(fileSize);
					} else {
						checkApp = new CheckApp();
						checkApp.setStatus(false);
					}
				}
			}
		} catch (Exception ex) {
			Log.e("检查是否有更新：", ex);
			return null;
		}
		return checkApp;
	}

	public int getOrderNum() {
		// TODO Auto-generated method stub
		int num = 0;
		String sql1 = "SELECT count(*) FROM ORDER_PRESENT WHERE ACTIVE = '1' AND SYNC_FLAG != '0' ";
		String sql2 = "SELECT count(*) FROM ORDER_TABLE WHERE ACTIVE = '1' AND SYNC_FLAG != '0' ";
		DBManager dbManager = null;
		try {
			dbManager = new DBManager(context);
			Cursor cursor = dbManager.rawQuery(sql1, null);
			if (cursor.moveToNext()) {
				num = Integer.parseInt(cursor.getString(0) == null ? "0"
						: cursor.getString(0).toString());
			}
			Cursor cursor1 = dbManager.rawQuery(sql2, null);
			if (cursor1.moveToNext()) {
				num = num
						+ Integer.parseInt(cursor1.getString(0) == null ? "0"
								: cursor1.getString(0).toString());
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("获取订单数量：", e);
			return num;
		} finally {
			dbManager.close();
		}
		return num;
	}

	public int getForecastNum() {
		int num = 0;
		String sql = "SELECT count(*) FROM ORDER_FORECAST_PDA WHERE ACTIVE = '1' AND SYNC_FLAG != '0'";
		DBManager dbManager = null;
		try {
			dbManager = new DBManager(context);
			Cursor cursor = dbManager.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				num = Integer.parseInt(cursor.getString(0) == null ? "0"
						: cursor.getString(0).toString());
			}
		} catch (Exception e) {
			Log.e("getForecastNum：", e);
			return num;
		} finally {
			dbManager.close();
		}
		return num;
	}

	public int getVisitNum() {
		int num = 0;
		String sql = "SELECT count(*) FROM VISIT_TASK WHERE ACTIVE = '1' AND SYNC_FLAG != '0'";
		DBManager dbManager = null;
		try {
			dbManager = new DBManager(context);
			Cursor cursor = dbManager.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				num = Integer.parseInt(cursor.getString(0) == null ? "0"
						: cursor.getString(0).toString());
			}

		} catch (Exception e) {
			Log.e("getVisitNum：", e);
			return num;
		} finally {
			dbManager.close();
		}
		return num;
	}

	public boolean saveSyncInfo(SyncInfoEntity syncInfoEntity) {
		boolean flag = true;
		DBManager dbManager = new DBManager(context);
		SQLiteDatabase conn = dbManager.getConn(DbConnType.db_conn_type_file);
		String updateDT = GetDate.GenerateDate();
		try {
			ContentValues values = new ContentValues();
			values.put("SYNC_ID", syncInfoEntity.getSyncId());
			values.put("START_DT", syncInfoEntity.getStartDt().replace("-", "")
					.replace(" ", "").replace(":", ""));
			values.put("END_DT", syncInfoEntity.getEndDt().replace("-", "")
					.replace(" ", "").replace(":", ""));
			values.put("SYNC_VERSION", syncInfoEntity.getSyncVersion());
			values.put("SYNC_RESULT", syncInfoEntity.getSyncResult());
			values.put("ORDER_CNT", syncInfoEntity.getOrderCnt());
			values.put("FORECAST_CNT", syncInfoEntity.getForecastCnt());
			values.put("VISIT_CNT", syncInfoEntity.getVisitCnt());
			values.put("ERROR_MSG", syncInfoEntity.getErrorMsg());
			values.put("COMPANY_CODE",
					settings.getString(ServerConfigPreference.companyCode, ""));
			values.put("DEPT_CODE",
					settings.getString(ServerConfigPreference.DEPT_CODE, ""));
			values.put("EMP_CODE",
					settings.getString(ServerConfigPreference.EMP_CODE, ""));
			values.put("UPDATE_DT", updateDT);
			values.put("SYNC_FLAG", "1");
			conn.insert("SYNC_LOG_PDA", null, values);
		} catch (Exception ex) {
			flag = false;
			Log.e("saveSyncInfo：", ex);
		} finally {
			dbManager.close();
		}
		return flag;
	}

	/**
	 * 需要备份的文件
	 * 
	 * @return
	 */
	private HashMap<String, File> getBackupFiles() {
		HashMap<String, File> backupFiles = new HashMap<String, File>();

		// db
		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "zerowire"
				+ File.separator + "database";
		File dbFile = new File(dbPath, ConfigSync.database_current_name);
		FileUtils.createDir(dbFile.getParent());
		FileUtils.chmodFile(dbFile.getParentFile());
		backupFiles.put(dbFile.getName(), dbFile);

		// xml
		String spPath = context.getFilesDir().getParent() + File.separator
				+ "shared_prefs";
		FileUtils.createDir(spPath);
		FileUtils.chmodFile(spPath);
		String settingsXMLName = ServerConfigPreference.SETTING_INFO + ".xml";
		String syncXMLName = context.getPackageName() + ".syncCofig" + ".xml";
		backupFiles.put(settingsXMLName, new File(spPath, settingsXMLName));
		backupFiles.put(syncXMLName, new File(spPath, syncXMLName));

		return backupFiles;
	}

	/**
	 * @return true:备份成功，并返还备份成功后的文件名
	 */
	public ResultMsg backupData() {
		ResultMsg resultMsg = new ResultMsg();
		String fileName = context.getString(R.string.text_date_backup_name);
		String backupDir = FileUtils.getBackupDir();
		if (backupDir == null) {
			resultMsg.setMsg("没有找到SD卡，无法继续备份！");
			return resultMsg;
		}
		File destFile = new File(backupDir, fileName);
		if (destFile.exists()) {
			destFile.delete();
		}

		HashMap<String, File> backupFiles = getBackupFiles();
		if (backupFiles != null && !backupFiles.isEmpty()) {
			List<File> tempFiles = new ArrayList<File>();
			Set<Entry<String, File>> fileSet = backupFiles.entrySet();
			for (Entry<String, File> entry : fileSet) {
				File file = entry.getValue();
				if (file.exists()) {
					tempFiles.add(file);
				}
			}

			try {
				if (tempFiles.isEmpty()) {
					resultMsg.setFlag(false);
					resultMsg.setMsg("没有需要备份的数据！");
				} else {
					ZipUtils.compressFiles(tempFiles, destFile);
					resultMsg.setFlag(true);
					resultMsg.setMsg("数据已经备份到根目录，文件名为:\n" + destFile.getName());
				}
			} catch (Exception e) {
				Log.e("backupData：", e);
				resultMsg.setMsg("备份数据出错！");
				return resultMsg;
			}
		}
		return resultMsg;
	}

	/**
	 * @return true:还原成功
	 */
	public ResultMsg restoreData() {
		ResultMsg resultMsg = new ResultMsg();
		String cachePath = FileUtils.getCacheDir(context);
		FileUtils.deleteFile(new File(cachePath));
		cachePath = FileUtils.getCacheDir(context);

		String backupFileName = context
				.getString(R.string.text_date_backup_name);
		String backupDir = FileUtils.getBackupDir();

		File backupFile = new File(backupDir, backupFileName);
		if (backupFile.exists()) {
			HashMap<String, File> backupFiles = getBackupFiles();
			try {
				ZipUtils.decompress(backupFile, cachePath);// 解压文件
				File[] deFiles = FileUtils.getFileList(cachePath);// 得到文件，开始移动文件
				boolean restoreResult = false;
				if (deFiles != null && deFiles.length > 0) {
					for (File file : deFiles) {
						String fileName = file.getName();
						File destFile = backupFiles.get(fileName);
						if (destFile != null) {
							if (destFile.exists()) {
								FileUtils.chmodFile(destFile);
								destFile.delete();
							}
							FileUtils.chmodFile(file);
							restoreResult = FileUtils.copyFile(file, destFile);
							if (!restoreResult) {
								break;
							}
						}
					}
					resultMsg.setFlag(restoreResult);
					if (restoreResult) {
						resultMsg.setMsg("使用根目录下的\n" + backupFile.getName()
								+ "\n成功还原数据！");
					} else {
						resultMsg.setMsg("还原失败！");
					}
				} else {
					resultMsg.setMsg("文件损坏。");
				}
			} catch (Exception e) {
				Log.e("restoreData：", e);
				resultMsg.setMsg("文件损坏。");
				return resultMsg;
			} finally {
				FileUtils.deleteFile(new File(cachePath));
			}
		} else {
			// 不存在备份文件
			resultMsg.setMsg("没有找到备份文件！");
		}
		return resultMsg;
	}

	/**
	 * @param context
	 * @return 保存的服务器地址 如：http://10.211.26.21:8084
	 */
	public static String getServiceIPAddress(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				ServerConfigPreference.SETTING_INFO, Context.MODE_PRIVATE
						| Context.MODE_MULTI_PROCESS);
		StringBuilder url = new StringBuilder();
		String IP = context.getString(R.string.ws_service_IP_default);
		String port = IP.substring(IP.lastIndexOf(":") + 1, IP.length());
		IP = IP.substring(IP.lastIndexOf("/") + 1, IP.lastIndexOf(":"));

		IP = settings.getString(ServerConfigPreference.IP_ADDRESS, IP);
		port = settings.getString(ServerConfigPreference.ADDRESS_PORT, port);

		url.append("http://");
		url.append(IP);
		url.append(":");
		url.append(port);

		return url.toString();
	}

	/**
	 * @param context
	 * @param settings
	 * @return [用户名,密码]
	 */
	public static String[] getSaveUser(Context context,
			SharedPreferences settings) {
		String[] user = new String[2];
		String userName = null;
		String password = null;
		boolean cbu = settings.getBoolean(
				context.getString(R.string.sp_key_save_username_state), false);
		boolean cbp = settings.getBoolean(
				context.getString(R.string.sp_key_save_password_state), false);
		if (cbu) {
			userName = settings.getString(
					context.getString(R.string.sp_key_username), "");
		}

		if (cbp) {
			password = settings.getString(
					context.getString(R.string.sp_key_password), "");
		}
		user[0] = userName;
		user[1] = password;
		return user;
	}

	public static void saveUser(Context context, SharedPreferences settings,
			String userName, String password) {
		SharedPreferences.Editor editor = settings.edit();
		boolean cbu = settings.getBoolean(
				context.getString(R.string.sp_key_save_username_state), false);
		boolean cbp = settings.getBoolean(
				context.getString(R.string.sp_key_save_password_state), false);
		if (cbu) {
			editor.putString(context.getString(R.string.sp_key_username),
					userName);
			editor.putString(ServerConfigPreference.EmpID, userName);
		}
		if (cbp) {
			editor.putString(context.getString(R.string.sp_key_password),
					password);
			editor.putString(ServerConfigPreference.PASSWORD, password);
		}
		editor.commit();
	}

	public void deleteData14Before(String data) {
		// TODO Auto-generated method stub
		DBManager dbManager = new DBManager(context);
		SQLiteDatabase conn = dbManager.getConn(DbConnType.db_conn_type_file);
		try {
			conn.beginTransaction();
			conn.delete(
					"VISIT_TASK",
					"EMP_CODE =? and active=? and VISIT_S_DT < ?",
					new String[] {
							settings.getString(ServerConfigPreference.EMP_CODE,
									""), "1", data });
			conn.delete(
					"ORDER_TABLE",
					"EMP_CODE =? and active=? and CREATE_DT < ?",
					new String[] {
							settings.getString(ServerConfigPreference.EMP_CODE,
									""), "1", data });
			conn.delete(
					"ORDER_PRESENT",
					"EMP_CODE =? and active=? and CREATE_DT < ?",
					new String[] {
							settings.getString(ServerConfigPreference.EMP_CODE,
									""), "1", data });
			conn.delete(
					"ORDER_FORECAST_PDA",
					"EMP_CODE =? and active=? and CREATE_DT < ?",
					new String[] {
							settings.getString(ServerConfigPreference.EMP_CODE,
									""), "1", data });
			conn.setTransactionSuccessful();
		} catch (Exception e) {

			Log.e("deleteData14Before：", e);
		} finally {
			conn.endTransaction();
			dbManager.close();
		}
	}
}
