package com.cw.assetsrfid.entity;

/**
 * ��¼�ɹ��󣬷����ĵ�¼�û���Ϣ
 * @author lee
 *
 */
public class LoginUserEntity {
	private String empID;
	private String empCode;
	private String empName;
	private String companyCode;
	private String dept_code;
	private String password;//�������Ʒ�Χ��ֻ�ܳ������뱣��
	private boolean isLogin;//true���Ѿ��ɹ���¼
	
	/**
	 * ��Щ����û�е�¼Ҳ���Բ���(�����ݱ��ݻ�ԭ)�������п�����Ҫ֮ǰ���û���<br>
	 * ���Կ������ô�ֵ�ж��Ƿ��Ѿ��ɹ���¼
	 * @return true���Ѿ��ɹ���¼
	 */
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
}
