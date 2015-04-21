package com.johnlam.cloudplayer;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.DBLog;

/**
 * 
 * 
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:CloundMusicDownloader
 * @Date:Nov 13, 2014
 * 
 */
public class SplashActivity extends DBFragmentActivity {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private ProgressBar mProgressBar;
	private boolean isPressBack;

	private Handler mHandler = new Handler();
	private TextView mTvCopyright;

	private TextView mTvVersion;
	private boolean isLoading;
	private TextView mTvAppName;
	private boolean isStartAnimation;
	private ImageView mImgLogo;
	protected boolean isShowingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.splash);
		this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		this.mTvCopyright = (TextView) findViewById(R.id.tv_copyright);
		this.mTvVersion = (TextView) findViewById(R.id.tv_version);
		this.mTvAppName = (TextView) findViewById(R.id.tv_app_name);

		mImgLogo = (ImageView) findViewById(R.id.img_logo);

		this.mTvCopyright.setTypeface(mTypefaceNormal);
		this.mTvVersion.setTypeface(mTypefaceNormal);
		this.mTvAppName.setTypeface(mTypefaceLogo);

		mProgressBar.setVisibility(View.INVISIBLE);
		mTvAppName.setVisibility(View.INVISIBLE);

		mTvVersion.setText(String.format(getString(R.string.info_version_format), ApplicationUtils.getVersionName(this)));
		DBLog.setDebug(DEBUG);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!ApplicationUtils.isOnline(this)){
			if(!isShowingDialog){
				isShowingDialog=true;
				showDialogTurnOnInternetConnection();
			}
		}
		else{
			if (!isLoading) {
				isLoading = true;
				startAnimationLogo(new IDBCallback() {
					@Override
					public void onAction() {
						mProgressBar.setVisibility(View.VISIBLE);
						mTvAppName.setVisibility(View.VISIBLE);

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								mProgressBar.setVisibility(View.INVISIBLE);
								Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
								startActivity(mIntent);
								finish();
							}
						}, 3000);
					}
				});
			}
		}

	}

	private void startAnimationLogo(final IDBCallback mCallback) {
		if (!isStartAnimation) {
			isStartAnimation = true;
			mProgressBar.setVisibility(View.INVISIBLE);
			mImgLogo.setRotationY(-180);

			AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
			final ViewPropertyAnimator localViewPropertyAnimator = mImgLogo.animate().rotationY(0).setDuration(1000).setInterpolator(mInterpolator);

			localViewPropertyAnimator.setListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					if (mCallback != null) {
						mCallback.onAction();
					}
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					if (mCallback != null) {
						mCallback.onAction();
					}
				}
			});
			localViewPropertyAnimator.start();
		}
		else {
			if (mCallback != null) {
				mCallback.onAction();
			}
		}
	}
	
	private void showDialogTurnOnInternetConnection() {
		Dialog mDialog = AlertDialogUtils.createFullDialog(this, 0, R.string.title_warning, R.string.title_settings, R.string.title_cancel,
				R.string.info_lose_internet, new IOnDialogListener() {
					@Override
					public void onClickButtonPositive() {
						isShowingDialog=false;
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					}

					@Override
					public void onClickButtonNegative() {
						isShowingDialog=false;
						onDestroyData();
						finish();
					}
				});
		mDialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onDestroyData() {
		super.onDestroyData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isPressBack) {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
