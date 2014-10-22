package com.cw.assetsrfid.entity;

import java.io.Serializable;

public class LookupCodeEntity implements Serializable{

	private static final long serialVersionUID = 2L;
	private String active;
	private String lookupCodeID;	
	private String lookupCodeType;	
	private String lookupCodeCode;
	private String lookupCodeMeaning;
	private String lookupCodeTag;
	private String lookupCodeDescription;
	private String lookupCodeInactiveDate;
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getLookupCodeID() {
		return lookupCodeID;
	}
	public void setLookupCodeID(String lookupCodeID) {
		this.lookupCodeID = lookupCodeID;
	}
	public String getLookupCodeType() {
		return lookupCodeType;
	}
	public void setLookupCodeType(String lookupCodeType) {
		this.lookupCodeType = lookupCodeType;
	}
	public String getLookupCodeCode() {
		return lookupCodeCode;
	}
	public void setLookupCodeCode(String lookupCodeCode) {
		this.lookupCodeCode = lookupCodeCode;
	}
	public String getLookupCodeMeaning() {
		return lookupCodeMeaning;
	}
	public void setLookupCodeMeaning(String lookupCodeMeaning) {
		this.lookupCodeMeaning = lookupCodeMeaning;
	}
	public String getLookupCodeTag() {
		return lookupCodeTag;
	}
	public void setLookupCodeTag(String lookupCodeTag) {
		this.lookupCodeTag = lookupCodeTag;
	}
	public String getLookupCodeDescription() {
		return lookupCodeDescription;
	}
	public void setLookupCodeDescription(String lookupCodeDescription) {
		this.lookupCodeDescription = lookupCodeDescription;
	}
	public String getLookupCodeInactiveDate() {
		return lookupCodeInactiveDate;
	}
	public void setLookupCodeInactiveDate(String lookupCodeInactiveDate) {
		this.lookupCodeInactiveDate = lookupCodeInactiveDate;
	}
	
	
}
