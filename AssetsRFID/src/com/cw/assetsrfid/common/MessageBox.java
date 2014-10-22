package com.cw.assetsrfid.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

/**
 * 提示对话框类（android中的对话框是非堵塞式的）
 * 
 * @author dingyou.tang
 * 
 */
public class MessageBox {

	/**
	 * 对话框的按钮枚举类型
	 * 
	 * @author dingyou.tang
	 * 
	 */
	public enum MessageBoxButton {
		/**
		 * [确定]按钮
		 */
		Button_Confirm,
		/**
		 * [是]按钮
		 */
		Button_Yes,
		/**
		 * [否]按钮
		 */
		Button_No,
		/**
		 * [取消]按钮
		 */
		Button_Cancel
	}

	private Context context;

	private AlertDialog.Builder dlg;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            父窗体
	 */
	public MessageBox(Context context) {
		this.context = context;
	}

	/**
	 * 打开对话框（只包含一个按钮）
	 * 
	 * @param icon
	 *            对话框图标
	 * @param title
	 *            对话框显示内容
	 * @param positiveButton
	 *            按钮
	 * @param positiveListener
	 *            按钮监听事件
	 */
	public void ShowMsg(int icon, String title,
			MessageBoxButton positiveButton, OnClickListener positiveListener) {
		dlg = new AlertDialog.Builder(context);
		dlg.setIcon(icon);
		dlg.setTitle(title);
		dlg.setPositiveButton(GetStringFromEnum(positiveButton),
				positiveListener);
		dlg.show();
	}
	
	public void ShowMsg(Drawable icon, String title, String message,
			MessageBoxButton positiveButton, OnClickListener positiveListener) {
		dlg = new AlertDialog.Builder(context);
		dlg.setIcon(icon);
		dlg.setTitle(title);
		dlg.setMessage(message);
		dlg.setPositiveButton(GetStringFromEnum(positiveButton),
				positiveListener);
		dlg.show();
	}

	/**
	 * 打开对话框（只包含一个按钮）
	 * 
	 * @param icon
	 *            对话框图标
	 * @param title
	 *            对话框显示内容
	 * @param positiveButton
	 *            按钮
	 * @param positiveListener
	 *            按钮监听事件
	 */
	public void ShowMsg(Drawable icon, String title,
			MessageBoxButton positiveButton, OnClickListener positiveListener) {
		dlg = new AlertDialog.Builder(context);
		dlg.setIcon(icon);
		dlg.setTitle(title);
		dlg.setPositiveButton(GetStringFromEnum(positiveButton),
				positiveListener);
		dlg.show();
	}

	/**
	 * 打开对话框（包含两个按钮）
	 * 
	 * @param icon
	 *            对话框图标
	 * @param title
	 *            对话框显示内容
	 * @param positiveButton
	 *            按钮
	 * @param positiveListener
	 *            按钮监听事件
	 * @param negativeButton
	 *            按钮
	 * @param negativeListener
	 *            按钮监听事件
	 */
	public void ShowMsg(int icon, String title,
			MessageBoxButton positiveButton, OnClickListener positiveListener,
			MessageBoxButton negativeButton, OnClickListener negativeListener) {
		dlg = new AlertDialog.Builder(context);
		dlg.setIcon(icon);
		dlg.setTitle(title);
		dlg.setPositiveButton(GetStringFromEnum(positiveButton),
				positiveListener);
		dlg.setNegativeButton(GetStringFromEnum(negativeButton),
				negativeListener);
		dlg.show();
	}

	/**
	 * 打开对话框（包含三个按钮）
	 * 
	 * @param icon
	 *            对话框图标
	 * @param title
	 *            对话框显示内容
	 * @param positiveButton
	 *            按钮
	 * @param positiveListener
	 *            按钮监听事件
	 * @param negativeButton
	 *            按钮
	 * @param negativeListener
	 *            按钮监听事件
	 * @param neutralButton
	 *            按钮
	 * @param neutralListener
	 *            按钮监听事件
	 */
	public void ShowMsg(int icon, String title,
			MessageBoxButton positiveButton, OnClickListener positiveListener,
			MessageBoxButton negativeButton, OnClickListener negativeListener,
			MessageBoxButton neutralButton, OnClickListener neutralListener) {
		dlg = new AlertDialog.Builder(context);
		dlg.setIcon(icon);
		dlg.setTitle(title);
		dlg.setPositiveButton(GetStringFromEnum(positiveButton),
				positiveListener);
		dlg.setNegativeButton(GetStringFromEnum(negativeButton),
				negativeListener);
		dlg.setNeutralButton(GetStringFromEnum(neutralButton), neutralListener);
		dlg.show();
	}

	/**
	 * 取得按钮的名称
	 * 
	 * @param button
	 * @return
	 */
	private String GetStringFromEnum(MessageBoxButton button) {
		String rtn = "确定";
		switch (button) {
		case Button_Cancel:
			rtn = "取消";
			break;
		case Button_No:
			rtn = "否";
			break;
		case Button_Yes:
			rtn = "是";
			break;
		case Button_Confirm:
			rtn = "确定";
			break;
		}
		return rtn;
	}
}
