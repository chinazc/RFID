package com.cw.assetsrfid.webservice;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;

public class WebServiceSvrInfo implements Serializable {
	private HashMap<String, String> _hashMap;
	public WebServiceMethodArg _args;
	private boolean _canCall;
	
	public static final int RESULT_TYPE_DATASET=0;
	public static final int RESULT_TYPE_SINGLEVALUE=1;
	
	private int _resultType=RESULT_TYPE_DATASET;
	
	public int GetResultType() {
		return _resultType;
	}
	
	public static WebServiceSvrInfo NewLocationServer(Context context,int location_id) {
		AssetsRfidDB db=new AssetsRfidDB(context);
		WebServiceSvrInfo server=db.GetCurrentServer();
		WebServiceMethodArg args=new WebServiceMethodArg();
		args.AddArg("id", new Integer(location_id));
		
		server.SetMethod("BaseInfoManage.asmx", "GetFunctionLocation", args,WebServiceSvrInfo.RESULT_TYPE_DATASET);
		return server;
	}
	
	//RFID--资产信息查询webservice
	//autoRfid.asmx/GetRfidAssetsInfo
	public static WebServiceSvrInfo NewRfidAssetsServer(Context context,String rfidCode) {
		AssetsRfidDB db=new AssetsRfidDB(context);
		WebServiceSvrInfo server=db.GetCurrentServer();
		WebServiceMethodArg args=new WebServiceMethodArg();
		args.AddArg("rfidCode", rfidCode);
		
		server.SetMethod("autoRfid.asmx", "GetRfidAssetsInfo", args,WebServiceSvrInfo.RESULT_TYPE_DATASET);
		return server;
	}
	
	public WebServiceSvrInfo(String id,String serverName,String namespace,String serviceRootUrl)
	{
		_hashMap=new HashMap<String, String>();
		_canCall=false;
		
		_hashMap.put("ID", id);
		_hashMap.put("ServerName", serverName);
		_hashMap.put("namespace", namespace);
		_hashMap.put("serviceRootUrl", serviceRootUrl);
	}
	
	public int GetID() {
		return Integer.parseInt(_hashMap.get("ID"));
	}
	
	public String ServerName() {
		return _hashMap.get("ServerName");
	}
	
	public void SetMethod(String pageNameString,String methodName,WebServiceMethodArg args,int resultType) {
		_hashMap.put("pagename", pageNameString);
		_hashMap.put("methodName", methodName);
		_args=args;
		_canCall=true;
		_resultType=resultType;
	}
	
	public String NameSpace() {
		
		return _hashMap.get("namespace");
		
	}
	public String ServiceRootURL() {
		String valueString=_hashMap.get("serviceRootUrl");
		if(valueString.getBytes()[valueString.length()-1]!='/')
		{
			valueString+="/";	//在尾部添加'/'
		}
		
		return valueString;
		
	}
	
	public String MethodName() {
		
		return _hashMap.get("methodName");
		
	}
	
	public String PageName() {
		return _hashMap.get("pagename");
	}

	public static WebServiceSvrInfo NewAssetsSimpleInfo(Context context,int assetsID2) {
		// TODO Auto-generated method stub
		AssetsRfidDB db=new AssetsRfidDB(context);
		WebServiceSvrInfo server=db.GetCurrentServer();
		WebServiceMethodArg args=new WebServiceMethodArg();
		args.AddArg("id", new Integer(assetsID2));
		
		server.SetMethod("AssetsManage.asmx", "GetAssetsSimpleInfo", args,WebServiceSvrInfo.RESULT_TYPE_DATASET);
		return server;
	}
}
