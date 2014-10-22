package com.cw.assetsrfid.webservice;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.os.Handler;
import android.util.Log;

public class WebServiceHandler extends Handler {
	private SoapObject _result;
	
	protected int _resultType=WebServiceSvrInfo.RESULT_TYPE_DATASET;
	public void SetResultType(int resultType) {
		_resultType=resultType;
	}
	public int GetResultType() {
		return _resultType;
	}
	
	private List<SoapObject> _resultList;
	public List<SoapObject> GetResultList() {
		return _resultList;
	}
		
	private String _resultString;
	public String GetResultString() {
		return _resultString;
	}
	
	//private WebServiceCallResult _resultWebServiceCallResult=null;
	
	public boolean SetResult(SoapObject soapObject) {
		_result=soapObject;
		return  ParseResult();
	}
	
	public boolean SetResult(String resultString) {
		_resultString=resultString;
		_resultType=WebServiceSvrInfo.RESULT_TYPE_SINGLEVALUE;
		return true;
	}
	
	public boolean ParseResult()
	{
		if(_result==null)
		{
			return false;
		}
		if(_resultType==WebServiceSvrInfo.RESULT_TYPE_SINGLEVALUE)
		{
			_resultString=_result.toString();
			return true;
		}

		SoapObject result =_result; 
		result = (SoapObject)result.getProperty(1); 
		result = (SoapObject)result.getProperty(0);
		
		_resultList=new ArrayList<SoapObject>();
		for(int i=0;i<result.getPropertyCount();i++)
		{
			Log.v("SoapObject Parse", ((SoapObject) result.getProperty(i)).toString());
			_resultList.add(  (SoapObject) result.getProperty(i));
		}
		
		return true;
	}
	
	public WebServiceCallResult GetCallResult() {
		WebServiceCallResult result=new WebServiceCallResult();
		switch (_resultType) {
		case WebServiceSvrInfo.RESULT_TYPE_SINGLEVALUE:
		{
			result.SetValueSuccessful("", _resultString);
			break;
		}
		case WebServiceSvrInfo.RESULT_TYPE_DATASET:
		{
			result.SetValueSuccessful("", _resultList);
			break;
		}
		}
		return result;
	}
}
