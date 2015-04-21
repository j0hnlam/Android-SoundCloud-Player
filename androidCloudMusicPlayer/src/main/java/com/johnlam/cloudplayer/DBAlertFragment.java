package com.johnlam.cloudplayer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener;

/**
 * 
 *
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:AndroidCloundMusicPlayer
 * @Date:Dec 14, 2014 
 *
 */
public class DBAlertFragment extends DialogFragment {
	
	public static final String TAG = DBAlertFragment.class.getSimpleName();
	
	public static final int TYPE_DIALOG_FULL = 1;
	public static final int TYPE_DIALOG_INFO = 2;
	
	public static final String KEY_TYPE="type";
	public static final String KEY_ID_DIALOG="id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_ICON = "icon";
	public static final String KEY_POSITIVE = "positive";
	public static final String KEY_NEGATIVE = "negative";
	
	public static DBAlertFragment newInstance(int idDialog,int iconId, int titleId, int idPositive, 
			int messageId) {
		
		DBAlertFragment frag = new DBAlertFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_TYPE, TYPE_DIALOG_INFO);
		args.putInt(KEY_ID_DIALOG, idDialog);
		args.putInt(KEY_TITLE, titleId);
		args.putInt(KEY_MESSAGE, messageId);
		args.putInt(KEY_ICON, iconId);
		args.putInt(KEY_POSITIVE, idPositive);
		frag.setArguments(args);
		return frag;
	}
	public static DBAlertFragment newInstance(int idDialog,int iconId, int titleId, int idPositive, int idNegative, 
			int messageId) {
		
		DBAlertFragment frag = new DBAlertFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_TYPE, TYPE_DIALOG_FULL);
		args.putInt(KEY_ID_DIALOG, idDialog);
		args.putInt(KEY_TITLE, titleId);
		args.putInt(KEY_MESSAGE, messageId);
		args.putInt(KEY_ICON, iconId);
		args.putInt(KEY_POSITIVE, idPositive);
		args.putInt(KEY_NEGATIVE, idNegative);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle mBundle = getArguments();
		int type = mBundle.getInt(KEY_TYPE);
		int idTitle = mBundle.getInt(KEY_TITLE);
		int idMessage = mBundle.getInt(KEY_MESSAGE);
		int idIcon = mBundle.getInt(KEY_ICON);
		int idPositive = mBundle.getInt(KEY_POSITIVE);
		final int idDialog = mBundle.getInt(KEY_ID_DIALOG);
		
		final DBFragmentActivity mContext = (DBFragmentActivity) getActivity();
		switch (type) {
		case TYPE_DIALOG_INFO:
			return AlertDialogUtils.createInfoDialog(mContext, idIcon, idTitle, idPositive, idMessage,new IOnDialogListener() {
				
				@Override
				public void onClickButtonPositive() {
					mContext.doPositiveClick(idDialog);
				}
				
				@Override
				public void onClickButtonNegative() {
					mContext.doNegativeClick(idDialog);
				}
			});

		case TYPE_DIALOG_FULL:
			int idNegative = mBundle.getInt(KEY_NEGATIVE);
			return AlertDialogUtils.createFullDialog(mContext, idIcon, idTitle, idPositive,idNegative, idMessage,new IOnDialogListener() {
				
				@Override
				public void onClickButtonPositive() {
					mContext.doPositiveClick(idDialog);
				}
				
				@Override
				public void onClickButtonNegative() {
					mContext.doNegativeClick(idDialog);
				}
			});
		}
		return super.onCreateDialog(savedInstanceState);
	}
	
}
