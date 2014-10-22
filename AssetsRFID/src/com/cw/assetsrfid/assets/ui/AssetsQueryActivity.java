package com.cw.assetsrfid.assets.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.AbstractBaseActivity.DialogType;
import com.cw.assetsrfid.assets.logic.AssetsLogicManager;
import com.cw.assetsrfid.common.ConfigInit;
import com.cw.assetsrfid.entity.AssetCategoriesEntity;
import com.cw.assetsrfid.entity.AssetsInfoEntity;
import com.cw.assetsrfid.entity.DepartmentEntity;
import com.cw.assetsrfid.entity.LookupCodeEntity;

public class AssetsQueryActivity extends AbstractBaseActivity implements OnClickListener,OnItemClickListener{

	private Button btExecQuery,btClean;
	private TextView assetsType,belongDept,currentStatus,importance,assetsCount;
	private ListView listViewQuery,listViewQueryresult;
	private List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	private int searchCondition = 0;//搜索条件，用于搜索条件listView的显示
	//1代表 点击的资产类别，2所属部门，3当前状态，4重要性
	private String assetsTypeIndex,belongDeptIndex,currentStatusIndex,importanceIndex;
	private SimpleAdapter assetsListAdapter,assetsSearchAdapter;
	private LinearLayout listContainer;
	private ImageView cancleImage;
	private EditText assetsCode;
	private ArrayList<AssetsInfoEntity> assetsList;
	private ProgressDialog progressDialog;
	private AssetsLogicManager logic;
	private ArrayList<LookupCodeEntity> statusList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assets_query_main);
		initTitle();
		init();
	}
	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		btnRight.setVisibility(View.GONE);
		btnLeft.setText("返回");
		btnLeft.setOnClickListener(this);
		titleName.setText("资产查询");
	}
	private void init() {
		logic = new AssetsLogicManager(this);
		assetsCount = (TextView)findViewById(R.id.textViewMessage);
		assetsCode = (EditText)findViewById(R.id.assetsCode);
		btExecQuery = (Button)findViewById(R.id.buttonFaultExecQuery);
		btExecQuery.setOnClickListener(this);
		btClean = (Button)findViewById(R.id.buttonReset);
		btClean.setOnClickListener(this);
		assetsType = (TextView)findViewById(R.id.assetsType);
		assetsType.setOnClickListener(this);
		belongDept = (TextView)findViewById(R.id.belongDept);
		belongDept.setOnClickListener(this);
		currentStatus = (TextView)findViewById(R.id.currentStatus);
		currentStatus.setOnClickListener(this);
		importance = (TextView)findViewById(R.id.importance);
		importance.setOnClickListener(this);
		listContainer = (LinearLayout)findViewById(R.id.listContainer);
		cancleImage = (ImageView)findViewById(R.id.cancleImage);
		cancleImage.setOnClickListener(this);
		listViewQuery = (ListView)findViewById(R.id.listViewQuery);
		listViewQuery.setOnItemClickListener(this);
		listViewQueryresult = (ListView)findViewById(R.id.listViewQueryresult);
		listViewQueryresult.setOnItemClickListener(this);
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String fmtString=getResources().getString(R.string.query_itemcount_message);
				assetsCount.setText(String.format(fmtString, assetsList.size()).toString());
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonFaultExecQuery://执行搜索
		
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitAssetsDataTask().execute();
			break;
		case R.id.buttonReset://重置
			searchCondition = 0;
			
			
			break;
		case R.id.app_title_left://返回
			finish();
			break;
		case R.id.qrCode	://二维码
			
			break;	
		case R.id.assetsType	://资产类别
			searchCondition = 1;
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitSearchDataTask().execute("正在加载资产类别列表...");
			break;	
		case R.id.belongDept	://所属部门
			searchCondition = 2;
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitSearchDataTask().execute("正在加载所属部门列表...");
			break;	
		case R.id.currentStatus	://当前状态
			searchCondition = 3;
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitSearchDataTask().execute("正在加载当前状态列表...");
			break;	
		case R.id.importance	://重要性
			searchCondition = 4;
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitSearchDataTask().execute("正在加载重要性列表...");
			break;
		case R.id.cancleImage:
			listContainer.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

    private class InitAssetsDataTask extends AsyncTask<Void, Void, ArrayList<AssetsInfoEntity>>{

		@Override
		protected ArrayList<AssetsInfoEntity> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String assetsCodeAtr = assetsCode.getText().toString().trim();
			assetsList = logic.getAssetList(assetsCodeAtr,assetsTypeIndex,belongDeptIndex,currentStatusIndex,importanceIndex);
//			AssetsInfoEntity entity;
//			for (int i = 0; i < 20; i++) {
//				entity = new AssetsInfoEntity();
//				entity.setAssetID(i+"");
//				entity.setAssetName(i*10+"");
//				entity.setEquipmentCategory("说明");
//				entity.setBelongDepartment(i*100+"");
//				entity.setImportance("重要");
//				entity.setStatus("完好");
//				assetsList.add(entity);
//			}
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			return assetsList;
		}

		@Override
		protected void onPostExecute(ArrayList<AssetsInfoEntity> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			listViewQueryresult.setAdapter(new AssetsListAdapter(AssetsQueryActivity.this,result));
			if (progressDialog!=null)
			progressDialog.cancel();
			}
			
		}
    
    private class InitSearchDataTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			HashMap<String, String> map = null;
			data.clear();
			
			switch (searchCondition) {
			case 1:
				AssetCategoriesEntity entity = null;
				if (ConfigInit.AssetCategories_List!= null) {
					for (int i = 0; i < ConfigInit.AssetCategories_List.size(); i++) {
						entity = ConfigInit.AssetCategories_List.get(i);
						map = new HashMap<String, String>();
						map.put("text",entity.getAssetCateDesc());
						data.add(map);
					}
				}
				break;
			case 2:
				DepartmentEntity entity1 = null;
				if (ConfigInit.Department_List!= null) {
					for (int i = 0; i < ConfigInit.Department_List.size(); i++) {
						entity1 = ConfigInit.Department_List.get(i);
						map = new HashMap<String, String>();
						map.put("text",entity1.getDepartmentName());
						data.add(map);
					}
				}

				break;
			case 3:
				LookupCodeEntity entity2 = null;
				statusList = ConfigInit.LookupCode_Hash.get("CUX_MEAM_ASSET_STATUS");
					if (statusList!=null) {
						for (int i = 0; i < statusList.size(); i++) {
							entity2 = statusList.get(i);
							map = new HashMap<String, String>();
							map.put("text",entity2.getLookupCodeMeaning());
							data.add(map);
						}
					}
				break;
			case 4:
				LookupCodeEntity entity3 = null;
				statusList = ConfigInit.LookupCode_Hash.get("CUX_MEAM_ASSET_CRITICALITY");
					if (statusList!=null) {
						for (int i = 0; i < statusList.size(); i++) {
							entity3 = statusList.get(i);
							map = new HashMap<String, String>();
							map.put("text",entity3.getLookupCodeMeaning());
							data.add(map);
						}
					}
				break;

			default:
				break;
			}
			
			return data;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			assetsSearchAdapter = new SimpleAdapter(AssetsQueryActivity.this,result,R.layout.assets_search_list_item,  
			        new String[]{"text"},new int[]{R.id.textView});
			if (result.size()>0) {
				listContainer.setVisibility(View.VISIBLE);
			}
			listViewQuery.setAdapter(assetsSearchAdapter);
			if (progressDialog!=null)
				progressDialog.cancel();
			}
		}
    

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		String string;
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.listViewQuery:
			switch (searchCondition) {
			case 1:
				string = data.get(position).get("text");
				assetsType.setText(string);
				assetsTypeIndex = ConfigInit.AssetCategories_List.get(position).getAssetCateID();
				break;
            case 2:
            	string = data.get(position).get("text");
            	belongDept.setText(string);
            	belongDeptIndex = ConfigInit.Department_List.get(position).getDepartmentID();
				break;
            case 3:
            	string = data.get(position).get("text");
            	currentStatus.setText(string);
            	currentStatusIndex = statusList.get(position).getLookupCodeCode();
	            break;
            case 4:
            	string = data.get(position).get("text");
            	importance.setText(string);
            	importanceIndex = statusList.get(position).getLookupCodeCode();
	            break;
			default:
				break;
			}
			listContainer.setVisibility(View.GONE);
			break;
		case R.id.listViewQueryresult:
			
			intent.setClass(AssetsQueryActivity.this, AssetsDetailActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
