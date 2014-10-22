package com.cw.assetsrfid.entity;

import java.io.Serializable;

public class CheckApp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean status;
	private String url;
	private String appWhatsnew;
	private String appVersion;
	private long fileSize;
	
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		long fs = 0l;
		if (fileSize != null && !fileSize.trim().equals("")) {
			fs = Long.parseLong(fileSize);
		}
		this.fileSize = fs;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getAppWhatsnew() {
		return appWhatsnew;
	}
	public void setAppWhatsnew(String appWhatsnew) {
		this.appWhatsnew = appWhatsnew;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
