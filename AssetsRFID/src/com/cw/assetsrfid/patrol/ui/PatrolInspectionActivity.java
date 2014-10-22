package com.cw.assetsrfid.patrol.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cw.assetsrfid.AbstractBaseActivity;
import com.cw.assetsrfid.R;

public class PatrolInspectionActivity extends AbstractBaseActivity implements OnClickListener,OnItemClickListener{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_inspection_main);
		
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
