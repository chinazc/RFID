package com.cw.assetsrfid.inventory.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.cw.assetsrfid.assets.ui.AssetsDetailActivity;
import com.cw.assetsrfid.entity.AssetsInfoEntity;

public class InventoryManageActivity extends AbstractBaseActivity implements OnClickListener,OnItemClickListener{

	private Button btExecQuery,btClean;
	private TextView materialsType,warehouseNum,assetsCount;
	private ListView listViewQuery,listViewInventoryResult;
	private List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	private int searchCondition = 0;//搜索条件，用于搜索条件listView的显示
	//1代表 物料类别，2仓库号
	private int assetsTypeIndex,belongDeptIndex,currentStatusIndex,importanceIndex;
	private SimpleAdapter inventoryListAdapter,inventorySearchAdapter;
	private LinearLayout listContainer;
	private ImageView cancleImage;
	private EditText inventoryCode;
	private ArrayList<AssetsInfoEntity> assetsList;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_manage_main);
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
		titleName.setText("库存查询");
	}
	private void init() {
		assetsCount = (TextView)findViewById(R.id.textViewMessage);
		inventoryCode = (EditText)findViewById(R.id.inventoryInventoryCode);
		btExecQuery = (Button)findViewById(R.id.buttonInventoryExecQuery);
		btExecQuery.setOnClickListener(this);
		btClean = (Button)findViewById(R.id.buttonInventoryReset);
		btClean.setOnClickListener(this);
		materialsType = (TextView)findViewById(R.id.materialsType);
		materialsType.setOnClickListener(this);
		warehouseNum = (TextView)findViewById(R.id.warehouseNum);
		warehouseNum.setOnClickListener(this);
		listContainer = (LinearLayout)findViewById(R.id.listContainer);
		cancleImage = (ImageView)findViewById(R.id.cancleImage);
		cancleImage.setOnClickListener(this);
		listViewQuery = (ListView)findViewById(R.id.listViewQuery);
		listViewQuery.setOnItemClickListener(this);
		listViewInventoryResult = (ListView)findViewById(R.id.listViewInventoryresult);
		listViewInventoryResult.setOnItemClickListener(this);
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
		case R.id.buttonInventoryExecQuery://执行搜索
			String assetsCodeAtr = inventoryCode.getText().toString().trim();
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitInventoryDataTask().execute("");
			break;
		case R.id.buttonInventoryReset://重置
			
			break;
		case R.id.app_title_left://返回
			finish();
			break;
		case R.id.qrCode	://二维码
			
			break;	
		case R.id.materialsType	://物料类别
			searchCondition = 1;
			listContainer.setVisibility(View.VISIBLE);
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitInventorySearchDataTask().execute("正在加载物料类别列表...");
			break;	
		case R.id.warehouseNum	://仓库号
			searchCondition = 2;
			listContainer.setVisibility(View.VISIBLE);
			progressDialog = getProgressDialog();
			progressDialog.show();
			new InitInventorySearchDataTask().execute("正在加载仓库号列表...");
			break;	
		case R.id.cancleImage:
			listContainer.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
    private class InitInventoryDataTask extends AsyncTask<String, Void, ArrayList<AssetsInfoEntity>>{

		@Override
		protected ArrayList<AssetsInfoEntity> doInBackground(String... params) {
			// TODO Auto-generated method stub
			assetsList =new ArrayList<AssetsInfoEntity>();
			AssetsInfoEntity entity;
			for (int i = 0; i < 20; i++) {
				entity = new AssetsInfoEntity();
				entity.setAssetID(i+"");
				entity.setAssetName("锅炉"+i);
				entity.setEquipmentCategory("1号库"+i);
				entity.setBelongDepartment("3"+i);
				entity.setImportance("个"+i);
				entity.setStatus("8"+i);
				assetsList.add(entity);
			}
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			return assetsList;
		}

		@Override
		protected void onPostExecute(ArrayList<AssetsInfoEntity> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			listViewInventoryResult.setAdapter(new InventoryManageListAdapter(InventoryManageActivity.this,result));
			if (progressDialog!=null)
			   progressDialog.cancel();
			}
			
		}
    
    private class InitInventorySearchDataTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{

		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			HashMap<String, String> map = null;
			for (int i = 0; i < 10; i++) {
				map = new HashMap<String, String>();
				map.put("text","测试数据"+searchCondition+":"+i);
				map.put("id","测试数据"+searchCondition+":"+i);
				data.add(map);
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			inventorySearchAdapter = new SimpleAdapter(InventoryManageActivity.this,result,R.layout.assets_search_list_item,  
			        new String[]{"text"},new int[]{R.id.textView});
			listViewQuery.setAdapter(inventorySearchAdapter);
			if (progressDialog!=null)
				progressDialog.cancel();
			}
		}
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String string;
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.listViewQuery:
			switch (searchCondition) {
			case 1:
				string = data.get(arg2).get("text");
				materialsType.setText(string);
				break;
            case 2:
            	string = data.get(arg2).get("text");
            	warehouseNum.setText(string);
				break;
			default:
				break;
			}
			listContainer.setVisibility(View.GONE);
			break;
		case R.id.listViewInventoryresult:
			intent.setClass(InventoryManageActivity.this, AssetsDetailActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
