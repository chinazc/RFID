package com.cw.assetsrfid.main;

import com.cw.assetsrfid.common.ConfigInit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class InitAppStaticDataTask extends AsyncTask<Bundle, Void, ProgressDialog>{

	private Context context;
	private ProgressDialog dialog;
	@Override
	protected ProgressDialog doInBackground(Bundle... params) {
		context = (Context) params[0].get("context");
		dialog = (ProgressDialog) params[0].get("dialog");
//		ConfigInit.AssetCategories_Hash = 
				
		return dialog;
	}

	@Override
	protected void onPostExecute(ProgressDialog dialog) {
		// TODO Auto-generated method stub
		super.onPostExecute(dialog);
		if (dialog != null) dialog.cancel();
	}


}
