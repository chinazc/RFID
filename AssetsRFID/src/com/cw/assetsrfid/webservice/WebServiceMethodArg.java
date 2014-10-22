package com.cw.assetsrfid.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebServiceMethodArg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<HashMap<String, Object>> _argList;//=new ArrayList<HashMap<String,Object>>();
	
	public WebServiceMethodArg()
	{
		_argList=new ArrayList<HashMap<String,Object>>();
	}
	
	public void AddArg(String argName,Object value)
	{
		HashMap<String, Object> arg=new HashMap<String, Object>();
		arg.put("argname", argName);
		arg.put("value", value);
		_argList.add(arg);
	}
	
	public int ArgCount()
	{
		return _argList.size();
	}
	
	public String GetArgName(int i)
	{
		return _argList.get(i).get("argname").toString();
	}
	
	public Object GetArgValue(int i) {
		return _argList.get(i).get("value");
	}
}
