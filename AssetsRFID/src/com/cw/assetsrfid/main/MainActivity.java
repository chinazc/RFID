package com.cw.assetsrfid.main;

import java.io.Serializable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.assets.ui.AssetsQueryActivity;
import com.cw.assetsrfid.cardreder.ui.CardReaderSettingActivity;
import com.cw.assetsrfid.common.ConfigInit;
import com.cw.assetsrfid.fault.AssetsFaultManageActivity;
import com.cw.assetsrfid.inventory.ui.InventoryManageActivity;
import com.cw.assetsrfid.patrol.ui.PatrolInspectionActivity;
import com.cw.assetsrfid.worksheet.ui.WorkSheetMainActivity;

public class MainActivity extends AbstractBaseActivity implements OnClickListener{

	private final String TAG="应用登录主界面";
	private ServiceConnection conn;
	private ProgressDialog progressDialog;
	private ConfigInitManager logic;
	private LinearLayout assetsQuery,faultManage,workSheet,preventiveMaintenance,inventoryManage,setting,exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTitle();
		init();
		progressDialog = getProgressDialog();
		progressDialog.show();
		new InitAppStaticDataTask().execute(progressDialog);
	}

	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
	}
	private void init() {
		logic = new ConfigInitManager(this);
		assetsQuery = (LinearLayout)findViewById(R.id.assetsQuery);
		assetsQuery.setOnClickListener(this);
		faultManage = (LinearLayout) findViewById(R.id.faultManage);
		faultManage.setOnClickListener(this);
		workSheet = (LinearLayout)findViewById(R.id.workSheet);
		workSheet.setOnClickListener(this);
		preventiveMaintenance = (LinearLayout)findViewById(R.id.preventiveMaintenance);
		preventiveMaintenance.setOnClickListener(this);
		inventoryManage = (LinearLayout)findViewById(R.id.inventoryManage);
		inventoryManage.setOnClickListener(this);
		setting = (LinearLayout)findViewById(R.id.setting);
		setting.setOnClickListener(this);
		exit = (LinearLayout)findViewById(R.id.exit);
		exit.setOnClickListener(this);
	}

	
//	//启动Rfid服务
//	private void StartRfidService() {
//        Intent intent = new Intent(MainActivity.this,RFIDService.class);  
//        Log.i(TAG, "bindService()");  
//        bindService(intent, conn, Context.BIND_AUTO_CREATE);  
//	}

	public class InitAppStaticDataTask extends AsyncTask<ProgressDialog, Void, ProgressDialog>{
		@Override
		protected ProgressDialog doInBackground(ProgressDialog... params) {

			ConfigInit.AssetCategories_List = logic.getAssetCategories();
			ConfigInit.LookupCode_Hash = logic.getLookUpCode();
			ConfigInit.Department_List = logic.getDepartments();
			return params[0];
		}

		@Override
		protected void onPostExecute(ProgressDialog dialog2) {
			// TODO Auto-generated method stub
			super.onPostExecute(dialog2);
			if (dialog2 != null) dialog2.cancel();
		}


	}

	
    @Override
    protected void onResume() {
     super.onResume();
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.assetsQuery:
			intent.setClass(MainActivity.this, AssetsQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.faultManage:
			intent.setClass(MainActivity.this, AssetsFaultManageActivity.class);
			startActivity(intent);
			break;
		case R.id.workSheet:
			intent.setClass(MainActivity.this, WorkSheetMainActivity.class);
			startActivity(intent);
			break;
		case R.id.preventiveMaintenance:
			intent.setClass(MainActivity.this, PatrolInspectionActivity.class);
			startActivity(intent);
			break;
		case R.id.inventoryManage:
			intent.setClass(MainActivity.this, InventoryManageActivity.class);
			startActivity(intent);
			
			break;
		case R.id.imageViewPropsUniform:
			
			break;
		case R.id.imageViewApprovalList:
			
			break;
		case R.id.setting:
	    	intent.setClass(MainActivity.this, CardReaderSettingActivity.class);
	    	startActivity(intent);
			break;
		case R.id.exit:
			exitApp();
			break;
		default:
			break;
		}
	}
}
