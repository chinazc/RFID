package com.cw.assetsrfid.entity;

/**
 * 登录成功后，返还的登录用户信息
 * @author lee
 *
 */
public class LoginUserEntity {
	private String empID;
	private String empCode;
	private String empName;
	private String companyCode;
	private String dept_code;
	private String password;//超出控制范围，只能常用明码保存
	private boolean isLogin;//true：已经成功登录
	
	/**
	 * 有些界面没有登录也可以操作(如数据备份还原)，而且有可能需要之前的用户名<br>
	 * 所以可以是用此值判断是否已经成功登录
	 * @return true：已经成功登录
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
