package com.cw.assetsrfid.approval.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;

public class ApprovalListActivity extends AbstractBaseActivity implements OnClickListener,OnItemClickListener{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approval_list_main);
		initTitle();
		init();
	}

	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		btnRight.setVisibility(View.GONE);
		btnLeft.setText("∑µªÿ");
		btnLeft.setOnClickListener(this);
		titleName.setText("ø‚¥Ê≤È—Ø");
	}
	private void init() {
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
