package com.cw.assetsrfid.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class WebServiceCallResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WEBSERVICE_CALL_RESULT_INVALID=0;
	public static final int WEBSERVICE_CALL_RESULT_VALID=1;
	private int _status=WEBSERVICE_CALL_RESULT_INVALID;
	
	public static final int WEBSERVICE_CALL_SUCCESSFUL=0;
	
	//访问结果状态信息，_errorCode=0成功;
	private int _errorCode;
	private String _errorMessage;
	//private List<SoapObject> _resultList;

	//访问结果
	private int _resultType;
	public int GetResultType() {
		return _resultType;
	}
	
	private String _resultString;
	private List<String[]> _resultList;
	

	public void SetValueFailure(int errorCode,String errorMessage)
	{
		_status=WEBSERVICE_CALL_RESULT_VALID;
		_errorCode=errorCode;
		_errorMessage=errorMessage;
		_resultList=null;
	}
	
	public void SetValueSuccessful(String message,List<SoapObject> resultList) {
		_status=WEBSERVICE_CALL_RESULT_VALID;
		_errorCode=WEBSERVICE_CALL_SUCCESSFUL;
		
		_errorMessage=message;
		
		_resultType=WebServiceSvrInfo.RESULT_TYPE_DATASET;
		
		_resultList= new ArrayList<String[]>();
		for(int i=0;i<resultList.size();i++)
		{
			SoapObject soapObject=resultList.get(i);
			
			int itemCount=soapObject.getPropertyCount();
			String[] items=new String[itemCount];
			for(int j=0;j<itemCount;j++)
			{
				items[j]=soapObject.getProperty(j).toString();
			}
			
			_resultList.add(items);
		}
	}
	
	public void SetValueSuccessful(String message,String reusltString) {
		_status=WEBSERVICE_CALL_RESULT_VALID;
		_errorCode=WEBSERVICE_CALL_SUCCESSFUL;
		
		_errorMessage=message;
		
		_resultType=WebServiceSvrInfo.RESULT_TYPE_SINGLEVALUE;
		_resultString=reusltString;
		_resultList=new ArrayList<String[]>();
	}
	
	public boolean IsOK() {
		return _status!=WEBSERVICE_CALL_RESULT_INVALID;
	}
	
	public boolean IsSuccessful() {
		return _errorCode==WEBSERVICE_CALL_SUCCESSFUL;
	}
	
	public String GetResultString() {
		return _resultString;
	}
	
	public String GetErrorMessage() {
		return _errorMessage;
	}

	public List<String[]> GetResultList() {
		// TODO Auto-generated method stub
		return _resultList;
	}
}
