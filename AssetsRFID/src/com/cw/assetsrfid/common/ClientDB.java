///************************************************************************
// * �ļ�����	Android �ͻ������ݿ⣨���󣩻����ļ�
// * ���ܣ�	�������ݿ⹦��
// * ���ߣ�    	�ƶ���
// * ���ڣ�	2011-4-8
// * **********************************************************************
// */
//
//package com.cw.assetsrfid.common;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.widget.Toast;
//
///***
// * Android �ͻ������ݿ���
// * 
// * @author dingyou.tang
// * 
// */
//public class ClientDB {
//
//	public static Connection _conn = null;
//	public static ConfigFileAndroid _config;
//	protected static Context context;
//	protected SharedPreferences settings;
//
//	/**
//	 * ���´������ݿ��ʾ�ַ��� Argument to initialize for resetting database.
//	 */
//	protected ResultSet rs = null;
//
//	/***
//	 * ���캯��
//	 * 
//	 * @param context
//	 * @throws Exception
//	 */
//	public ClientDB(Context context) {
//		this.context = context;
//		settings = context.getSharedPreferences(
//				ServerConfigPreference.SETTING_INFO, 0);
//		try {
//			initialize(null);
//		} catch (ZwException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//
//	}
//
//	/***
//	 * ��ʼ������
//	 * 
//	 * @param args
//	 * @return
//	 * @throws ULjException
//	 */
//	public static boolean initialize(String[] args) throws ZwException {
//		if (null != _conn) {
//			// ���ݿ�����ʵ���Ѿ�����ʱ��ֱ�ӷ���
//			return true;
//		}
//		try {
//			if (_config == null) {
//
//				PackageInfo info;
//				try {
//					info = context.getPackageManager().getPackageInfo(
//							context.getPackageName(), 0);
//					_config = createConfig(Integer.toString(info.versionCode)
//							+ ".udb");
//					_config.setUserName(Integer.toString(info.versionCode));
//					_config.setPassword(Integer.toString(info.versionCode));
//					_config.setLazyLoadIndexes(true);
//				} catch (NameNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return false;
//				}
//
//			}
//			// �����ݿ�����
//			try {
//				SharedPreferences settings = context.getSharedPreferences(
//						ServerConfigPreference.SETTING_INFO, 0);
//				if (settings
//						.getBoolean(ServerConfigPreference.FIRST_SYNC, true)) {
//					_conn = CreateDatabase(_config);
//				} else {
//					_conn = ConnectDatabase(_config);
//				}
//			} catch (ULjException e) {
//				// ���ݿⲻ����ʱ�򴴽�
//				_conn = CreateDatabase(_config);
//			}
//		} catch (ULjException ex) {
//			// ���ݿ����
//			return false;
//		}
//		return true;
//
//	}
//
//	public static boolean Reset() {
//		_conn = null;
//		try {
//			initialize(null);
//
//		} catch (ZwException e) {
//			// TODO Auto-generated catch block
//			return false;
//		}
//		return true;
//	}
//
//	public boolean isExistDataBase() throws ULjException {
//
//		PackageInfo info = null;
//		boolean exist = false;
//		try {
//			info = this.context.getPackageManager().getPackageInfo(
//					this.context.getPackageName(), 0);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			exist = false;
//			return false;
//		}
//		String databaseName = _config.getDatabaseName();
//		if (!"".equals(databaseName) && null != databaseName) {
//			exist = true;
//		}
//		return exist;
//	}
//
//	/**
//	 * Create the configuration for the database.
//	 * 
//	 * @param db_name
//	 *            name of the databse
//	 * @return configuration object
//	 */
//	/*
//	 * abstract protected ConfigPersistent createConfig( String db_name ) throws
//	 * ULjException;
//	 */
//
//	protected static ConfigFileAndroid createConfig(String db_name)
//			throws ULjException {
//		return DatabaseManager.createConfigurationFileAndroid(db_name, context);
//	}
//
//	/**
//	 * �������ݿ�
//	 * 
//	 * @param _config
//	 * @return
//	 * @throws ULjException
//	 */
//	private static Connection CreateDatabase(ConfigPersistent _config)
//			throws ULjException {
//		Connection _con = DatabaseManager.createDatabase(_config);
//
//		SchemaCreator schema = new SchemaCreator(_con);
//		schema.createTables(context);
//		return _con;
//	}
//
//	/**
//	 * �������ݿ�
//	 * 
//	 * @param _config
//	 * @return
//	 * @throws ULjException
//	 */
//	private static Connection ConnectDatabase(ConfigPersistent _config)
//			throws ULjException {
//		Connection _con = DatabaseManager.connect(_config);
//		return _con;
//	}
//
//	/**
//	 * Execute a SQL statement.
//	 * 
//	 * @param do_commit
//	 *            �Ƿ񱣴�
//	 * @param sql_stmt
//	 *            SQL���
//	 * @param args
//	 *            ����
//	 */
//	public boolean executeStatement(boolean do_commit, String sql_stmt,
//			Object[] args) throws ULjException {
//		PreparedStatement ps = _conn.prepareStatement(sql_stmt);
//		if (null != args) {
//			for (int i = 0; i < args.length; i++) {
//				String value = null;
//				if (args[i] != null)
//					value = args[i].toString();
//				ps.set(i + 1, value);
//			}
//		}
//		try {
//			ps.execute();
//			if (do_commit) {
//				_conn.commit();
//			}
//		} finally {
//			ps.close();
//		}
//		return true;
//	}
//
//	/**
//	 * Prepared a SQL statement
//	 * 
//	 * @param sql_stmt
//	 *            SQL���
//	 * @param args
//	 *            ����
//	 * @return
//	 * @throws ULjException
//	 */
//	public PreparedStatement preparedStatement(String sql_stmt, Object[] args)
//			throws ULjException {
//		PreparedStatement preparedStatement = _conn.prepareStatement(sql_stmt);
//		if (args != null) {
//			for (int i = 0; i < args.length; i++) {
//				String value = null;
//				if (args[i] != null)
//					value = args[i].toString();
//				preparedStatement.set(i + 1, value);
//			}
//		}
//		return preparedStatement;
//	}
//
//	/**
//	 * �ر���Դ
//	 * 
//	 * @param ps
//	 * @param rs
//	 * @throws ZwException
//	 */
//	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
//
//		if (null != rs) {
//			try {
//				rs.close();
//				rs = null;
//			} catch (ULjException e) {
//				Toast.makeText(context, "���ݿ��쳣,�뷵������", 1000).show();
//				return;
//			}
//		}
//		if (null != ps)
//			try {
//				ps.close();
//				ps = null;
//			} catch (ULjException e) {
//				Toast.makeText(context, "���ݿ��쳣,�뷵������", 1000).show();
//				return;
//			}
//		if (null != conn) {
//			conn = null;
//		}
//	}
//
//	public static void dropDataBase(Context context) {
//
//		try {
//			File file = new File("/data/data/zerowire.SFA.Product/1.udb");
//			if (file.exists()) {
//				file.delete();
//			}
//			file = new File("/data/data/zerowire.SFA.Product/1.~db");
//			if (file.exists()) {
//				file.delete();
//			}
//			_conn.release();
//			_conn = null;
//			_config = null;
//			SharedPreferences.Editor editor = context.getSharedPreferences(
//					ServerConfigPreference.SETTING_INFO, 0).edit();
//			editor.putBoolean(ServerConfigPreference.FIRST_SYNC, true);
//			editor.commit();
//			new ClientDB(context);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//		}
//	}
//}
