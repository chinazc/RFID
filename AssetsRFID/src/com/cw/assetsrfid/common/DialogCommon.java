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
    // 使用AlertBuilder创建新的对话框  
    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());  
    // 配置对话框UI  
    dialog.setTitle(_titleString);  
    dialog.setMessage(_messageString);  
    // 返回配置完成的对话框  
    return dialog.create();  
  }



}
