package com.cw.assetsrfid.webservice;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class WebServiceCall {
	
	
	private final String TAG="WebService Query";
	
	private Context _context;
	private WebServiceHandler _handler;
	private WebServiceSvrInfo _server;
	private SoapObject _request=null;
	private SoapSerializationEnvelope envelope=null;
	private HttpTransportSE  transport=null;
	
	private Thread callThread;
	
	//public List<SoapObject> _resultList;
	public boolean _isOK=false;
	
	public WebServiceCall(Context context,WebServiceHandler handler)
	{
		_context=context;
		_handler=handler;
		
		_isOK=false;
	}
	
	public void Call(WebServiceSvrInfo server) {
		_server=server;
		_handler.SetResultType(_server.GetResultType());

		callThread=  new Thread(callServiceRunnable);
		callThread.start();
	}
	
	Runnable callServiceRunnable = new Runnable(){
	    @Override
	    public void run() {
	        //
	    	Log.v(TAG,"Begin query......");
	        Message msg = new Message();
	        Bundle data = new Bundle();
	        
	        //Create and Set SoapObject object
			_request=new SoapObject(_server.NameSpace(), _server.MethodName());
			if(_server._args!=null)
			{
				for(int i=0;i<_server._args.ArgCount();i++)
				{
					_request.addProperty(_server._args.GetArgName(i)
							,_server._args.GetArgValue(i));
					//Log.v("Webservice property",String.format("Property %d is %s", i,_request.getProperty(i).toString()));
				}
			}

			Log.v("Webservice",String.format("property count  is %d.", _request.getPropertyCount()));
			
			envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);          
			envelope.bodyOut=_request;
			envelope.dotNet=true;
			envelope.setOutputSoapObject(_request); 
			
			(new MarshalBase64()).register(envelope);
			
			//Android传输对象          
			transport=new HttpTransportSE(_server.ServiceRootURL()+_server.PageName()); 
			Log.v(TAG,_server.ServiceRootURL()+_server.PageName());
			Log.v(TAG,"方法:"+_server.MethodName());
			transport.debug=true; 
			Log.v(TAG,"Begin qyery......");
			
			try {
				transport.call(_server.NameSpace()+_server.MethodName(), envelope);
			} catch (HttpResponseException e1) {
				// TODO: handle exception
				e1.printStackTrace();
		        data.putString("value",e1.getMessage());
		        msg.what=6;
		        msg.setData(data);
		        _handler.sendMessage(msg);
				return;
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		        data.putString("value",e1.getMessage());
		        msg.what=1;
		        msg.setData(data);
		        _handler.sendMessage(msg);
				return;
			} catch (XmlPullParserException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		        data.putString("value",e1.getMessage());
		        msg.what=2;
		        msg.setData(data);
		        _handler.sendMessage(msg);
		        return;
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
		        data.putString("value",e.getMessage());
		        msg.what=3;
		        msg.setData(data);
		        _handler.sendMessage(msg);
		        return;
			}

			//process result
			//此处要注意的一点是，如果webservice的返回值是byte类型的，应该用
			//SoapObject result =(SoapObject)envelope.bodyIn;
			//getResponse(); 
			//如果是String类型的则用 Object result=envelope.bodyIn; 否则会报错！			
			try {
				if(envelope.getResponse()==null)
				{
					Log.v(TAG,"No response!");
			        data.putString("value","No response.");
			        msg.what=11;
			        msg.setData(data);
			        _handler.sendMessage(msg);
					return;
				}
				//返回结果
				//SoapObject result=(SoapObject) envelope.bodyIn;
				switch (_handler.GetResultType()) {
				case WebServiceSvrInfo.RESULT_TYPE_SINGLEVALUE:
				{
					SoapObject resultSoapObject=(SoapObject) envelope.bodyIn;
					_handler.SetResult(resultSoapObject.getProperty(0).toString());
					break;
				}
				case WebServiceSvrInfo.RESULT_TYPE_DATASET:
				{
					_handler.SetResult((SoapObject)envelope.getResponse());
					break;
				}
				}
				_isOK=true;
				
		        data.putString("value",envelope.bodyIn.toString());
		        Log.v(TAG,envelope.bodyIn.toString());
		        msg.what=0;
		        msg.setData(data);
		        _handler.sendMessage(msg);
		        return;
			} catch (SoapFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		        data.putString("value",e.faultstring);
		        msg.what=4;
		        msg.setData(data);
		        _handler.sendMessage(msg);
		        return;
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
		        data.putString("value",e.getMessage());
		        msg.what=5;
		        msg.setData(data);
		        _handler.sendMessage(msg);
		        return;
			}
	    }
	};
}
