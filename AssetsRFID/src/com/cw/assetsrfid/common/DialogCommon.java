package com.cw.assetsrfid.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class DialogCommon extends DialogFragment {
	private String _titleString;
	private String _messageString;
	
	public static DialogCommon newinstants(String title,String message)
	{
		DialogCommon dialog=new DialogCommon();
		dialog._titleString=title;
		dialog._messageString=message;
		return dialog;
	}
	
  @Override  
  public Dialog onCreateDialog(Bundle savedInstanceState) {  
    // ʹ��AlertBuilder�����µĶԻ���  
    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());  
    // ���öԻ���UI  
    dialog.setTitle(_titleString);  
    dialog.setMessage(_messageString);  
    // ����������ɵĶԻ���  
    return dialog.create();  
  }



}
