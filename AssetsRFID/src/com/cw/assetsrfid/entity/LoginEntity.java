package com.cw.assetsrfid.entity;

import com.cw.assetsrfid.common.Utils;
import com.cw.assetsrfid.login.logic.LoginManager.LoginResult;

/**
 * 登录输入的数据
 * @author lee
 *
 */
public class LoginEntity {
	private String userName;
	private String password;
	private boolean needInitData;
	private LoginResult result;
	
	public LoginResult getResult() {
		return result;
	}
	public void setResult(LoginResult result) {
		this.result = result;
	}
	/**
	 * @return <code>true</code>:做初始化同步
	 */
	public boolean isNeedInitData() {
		return needInitData;
	}
	public void setNeedInitData(boolean needInitData) {
		this.needInitData = needInitData;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMD5PassWord(){
		return Utils.getMD5Str(password);
	}
}
