package com.johnlam.cloudplayer;

import java.util.ArrayList;

import com.johnlam.cloudplayer.constants.ICloudMusicPlayerConstants;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener;
import com.ypyproductions.dialog.utils.IDialogFragmentListener;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBConstantURL;
import com.ypyproductions.utils.ResolutionUtils;
import com.ypyproductions.utils.ShareActionUtils;

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
public class DBFragmentActivity extends FragmentActivity implements IDBConstantURL, 
	IDialogFragmentListener, ICloudMusicPlayerConstants {
	
	public static final String TAG = DBFragmentActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;

	private int screenWidth;
	private int screenHeight;
	
	public ArrayList<Fragment> mListFragments;
	
	public Typeface mTypefaceNormal;
	public Typeface mTypefaceLight;
	public Typeface mTypefaceBold;
	public Typeface mTypefaceLogo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFormat(PixelFormat.RGBA_8888);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.createProgressDialog();
		
		mTypefaceNormal=Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		mTypefaceLight=Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		mTypefaceBold=Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		mTypefaceLogo=Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.otf");

		int[] mRes=ResolutionUtils.getDeviceResolution(this);
		if(mRes!=null && mRes.length==2){
			screenWidth=mRes[0];
			screenHeight=mRes[1];
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialogFragment(DIALOG_QUIT_APPLICATION);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showDialogFragment(int idDialog) {
		FragmentManager mFragmentManager = getSupportFragmentManager();
		switch (idDialog) {
		case DIALOG_LOSE_CONNECTION:
			createWarningDialog(DIALOG_LOSE_CONNECTION, R.string.title_warning, R.string.info_lose_internet).show(mFragmentManager, "DIALOG_LOSE_CONNECTION");
			break;
		case DIALOG_EMPTY:
			createWarningDialog(DIALOG_EMPTY, R.string.title_warning, R.string.info_empty).show(mFragmentManager, "DIALOG_EMPTY");
			break;
		case DIALOG_QUIT_APPLICATION:
			createQuitDialog().show(mFragmentManager, "DIALOG_QUIT_APPLICATION");
			break;
		case DIALOG_SEVER_ERROR:
			createWarningDialog(DIALOG_SEVER_ERROR, R.string.title_warning, R.string.info_server_error).show(mFragmentManager, "DIALOG_SEVER_ERROR");
			break;
		default:
			break;
		}
	}

	public DialogFragment createWarningDialog(int idDialog, int titleId, int messageId) {
		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(idDialog, android.R.drawable.ic_dialog_alert, titleId, android.R.string.ok, messageId);
		return mDAlertFragment;
	}
	
	public void showWarningDialog(int titleId, int messageId) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, 
				titleId, R.string.title_ok, messageId, null);
		mAlertDialog.show();
	}
	public void showWarningDialog(int titleId, String message) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, 
				titleId, R.string.title_ok, message, null);
		mAlertDialog.show();
	}
	public void showInfoDialog(int titleId, String message) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, 
				titleId, R.string.title_ok, message, null);
		mAlertDialog.show();
	}
	
	public void showInfoDialog(int titleId, String message, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, 
				titleId, R.string.title_ok, message, new IOnDialogListener() {
					
					@Override
					public void onClickButtonPositive() {
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
					
					@Override
					public void onClickButtonNegative() {
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
				});
		mAlertDialog.show();
	}
	public void showFullDialog(int titleId, int message,int idPositive,int idNegative, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createFullDialog(this, -1, titleId, idPositive, idNegative, message,new IOnDialogListener() {
					
					@Override
					public void onClickButtonPositive() {
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
					
					@Override
					public void onClickButtonNegative() {
						
					}
				});
		mAlertDialog.show();
	}
	
	public void showFullDialog(int titleId, String message,int idPositive,int idNegative, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createFullDialog(this, -1, titleId, idPositive, idNegative, message,new IOnDialogListener() {
					
					@Override
					public void onClickButtonPositive() {
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
					
					@Override
					public void onClickButtonNegative() {
						
					}
				});
		mAlertDialog.show();
	}
	public void showInfoDialog(int titleId, int message, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, 
				titleId, R.string.title_ok, message, new IOnDialogListener() {
					
					@Override
					public void onClickButtonPositive() {
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
					
					@Override
					public void onClickButtonNegative() {
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
				});
		mAlertDialog.show();
	}

	private DialogFragment createQuitDialog() {
		int mTitleId = R.string.title_confirm;
		int mYesId = R.string.title_yes;
		int mNoId = R.string.title_rate_us;
		int iconId = R.drawable.ic_launcher;
		int messageId = R.string.info_close_app;

		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(DIALOG_QUIT_APPLICATION, iconId, mTitleId, mYesId, mNoId, messageId);
		return mDAlertFragment;

	}

	
	private void createProgressDialog() {
		this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}

	public void showProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(this.getString(R.string.loading));
			if(!mProgressDialog.isShowing()){
				mProgressDialog.show();
			}
		}
	}
	public void showProgressDialog(int messageId) {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(this.getString(messageId));
			if(!mProgressDialog.isShowing()){
				mProgressDialog.show();
			}
		}
	}
	public void showProgressDialog(String message) {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(message);
			if(!mProgressDialog.isShowing()){
				mProgressDialog.show();
			}
		}
	}
	
	public void dimissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
	public void showToast(int resId) {
		showToast(getString(resId));
	}

	public void showToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public void showToastWithLongTime(int resId) {
		showToastWithLongTime(getString(resId));
	}
	
	public void showToastWithLongTime(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void doPositiveClick(int idDialog) {
		switch (idDialog) {
		case DIALOG_QUIT_APPLICATION:
			onDestroyData();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void doNegativeClick(int idDialog) {
		String url =String.format(URL_FORMAT_LINK_APP, getPackageName());
		ShareActionUtils.goToUrl(this, url);
	}
	
	public void onDestroyData(){
		
	}
	
	public void createArrayFragment(){
		mListFragments=new ArrayList<Fragment>();
	}
	
	public void addFragment(Fragment mFragment){
		if(mFragment!=null && mListFragments!=null){
			synchronized (mListFragments) {
				mListFragments.add(mFragment);
			}
		}
	}
	
	public boolean backStack(IDBCallback mCallback){
		if(mListFragments!=null && mListFragments.size()>0){
			int count =mListFragments.size();
			if(count>0){
				synchronized (mListFragments) {
					Fragment mFragment = mListFragments.remove(count-1);
					if(mFragment!=null){
						if(mFragment instanceof DBFragment){
							((DBFragment)mFragment).backToHome(this);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
