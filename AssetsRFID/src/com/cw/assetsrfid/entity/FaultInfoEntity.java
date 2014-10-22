package com.cw.assetsrfid.entity;

import java.io.Serializable;
import java.util.Date;

public class FaultInfoEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;			//π ’œid
	private int assetsId;
	private int departmentId;
	private int reportorId;
	private String description;		//π ’œ√Ë ˆ
	private String positions;
	private String appearances;
	private int priority;
	private String reportTime;			//π ’œ±®∏Ê ±º‰
	private int status;		//π ’œ◊¥Ã¨
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAssetsId() {
		return assetsId;
	}
	public void setAssetsId(int assetsId) {
		this.assetsId = assetsId;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getReportorId() {
		return reportorId;
	}
	public void setReportorId(int reportorId) {
		this.reportorId = reportorId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getAppearances() {
		return appearances;
	}
	public void setAppearances(String appearances) {
		this.appearances = appearances;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
