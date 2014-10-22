package com.cw.assetsrfid.assets.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;

public class AssetsDetailActivity extends AbstractBaseActivity implements OnClickListener,OnCheckedChangeListener{

//	private ScrollView containerView;
	private LinearLayout containerView;
	private RadioButton assetsBaseInfo,beijianList,equipmentLog,standard,plan,document;
	private LayoutInflater inflater;
	private View subView;
	private RadioGroup radioGroup;
	private TextView textViewAssetNo,textViewAssetsName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assets_detail_pad);
		initTitle();
		init();
	}



	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		titleOther = (TextView) findViewById(R.id.app_title_other);
		btnRight.setText("菜单");
		btnRight.setTag("1");
		btnRight.setVisibility(View.GONE);
		btnLeft.setText("返回");
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		titleName.setText("资产明细");
	}
	
	private void init() {
		containerView = (LinearLayout)findViewById(R.id.linearDetailInfo);
		radioGroup = (RadioGroup)findViewById(R.id.linearLayoutButton);
		radioGroup.setOnCheckedChangeListener(this);
		textViewAssetNo = (TextView)findViewById(R.id.textViewAssetNo);
		textViewAssetsName = (TextView)findViewById(R.id.textViewAssetsName);
		
		
		inflater=getLayoutInflater();
		containerView.removeAllViews();
        subView=inflater.inflate(R.layout.activity_assets_detail_first_pad, null);
        containerView.addView(subView);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.app_title_left:
			finish();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.buttonAssetsBaseinfo:
			containerView.removeAllViews();
            subView=inflater.inflate(R.layout.activity_assets_detail_first_pad, null);
            containerView.addView(subView);
            initFirstTab();
            break;
		case R.id.buttonBeijianList:
			containerView.removeAllViews();
            subView=inflater.inflate(R.layout.activity_assets_detail_second_pad, null);
            containerView.addView(subView);
            initSecondTab();
			break;
		case R.id.buttonEquipmentLog:
			containerView.removeAllViews();
            subView=inflater.inflate(R.layout.activity_assets_detail_three_pad, null);
            containerView.addView(subView);
            initThreeTab();
			break;
		case R.id.buttonStandard:
			containerView.removeAllViews();
            subView=inflater.inflate(R.layout.activity_assets_detail_four_pad, null);
            containerView.addView(subView);
            initFourTab();
			break;
		case R.id.buttonPlan:
			containerView.removeAllViews();
            subView=inflater.inflate(R.layout.activity_assets_detail_five_pad, null);
            containerView.addView(subView);
            initFiveTab();
			break;
		case R.id.buttonDocument:
			containerView.removeAllViews();
            subView=inflater.inflate(R.layout.activity_assets_detail_six_pad, null);
            containerView.addView(subView);
            initSixTab();
			break;
			

		default:
			break;
		}
	}



	







	private void initFirstTab() {
		// TODO Auto-generated method stub
		ImageView firstTabImageView = (ImageView)subView.findViewById(R.id.imageViewAssetsImage);
		TextView deviceType = (TextView)subView.findViewById(R.id.deviceType);
		TextView importance = (TextView)subView.findViewById(R.id.importance);
		TextView belongDept = (TextView)subView.findViewById(R.id.belongDept);
		TextView detailType = (TextView)subView.findViewById(R.id.detailType);
		TextView type = (TextView)subView.findViewById(R.id.type);
		TextView manuFacturer = (TextView)subView.findViewById(R.id.manuFacturer);
		TextView currentStatus = (TextView)subView.findViewById(R.id.currentStatus);
		TextView assetsSource = (TextView)subView.findViewById(R.id.assetsSource);
		TextView startTime = (TextView)subView.findViewById(R.id.startTime);
		TextView assetsOriValue = (TextView)subView.findViewById(R.id.assetsOriValue);
		TextView assetsNetValue = (TextView)subView.findViewById(R.id.assetsNetValue);
		TextView fixedAssets = (TextView)subView.findViewById(R.id.fixedAssets);
		TextView lightSource = (TextView)subView.findViewById(R.id.lightSource);
		TextView psu = (TextView)subView.findViewById(R.id.psu);
		TextView opticalLens = (TextView)subView.findViewById(R.id.opticalLens);
		TextView controlPlan = (TextView)subView.findViewById(R.id.controlPlan);
		deviceType.setText("aaaa");
		firstTabImageView.setImageResource(R.drawable.add);
	}
	
	private void initSecondTab() {
		ListView listViewAssetDeviceList = (ListView)subView.findViewById(R.id.listViewAssetDeviceList);
		SimpleAdapter secondTabListAdapter = new SimpleAdapter(this,getSecondData(),R.layout.activity_assets_detail_second_pad_list_item,  
		        new String[]{"materialsCode","materialsDescription","unit","Inventory"},
		        new int[]{R.id.materialsCode,R.id.materialsDescription,R.id.unit,R.id.Inventory});
		listViewAssetDeviceList.setAdapter(secondTabListAdapter);
	}
	private void initThreeTab() {
		ListView listViewAssetLogList = (ListView)subView.findViewById(R.id.listViewAssetLogList);
		SimpleAdapter threeTabListAdapter = new SimpleAdapter(this,getThreeData(),R.layout.activity_assets_detail_three_pad_list_item,  
		        new String[]{"event","eventDescription","dateTime"},
		        new int[]{R.id.event,R.id.eventDescription,R.id.dateTime});
		listViewAssetLogList.setAdapter(threeTabListAdapter);
	}
	private void initFourTab() {
		ListView listViewStandardWorkList = (ListView)subView.findViewById(R.id.listViewStandardWorkList);
		SimpleAdapter fourTabListAdapter = new SimpleAdapter(this,getFourData(),R.layout.activity_assets_detail_four_pad_list_item,  
		        new String[]{"activityNum","activityName","activityDate"},
		        new int[]{R.id.activityNum,R.id.activityName,R.id.activityDate});
		listViewStandardWorkList.setAdapter(fourTabListAdapter);
	}

	private void initFiveTab() {
		// TODO Auto-generated method stub
		
	}
	private void initSixTab() {
		// TODO Auto-generated method stub
		ListView listViewDeviceDocumentList = (ListView)subView.findViewById(R.id.listViewDeviceDocumentList);
		SimpleAdapter sixTabListAdapter = new SimpleAdapter(this,getSixData(),R.layout.activity_assets_detail_six_pad_list_item,  
		        new String[]{"date","type","description"},
		        new int[]{R.id.date,R.id.type,R.id.description});
		listViewDeviceDocumentList.setAdapter(sixTabListAdapter);
	}


	private List<HashMap<String, String>> getSecondData() {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("materialsCode", "a1");
			map.put("materialsDescription", "b1");
			map.put("unit", "c1");
			map.put("Inventory", "d1");
			data.add(map);
		}
		return data;
	}
	private List<HashMap<String, String>> getThreeData() {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("event", "a2");
			map.put("eventDescription", "b2");
			map.put("dateTime", "c2");
			data.add(map);
		}
		return data;
	}
	private List<HashMap<String, String>> getFourData() {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("activityNum", "a3");
			map.put("activityName", "b3");
			map.put("activityDate", "c3");
			data.add(map);
		}
		return data;
	}
	private List<HashMap<String, String>> getSixData() {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("date", "a4");
			map.put("type", "b4");
			map.put("description", "c4");
			data.add(map);
		}
		return data;
	}

}
