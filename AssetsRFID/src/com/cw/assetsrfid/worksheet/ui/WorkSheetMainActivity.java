package com.cw.assetsrfid.worksheet.ui;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.common.DialogCommon;
import com.cw.assetsrfid.entity.FaultInfoEntity;
import com.cw.assetsrfid.fault.FaultGridViewAdapter;

public class WorkSheetMainActivity extends AbstractBaseActivity implements OnClickListener,OnItemClickListener{

    private ListView workSheetListView;
    private Button execQuery,reset;
    private Dialog dialog;
    private Context context;
    private TextView workSheetCount;
    private ArrayList<FaultInfoEntity> workList;
    private ImageView add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_sheet_main);
		initTitle();
		init();
	}

	private void init() {
		execQuery = (Button)findViewById(R.id.buttonFaultExecQuery);
		execQuery.setOnClickListener(this);
		reset = (Button)findViewById(R.id.buttonReset);
		reset.setOnClickListener(this);
		workSheetListView = (ListView)findViewById(R.id.listViewWorkSheetList);
		workSheetListView.setOnItemClickListener(this);
		workSheetCount = (TextView)findViewById(R.id.textViewMessage);
		add = (ImageView)findViewById(R.id.addImage);
		add.setOnClickListener(this);
	}

	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		titleName.setText("工作单管理");
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
			new InitDataTask1().execute("加载工作单数据");
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
				workSheetCount.setText(String.format(fmtString, workList.size()).toString());
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
	
	private class InitDataTask1 extends AsyncTask<String, Void, ArrayList<FaultInfoEntity>>{

		@Override
		protected ArrayList<FaultInfoEntity> doInBackground(String... params) {
			// TODO Auto-generated method stub
			workList =new ArrayList<FaultInfoEntity>();
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
				workList.add(entity);
			}
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			return workList;
		}

		@Override
		protected void onPostExecute(final ArrayList<FaultInfoEntity> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			workSheetListView.setAdapter(new WorkSheetListViewAdapter(WorkSheetMainActivity.this,result) );
			if (dialog!=null) 
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
//			intent.setClass(WorkSheetMainActivity.this, FaultDetailShowActivity.class);
//			intent.putExtra("WorkRequestID", faultList.get(position).getId());
//			startActivity(intent);
			
			
			break;

		default:
			break;
		}
	}

		
}
