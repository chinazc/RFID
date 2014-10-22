package com.cw.assetsrfid.fault;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;

public class FaultDetailShowActivity extends AbstractBaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.assets_fault_manage);
		initTitle();
		init();
	}

	private void init() {
		
	}

	private void initTitle(){
		btnLeft = (TextView) findViewById(R.id.app_title_left);
		btnRight = (TextView) findViewById(R.id.app_title_right);
		titleName = (TextView) findViewById(R.id.app_title_name);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		titleName.setText("π ’œ√˜œ∏");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
