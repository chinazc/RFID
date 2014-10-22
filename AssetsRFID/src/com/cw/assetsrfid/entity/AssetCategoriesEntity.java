package com.cw.assetsrfid.entity;

import java.io.Serializable;

public class AssetCategoriesEntity implements Serializable{

	private static final long serialVersionUID = 4L;
	private String assetCateID;	
	private String assetCateCode;	
	private String assetCateDesc;
	public String getAssetCateID() {
		return assetCateID;
	}
	public void setAssetCateID(String assetCateID) {
		this.assetCateID = assetCateID;
	}
	public String getAssetCateCode() {
		return assetCateCode;
	}
	public void setAssetCateCode(String assetCateCode) {
		this.assetCateCode = assetCateCode;
	}
	public String getAssetCateDesc() {
		return assetCateDesc;
	}
	public void setAssetCateDesc(String assetCateDesc) {
		this.assetCateDesc = assetCateDesc;
	}
	
}
