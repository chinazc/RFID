package com.cw.assetsrfid.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

/**
 * ��ʾ�Ի����ࣨandroid�еĶԻ����ǷǶ���ʽ�ģ�
 * 
 * @author dingyou.tang
 * 
 */
public class MessageBox {

	/**
	 * �Ի���İ�ťö������
	 * 
	 * @author dingyou.tang
	 * 
	 */
	public enum MessageBoxButton {
		/**
		 * [ȷ��]��ť
		 */
		Button_Confirm,
		/**
		 * [��]��ť
		 */
		Button_Yes,
		/**
		 * [��]��ť
		 */
		Button_No,
		/**
		 * [ȡ��]��ť
		 */
		Button_Cancel
	}

	private Context context;

	private AlertDialog.Builder dlg;

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 */
	public MessageBox(Context context) {
		this.context = context;
	}

	/**
	 * �򿪶Ի���ֻ����һ����ť��
	 * 
	 * @param icon
	 *            �Ի���ͼ��
	 * @param title
	 *            �Ի�����ʾ����
	 * @param positiveButton
	 *            ��ť
	 * @param positiveListener
	 *            ��ť�����¼�
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
	 * �򿪶Ի���ֻ����һ����ť��
	 * 
	 * @param icon
	 *            �Ի���ͼ��
	 * @param title
	 *            �Ի�����ʾ����
	 * @param positiveButton
	 *            ��ť
	 * @param positiveListener
	 *            ��ť�����¼�
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
	 * �򿪶Ի��򣨰���������ť��
	 * 
	 * @param icon
	 *            �Ի���ͼ��
	 * @param title
	 *            �Ի�����ʾ����
	 * @param positiveButton
	 *            ��ť
	 * @param positiveListener
	 *            ��ť�����¼�
	 * @param negativeButton
	 *            ��ť
	 * @param negativeListener
	 *            ��ť�����¼�
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
	 * �򿪶Ի��򣨰���������ť��
	 * 
	 * @param icon
	 *            �Ի���ͼ��
	 * @param title
	 *            �Ի�����ʾ����
	 * @param positiveButton
	 *            ��ť
	 * @param positiveListener
	 *            ��ť�����¼�
	 * @param negativeButton
	 *            ��ť
	 * @param negativeListener
	 *            ��ť�����¼�
	 * @param neutralButton
	 *            ��ť
	 * @param neutralListener
	 *            ��ť�����¼�
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
	 * ȡ�ð�ť������
	 * 
	 * @param button
	 * @return
	 */
	private String GetStringFromEnum(MessageBoxButton button) {
		String rtn = "ȷ��";
		switch (button) {
		case Button_Cancel:
			rtn = "ȡ��";
			break;
		case Button_No:
			rtn = "��";
			break;
		case Button_Yes:
			rtn = "��";
			break;
		case Button_Confirm:
			rtn = "ȷ��";
			break;
		}
		return rtn;
	}
}
