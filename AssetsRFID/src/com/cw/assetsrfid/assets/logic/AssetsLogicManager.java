package com.cw.assetsrfid.assets.logic;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import com.cw.assetsrfid.SFAAPPInfo;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.entity.AssetsInfoEntity;
import com.neil.myandroidtools.log.Log;
import com.zerowire.framework.db.DBManager;

public class AssetsLogicManager {
	private Context context;
	private DBManager dbManager;
	protected SharedPreferences settings;
	public SFAAPPInfo appInfo;
	public AssetsLogicManager(Context context) {
		this.context = context;
		this.settings = context.getSharedPreferences(
				ServerConfigPreference.SETTING_INFO, 0);
		this.appInfo = (SFAAPPInfo) context.getApplicationContext();
	}
	public ArrayList<AssetsInfoEntity> getOrderType() {
		// TODO Auto-generated method stub
		this.dbManager = new DBManager(context);
		ArrayList<AssetsInfoEntity> list = new ArrayList<AssetsInfoEntity>();
		AssetsInfoEntity entity = null;
		String sql = "SELECT sys_key2,sys_value FROM SYS_PARM WHERE sys_key1 = '017'";
		try {
			Cursor cursor = dbManager.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				entity = new AssetsInfoEntity();
				entity.setAssetID(cursor.getString(0) == null ? "" : cursor
						.getString(0).toString());
				entity.setAssetName(cursor.getString(1) == null ? "" : cursor
						.getString(1).toString());
				list.add(entity);
			}
		} catch (Exception e) {
			Log.e("getAssetsInfoList£º", e);
			return list;
		} finally {
			dbManager.close();
		}
		return list;
	}
	public ArrayList<AssetsInfoEntity> getAssetList(String assetCode,String assetCategory,String belongDept
			,String currentStatus,String importance) {
		ArrayList<AssetsInfoEntity> list =new ArrayList<AssetsInfoEntity>();
		this.dbManager = new DBManager(context);
		AssetsInfoEntity entity = null;
		String sql = "SELECT sys_key2,sys_value FROM SYS_PARM WHERE sys_key1 = '017'";
		try {
			Cursor cursor = dbManager.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				entity = new AssetsInfoEntity();
				entity.setAssetID(cursor.getString(0) == null ? "" : cursor
						.getString(0).toString());
				entity.setAssetName(cursor.getString(1) == null ? "" : cursor
						.getString(1).toString());
				list.add(entity);
			}
		} catch (Exception e) {
			Log.e("getAssetsInfoList£º", e);
			return list;
		} finally {
			dbManager.close();
		}
		return list;
	}
	
	 
}
