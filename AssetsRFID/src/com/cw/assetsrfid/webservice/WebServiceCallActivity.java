package com.cw.assetsrfid.webservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cw.assetsrfid.R;

public class WebServiceCallActivity extends Activity {
	
	private TextView titleTextView;
	private TextView messageTextView;
	private TextView errorMessageTextView;
	
	private ProgressBar callProgressBar;
	
	private int _autoCall;
	
	public static int WEBSERVICECALL_STATUS_UNCALL=0;
	public static int WEBSERVICECALL_STATUS_CALLING=1;
	public static int WEBSERVICECALL_STATUS_SUCCESSFAL=2;
	public static int WEBSERVICECALL_STATUS_FAILURE=3;
	
	private int _callStatus=WEBSERVICECALL_STATUS_UNCALL;
	
//	private web
//	
//	public static WebServiceCallActivity newInstance() {
//		WebServiceCallActivity webcallActivity=new WebServiceCallActivity();
//		
//		return webcallActivity;
//	}
	
	private WebServiceSvrInfo _serInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_web_service_call);

		InitControls();

		//获取访问的信息
		try {
			Bundle bundle=getIntent().getExtras();
			titleTextView.setText(bundle.getString("title"));
			messageTextView.setText(bundle.getString("message"));
			_autoCall=bundle.getInt("autocall");

			_serInfo=(WebServiceSvrInfo) bundle.getSerializable("WebCallServer");
			
			if(_autoCall>0)
			{
				OnWebServiceCall();
			}
		} catch (Exception e) {
			// TODO: handle exception
			ShowErrorMessage(1, e.getMessage());
		}
	}

	private void OnWebServiceCall() {
		// TODO Auto-generated method stub
		callProgressBar.setVisibility(View.VISIBLE);
		WebServiceCall webCaller=new WebServiceCall(this, handler);
		webCaller.Call(_serInfo);
	}

	private void InitControls() {
		// TODO Auto-generated method stub
		titleTextView=(TextView) findViewById(R.id.textViewTitle);
		messageTextView=(TextView) findViewById(R.id.textViewMessage);
		errorMessageTextView=(TextView) findViewById(R.id.textViewErrorMessage);
		
		callProgressBar=(ProgressBar) findViewById(R.id.progressBar1);
		
		InitButtons();
	}

	private void InitButtons() {
		// TODO Auto-generated method stub
		Button okButton=(Button) findViewById(R.id.buttonOK);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnReturnResult();
			}
		});
		
		Button cancelButton=(Button) findViewById(R.id.buttonCancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnCancelReturnResult();
			}
		});
	}

	protected void OnCancelReturnResult() {
		// TODO Auto-generated method stub
		setResult(RESULT_CANCELED);
		finish();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.web_service_call, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private WebServiceHandler handler= new WebServiceHandler()
	{
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        
	        callProgressBar.setVisibility(View.INVISIBLE);
	        
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        
	        switch (msg.what) {
			case 0://Login Service access sucessful.
			{
				_callStatus=WEBSERVICECALL_STATUS_SUCCESSFAL;
				break;
			}
			case 10://设置进度图标
			{
				//ProgressBar proBar=(ProgressBar)findView
				break;
			}
			default://Fail
			{
				ShowErrorMessage(1,Integer.toString(msg.what)+":"+val);
				break;
			}
	        }
			if(_autoCall>0)
			{
				OnReturnResult();
			}
	    }

	};

	private void ShowErrorMessage(int messageType, String messageText) {
		// TODO Auto-generated method stub
		switch (messageType) {
		case 0://正常
		{
			int color=getResources().getColor(R.color.black);
			errorMessageTextView.setTextColor(color);
			errorMessageTextView.setText(messageText);
			break;
		}
		case 1://错误
		{
			int color=getResources().getColor(R.color.red);
			errorMessageTextView.setTextColor(color);
			errorMessageTextView.setText(messageText);
			break;
		}
		default:
		{
			int color=getResources().getColor(R.color.green);
			errorMessageTextView.setTextColor(color);
			errorMessageTextView.setText(messageText);
			break;
		}
		}
	}

	protected void OnReturnResult() {
		// TODO Auto-generated method stub
		Log.v("Web service call","Begin return result...");
		if(_callStatus==WEBSERVICECALL_STATUS_SUCCESSFAL)
		{
			Log.v("Web service call","Begin return result...");
			Bundle bundle=new Bundle();
			bundle.putInt("havevalue", 1);
			WebServiceCallResult result=handler.GetCallResult();
			bundle.putSerializable("WebcallResult", result);
			
			Intent intent=new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
		}
		finish();
	}
}
