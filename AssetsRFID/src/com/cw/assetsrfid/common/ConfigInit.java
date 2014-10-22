package com.cw.assetsrfid.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.cw.assetsrfid.entity.AssetCategoriesEntity;
import com.cw.assetsrfid.entity.DepartmentEntity;
import com.cw.assetsrfid.entity.LookupCodeEntity;

public class ConfigInit {

	public static ArrayList<AssetCategoriesEntity> AssetCategories_List = null;
	public static  ArrayList<DepartmentEntity> Department_List = null; 
	public static  HashMap<String, ArrayList<LookupCodeEntity>> LookupCode_Hash = null;
}
