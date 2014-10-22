package com.cw.assetsrfid.fault;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.common.DialogCommon;
import com.cw.assetsrfid.entity.FaultInfoEntity;

public class AssetsFaultManageActivity extends AbstractBaseActivity implements OnClickListener,OnItemClickListener{

    private GridView faultResultGridView;
    private Button execQuery,reset;
    private Dialog dialog;
    private Context context;
    private TextView faultCount;
    private ArrayList<FaultInfoEntity> faultList;
    private ImageView add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.assets_fault_manage);
		initTitle();
		init();
	}

	private void init() {
		execQuery = (Button)findViewById(R.id.buttonFaultExecQuery);
		execQuery.setOnClickListener(this);
		reset = (Button)findViewById(R.id.buttonReset);
		reset.setOnClickListener(this);
		faultResultGridView = (GridView)findViewById(R.id.assetsGridview);
		faultResultGridView.setOnItemClickListener(this);
		faultCount = (TextView)findViewById(R.id.textViewMessage);
		add = (ImageView)findViewById(R.id.addImage);
		add.setOnClickListener(this);
	}

	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		titleName.setText("故障管理");
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.app_title_left:
			finish();
			break;
		case R.id.buttonFaultExecQuery:
			Bundle args = new Bundle();
			args.putString("title", "");
			dialog = getDialog(DialogType.WaitDialog, args);
			new InitDataTask().execute("加载故障数据");
			break;
		case R.id.buttonReset:
			
			break;
		case R.id.addImage:
			showNewFaultDialog();
			break;
		case R.id.textViewCancel:
			addBuilder.cancel();
			break;
		case R.id.buttonNext:
			String assetsNum = assetsCode.getText().toString().trim();
			if(assetsNum.isEmpty())//未输入资产编号时提示错误，停留在当前对话框
			{
				DialogCommon dialog=DialogCommon.newinstants("资产编号", "请输入或扫描资产编号");
				ShowDialog(dialog);
				return;
			}
//			AssetsFaultManageActivity act=(AssetsFaultManageActivity) getActivity();
//			
//			Bundle bundle=new Bundle();
//			bundle.putInt("codetype", _codetype);
//			if(_codetype==GET_ASSETS_CODE_TYPE_ID)
//			{
//				//assetsID=Integer.parseInt(assetsNum);
//				assetsID = AssetIdEPCMap.GetAssetIdByAssetNumber(assetsNum);
//				
//				bundle.putInt("AssetsID", assetsID);
//			}
//			else {
//				bundle.putString("AssetsNum", assetsNum);
//			}
//			act.GetAssetsNumber("故障报告", true, bundle);
//			dismiss();
			break;
		case R.id.search:
			
			break;
		case R.id.imageViewErCode:
//			Intent intent=new Intent();
//			intent.setClass(this,ScanRfidOrBarcodeActivity.class);
//			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}
	
	public void ShowDialog(DialogFragment dialog){
	      String dialogTag="assetsnum";
	      FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	      DialogFragment prev=(DialogFragment) getFragmentManager().findFragmentByTag(dialogTag);
	      if(prev!=null)
	      {
	      	fragmentTransaction.remove(prev);
	      }
	      fragmentTransaction.add(dialog,dialogTag);  
	      fragmentTransaction.commit();
	  }
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String fmtString=getResources().getString(R.string.query_itemcount_message);
				faultCount.setText(String.format(fmtString, faultList.size()).toString());
				break;
			default:
				break;
			}
		};
	};
	
	private Dialog addBuilder;
	private EditText assetsCode;
	private void showNewFaultDialog() {
		TextView cancle;
		ImageView qrCode,search;
		Button nextButton;
		LayoutInflater mLI = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mRL = mLI.inflate(R.layout.view_layout_fault_report_assets_num, null);
		addBuilder = new Dialog(this, R.style.my_dialog_style);
		addBuilder.setContentView(mRL);
		addBuilder.setCanceledOnTouchOutside(false);// 设置可按返回键取消dialog
		cancle = (TextView) mRL.findViewById(R.id.textViewCancel);
		cancle.setOnClickListener(this);
		assetsCode = (EditText) mRL.findViewById(R.id.editTextAssetsCode);
		qrCode = (ImageView) mRL.findViewById(R.id.imageViewErCode);
		qrCode.setOnClickListener(this);
		search = (ImageView) mRL.findViewById(R.id.search);
		search.setOnClickListener(this);
		nextButton = (Button) mRL.findViewById(R.id.buttonNext);
		nextButton.setOnClickListener(this);
		addBuilder.show();
	}
	
	private class InitDataTask extends AsyncTask<String, Void, ArrayList<FaultInfoEntity>>{

		@Override
		protected ArrayList<FaultInfoEntity> doInBackground(String... params) {
			// TODO Auto-generated method stub
			faultList =new ArrayList<FaultInfoEntity>();
			FaultInfoEntity entity;
			for (int i = 0; i < 9; i++) {
				entity = new FaultInfoEntity();
				entity.setId(i);
				entity.setAssetsId(i*10);
				entity.setDepartmentId(i*100);
				entity.setDescription("说明");
				entity.setStatus(1);
				entity.setReportTime("20141013");
				entity.setPositions(i*1000+"");
				entity.setDepartmentId(1000*i);
				entity.setPriority(1);
				faultList.add(entity);
			}
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			return faultList;
		}

		@Override
		protected void onPostExecute(ArrayList<FaultInfoEntity> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			faultResultGridView.setAdapter(new FaultGridViewAdapter(context,result));
			dialog.cancel();
			}
			
		}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.assetsGridview:
			showDevMsg(""+position);
//			intent=new Intent();
//			intent.setClass(AssetsFaultManageActivity.this, FaultDetailShowActivity.class);
//			intent.putExtra("WorkRequestID", faultList.get(position).getId());
//			
//			startActivity(intent);
			
			
			break;

		default:
			break;
		}
	}

		
}
