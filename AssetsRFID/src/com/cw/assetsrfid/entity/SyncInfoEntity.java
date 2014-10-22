package com.cw.assetsrfid.entity;

public class SyncInfoEntity implements java.io.Serializable{
	private static final long serialVersionUID = 9L;
	private String syncId;
	private String startDt;
	private String endDt;
	private String syncVersion;
	private String syncResult;
	private int orderCnt;
	private int forecastCnt;
	private int visitCnt;
	private String errorMsg;
	private String companyCode;
	private String deptCode;
	private String empCode;
	private String updateDt;
	
	
	 public int orderNum = 0;//�?��上传的订单条�?
	 public int orderLeft = 0;//剩余的没上传的订单条�?
	 public int forecastNum = 0;//�?��上传的订单条�?
	 public int forecastLeft = 0;//剩余的没上传的订单条�?
	 public int visitNum = 0;//�?��上传的订单条�?
	 public int visitLeft = 0;//剩余的没上传的订单条�?
	
	 public int getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(int orderNum) {
			this.orderNum = orderNum;
		}
		public int getOrderLeft() {
			return orderLeft;
		}
		public void setOrderLeft(int orderLeft) {
			this.orderLeft = orderLeft;
		}
		public int getForecastNum() {
			return forecastNum;
		}
		public void setForecastNum(int forecastNum) {
			this.forecastNum = forecastNum;
		}
		public int getForecastLeft() {
			return forecastLeft;
		}
		public void setForecastLeft(int forecastLeft) {
			this.forecastLeft = forecastLeft;
		}
		public int getVisitNum() {
			return visitNum;
		}
		public void setVisitNum(int visitNum) {
			this.visitNum = visitNum;
		}
		public int getVisitLeft() {
			return visitLeft;
		}
		public void setVisitLeft(int visitLeft) {
			this.visitLeft = visitLeft;
		}
	public String getSyncId() {
		return syncId;
	}
	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getSyncVersion() {
		return syncVersion;
	}
	public void setSyncVersion(String syncVersion) {
		this.syncVersion = syncVersion;
	}
	public String getSyncResult() {
		return syncResult;
	}
	public void setSyncResult(String syncResult) {
		this.syncResult = syncResult;
	}
	public int getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(int orderCnt) {
		this.orderCnt = orderCnt;
	}
	public int getForecastCnt() {
		return forecastCnt;
	}
	public void setForecastCnt(int forecastCnt) {
		this.forecastCnt = forecastCnt;
	}
	public int getVisitCnt() {
		return visitCnt;
	}
	public void setVisitCnt(int visitCnt) {
		this.visitCnt = visitCnt;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getUpdateDt() {
		return updateDt;
	}
	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}
	

}
