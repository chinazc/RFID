package com.cw.assetsrfid.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.cw.assetsrfid.SFAAPPInfo;
import com.cw.assetsrfid.common.ServerConfigPreference;
import com.cw.assetsrfid.entity.AssetCategoriesEntity;
import com.cw.assetsrfid.entity.AssetsInfoEntity;
import com.cw.assetsrfid.entity.DepartmentEntity;
import com.cw.assetsrfid.entity.LookupCodeEntity;
import com.neil.myandroidtools.log.Log;
import com.zerowire.framework.db.DBManager;

public class ConfigInitManager {

	private Context context;
	private DBManager dbManager;
	protected SharedPreferences settings;
	public SFAAPPInfo appInfo;
	public ConfigInitManager(Context context) {
		this.context = context;
		this.settings = context.getSharedPreferences(
				ServerConfigPreference.SETTING_INFO, 0);
		this.appInfo = (SFAAPPInfo) context.getApplicationContext();
	}
	public ArrayList<AssetCategoriesEntity> getAssetCategories() {
		// TODO Auto-generated method stub
		this.dbManager = new DBManager(context);
		ArrayList<AssetCategoriesEntity> list = new ArrayList<AssetCategoriesEntity>();
		AssetCategoriesEntity entity = null;
		String sql = "SELECT Asset_Cate_ID,Asset_Cate_Code,Asset_Cate_Desc FROM Asset_Categories WHERE ACTIVE = '1'";
		try {
			Cursor cursor = dbManager.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				entity = new AssetCategoriesEntity();
				entity.setAssetCateID(cursor.getString(0) == null ? "" : cursor
						.getString(0).toString());
				entity.setAssetCateCode(cursor.getString(1) == null ? "" : cursor
						.getString(1).toString());
				entity.setAssetCateDesc(cursor.getString(2) == null ? "" : cursor
						.getString(2).toString());
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
	
	public HashMap<String, ArrayList<LookupCodeEntity>> getLookUpCode() {
		// TODO Auto-generated method stub
		this.dbManager = new DBManager(context);
		HashMap<String, ArrayList<LookupCodeEntity>> map = new HashMap<String,ArrayList<LookupCodeEntity>>();
		ArrayList<LookupCodeEntity> list = null;
		String lastLookupCodeCode = "",currentLookupCodeCode = "";
		
		LookupCodeEntity entity = null;
		String sql = "SELECT Lookup_Code_ID,Lookup_Code_Type,Lookup_Code_Code,Lookup_Code_Meaning," +
				"Lookup_Code_Tag,Lookup_Code_Description,Ineffective_Date FROM Lookup_Codes WHERE ACTIVE = '1'";
		try {
			Cursor cursor = dbManager.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				
				entity = new LookupCodeEntity();
				entity.setLookupCodeID(cursor.getString(0) == null ? "" : cursor
						.getString(0).toString());
				entity.setLookupCodeType(cursor.getString(1) == null ? "" : cursor
						.getString(1).toString());
				entity.setLookupCodeCode(cursor.getString(2) == null ? "" : cursor
						.getString(2).toString());
				entity.setLookupCodeMeaning(cursor.getString(3) == null ? "" : cursor
						.getString(3).toString());
				entity.setLookupCodeTag(cursor.getString(4) == null ? "" : cursor
						.getString(4).toString());
				entity.setLookupCodeDescription(cursor.getString(5) == null ? "" : cursor
						.getString(5).toString());
				entity.setLookupCodeInactiveDate(cursor.getString(6) == null ? "" : cursor
						.getString(6).toString());
				currentLookupCodeCode = cursor.getString(1) == null ? "" : cursor
						.getString(1).toString();
				if ("".equals(lastLookupCodeCode)) {
					lastLookupCodeCode = currentLookupCodeCode;	
					list = new ArrayList<LookupCodeEntity>();
					}
				if (lastLookupCodeCode.equals(currentLookupCodeCode)) {
					list.add(entity);
				}else {
					map.put(lastLookupCodeCode, list);
					list = new ArrayList<LookupCodeEntity>();
					list.add(entity);
					lastLookupCodeCode = currentLookupCodeCode;
				}
			}
			
		} catch (Exception e) {
			Log.e("getAssetsInfoList£º", e);
			return map;
		} finally {
			map.put(lastLookupCodeCode, list);
			dbManager.close();
		}
		return map;
	}
	public ArrayList<DepartmentEntity> getDepartments() {
		// TODO Auto-generated method stub
		this.dbManager = new DBManager(context);
		ArrayList<DepartmentEntity> list = new ArrayList<DepartmentEntity>();
		DepartmentEntity entity = null;
		String sql = "SELECT Department_ID,Organization_ID,Department_Code,Department_Name FROM Asset_Categories WHERE ACTIVE = '1'";
		try {
			Cursor cursor = dbManager.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				entity = new DepartmentEntity();
				entity.setDepartmentID(cursor.getString(0) == null ? "" : cursor
						.getString(0).toString());
				entity.setOrganizationID(cursor.getString(1) == null ? "" : cursor
						.getString(1).toString());
				entity.setDepartmentCode(cursor.getString(2) == null ? "" : cursor
						.getString(2).toString());
				entity.setDepartmentName(cursor.getString(3) == null ? "" : cursor
						.getString(3).toString());
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
