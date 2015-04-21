package com.johnlam.cloudplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.MatrixCursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.johnlam.soundcloud.ISoundCloudConstants;
import com.johnlam.soundcloud.SoundCloudAPI;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.johnlam.cloudplayer.adapter.SuggestionAdapter;
import com.johnlam.cloudplayer.adapter.TrackAdapter;
import com.johnlam.cloudplayer.adapter.TrackAdapter.ITrackAdapterListener;
import com.johnlam.cloudplayer.dataMng.XMLParsingData;
import com.johnlam.cloudplayer.setting.SettingManager;
import com.johnlam.soundcloud.object.TrackObject;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.DBListExcuteAction;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.ShareActionUtils;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;


public class MainActivity extends DBFragmentActivity implements IDBFragmentConstants, ISoundCloudConstants {
	public static final String TAG = MainActivity.class.getSimpleName();

	public static final int BUFFER_SIZE = 1024;

	private SearchView searchView;

	private Menu mMenu;

	private ArrayList<String> mListSuggestionStr;
	private SuggestionAdapter mSuggestAdapter;

	private String[] mColumns;

	private Object[] mTempData;

	private MatrixCursor mCursor;

	private TextView mTvNoResult;

	private PullToRefreshListView mListView;

	private DisplayImageOptions mImgTrackOptions;

	private DBTask mDBTask;

	public SoundCloudAPI mSoundCloud = new SoundCloudAPI(SOUNDCLOUND_CLIENT_ID, SOUNDCLOUND_CLIENT_SECRET);

	private TrackAdapter mAdapter;
	private ArrayList<TrackObject> mListTrackObjects;

	protected ProgressDialog progressDialog;

	private RelativeLayout mLayoutPlayMusic;

	private SeekBar mSeekbar;

	private TextView mTvCurrentTime;

	private TextView mTvDuration;

	private Button mBtnPlay;

	private TextView mTvTitleSongs;
	private Handler mHandler = new Handler();

	private MediaPlayer mPlayer;

	private TrackObject mCurrentTrack;

	private Button mBtnClose;

	private ProgressBar mProgressBar;

	private AdView adView;

	private InterstitialAd mInterstitial;

	private DisplayImageOptions mAvatarOptions;

	private Button mBtnPrev;

	private Button mBtnNext;

	private TextView mTvLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		this.mListView = (PullToRefreshListView) findViewById(R.id.list_tracks);
		this.mTvNoResult = (TextView) findViewById(R.id.tv_no_result);
		this.mTvNoResult.setTypeface(mTypefaceNormal);

		this.mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				startGetData(true, SettingManager.getLastKeyword(MainActivity.this));
			}
		});

		SettingManager.setFirstTime(this, true);
		setUpPlayMusicLayout();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(400, 400).diskCacheExtraOptions(400, 400, null)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.FIFO).writeDebugLogs().build();
		ImageLoader.getInstance().init(config);

		this.mImgTrackOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.music_note).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true).build();

		this.mAvatarOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_account_circle_grey).resetViewBeforeLoading(false).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();

		//setUpLayoutAdmob();
		handleIntent(getIntent());

		startGetData(false, SettingManager.getLastKeyword(MainActivity.this));
	}

	/*private void setUpLayoutAdmob() {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
		if (ApplicationUtils.isOnline(this)) {
			if (SHOW_ADVERTISEMENT) {
				adView = new AdView(this);
				adView.setAdUnitId(ADMOB_ID_BANNER);
				adView.setAdSize(AdSize.BANNER);

				layout.addView(adView);
				AdRequest mAdRequest = new AdRequest.Builder().build();
				adView.loadAd(mAdRequest);

				mInterstitial = new InterstitialAd(this);
				mInterstitial.setAdUnitId(ADMOB_ID_INTERTESTIAL);
				AdRequest adRequest = new AdRequest.Builder().build();
				mInterstitial.loadAd(adRequest);
			}
			else {
				layout.setVisibility(View.GONE);
			}
		}
		else {
			layout.setVisibility(View.GONE);
		}
	}*/

	private void setUpPlayMusicLayout() {
		mLayoutPlayMusic = (RelativeLayout) findViewById(R.id.layout_listen_music);
		mSeekbar = (SeekBar) findViewById(R.id.seekBar1);

		mLayoutPlayMusic.findViewById(R.id.img_bg).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mTvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
		mTvCurrentTime.setTypeface(mTypefaceLight);

		mTvDuration = (TextView) findViewById(R.id.tv_duration);
		mTvDuration.setTypeface(mTypefaceLight);

		mTvLink = (TextView) findViewById(R.id.tv_link);
		mTvLink.setTypeface(mTypefaceLight);

		mTvTitleSongs = (TextView) findViewById(R.id.tv_name_songs);
		mTvTitleSongs.setTypeface(mTypefaceBold);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		mBtnPlay = (Button) findViewById(R.id.btn_play);
		mBtnClose = (Button) findViewById(R.id.btn_close);
		mBtnPrev = (Button) findViewById(R.id.btn_prev);
		mBtnNext = (Button) findViewById(R.id.btn_next);

		mBtnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextTrack();
			}
		});

		mTvLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentTrack != null && !StringUtils.isEmptyString(mCurrentTrack.getPermalinkUrl())) {
					ShareActionUtils.goToUrl(MainActivity.this, mCurrentTrack.getPermalinkUrl());
				}
			}
		});
		mBtnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prevTrack();
			}
		});

		mBtnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (mPlayer != null) {
						if (mPlayer.isPlaying()) {
							mBtnPlay.setBackgroundResource(R.drawable.ic_play_arrow_grey600_36dp);
							mPlayer.pause();
						}
						else {
							mBtnPlay.setBackgroundResource(R.drawable.ic_pause_grey600_36dp);
							mPlayer.start();
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		mBtnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mProgressBar.getVisibility()!=View.VISIBLE){
					onHiddenPlay();
				}
				else{
					showToast(R.string.loading);
				}
				
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			processSearchData(query);
		}
	}

	@Override
	public void onDestroyData() {
		super.onDestroyData();
		ImageLoader.getInstance().stop();
		ImageLoader.getInstance().clearDiskCache();
		if (mInterstitial != null && mInterstitial.isLoaded()) {
			mInterstitial.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoader.getInstance().stop();
		onMusicStop();
		mColumns = null;
		mTempData = null;
		try {
			if (mCursor != null) {
				mCursor.close();
				mCursor = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processSearchData(final String query) {
		if (!StringUtils.isEmptyString(query)) {
			final String mQuery = StringUtils.urlEncodeString(query);
			startGetData(false, mQuery);
		}
	}

	private void startGetData(final boolean isRefresh, final String keyword) {
		if (!ApplicationUtils.isOnline(this)) {
			mListView.onRefreshComplete();
			if (isRefresh) {
				showToast(R.string.info_lose_internet);
			}
			if (mAdapter == null) {
				this.mTvNoResult.setVisibility(View.VISIBLE);
			}
			return;
		}
		mDBTask = new DBTask(new IDBTaskListener() {

			private ArrayList<TrackObject> mListNewTrackObjects;

			@Override
			public void onPreExcute() {
				if (!isRefresh) {
					showProgressDialog();
				}
			}

			@Override
			public void onDoInBackground() {
				mListNewTrackObjects = mSoundCloud.getListTrackObjectsByQuery(keyword, 0, 100);
				if (mListNewTrackObjects != null && mListNewTrackObjects.size() > 0) {
					SettingManager.setLastKeyword(MainActivity.this, keyword);
				}
			}

			@Override
			public void onPostExcute() {
				dimissProgressDialog();
				mListView.onRefreshComplete();
				hiddenVirtualKeyBoard();
				setUpInfo(mListNewTrackObjects);
			}

		});
		mDBTask.execute();

	}

	private void setUpInfo(ArrayList<TrackObject> mListNewTrackObjects) {
		mListView.setAdapter(null);
		if (mListTrackObjects != null) {
			mListTrackObjects.clear();
			mListTrackObjects = null;
		}
		this.mListTrackObjects = mListNewTrackObjects;
		if (mListNewTrackObjects != null && mListNewTrackObjects.size() > 0) {
			this.mTvNoResult.setVisibility(View.GONE);
			this.mListView.setVisibility(View.VISIBLE);
			mAdapter = new TrackAdapter(this, mListNewTrackObjects, mTypefaceBold, mTypefaceLight, mImgTrackOptions, mAvatarOptions);
			mListView.setAdapter(mAdapter);

			mAdapter.setTrackAdapterListener(new ITrackAdapterListener() {
				@Override
				public void onDownload(TrackObject mTrackObject) {
					showAlertDownload(mTrackObject);
				}

				@Override
				public void onListenDemo(TrackObject mTrackObject) {
					if (!ApplicationUtils.isOnline(MainActivity.this)) {
						showToast(R.string.info_server_error);
						return;
					}
					onListenMusicDemo(mTrackObject);
				}
			});
		}
		else {
			this.mTvNoResult.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLayoutPlayMusic.getVisibility() == View.VISIBLE) {
				onHiddenPlay();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onListenMusicDemo(final TrackObject mTrackObject) {
		mCurrentTrack = mTrackObject;
		mLayoutPlayMusic.setVisibility(View.VISIBLE);
		mTvTitleSongs.setText(mTrackObject.getTitle());
		mTvLink.setText(mTrackObject.getPermalinkUrl());
		mTvCurrentTime.setText("00:00");
		mProgressBar.setVisibility(View.VISIBLE);
		mSeekbar.setProgress(0);

		mBtnPlay.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.GONE);
		mBtnPrev.setVisibility(View.GONE);
		mTvLink.setVisibility(View.GONE);

		long duration = mTrackObject.getDuration() / 1000;
		String minute = String.valueOf((int) (duration / 60));
		String seconds = String.valueOf((int) (duration % 60));
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		if (seconds.length() < 2) {
			seconds = "0" + seconds;
		}
		mTvDuration.setText(minute + ":" + seconds);
		startGetLinkStream(new IDBCallback() {
			@Override
			public void onAction() {
				onCreateAndPlayMedia(mTrackObject);

			}
		}, false);

	}

	private void onCreateAndPlayMedia(final TrackObject mTrackObject) {
		mPlayer = new MediaPlayer();
		mPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mProgressBar.setVisibility(View.GONE);

				mBtnPlay.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
				mBtnPrev.setVisibility(View.VISIBLE);
				mTvLink.setVisibility(View.VISIBLE);

				mPlayer.start();
				mBtnPlay.setBackgroundResource(R.drawable.ic_pause_grey600_36dp);
				startUpdatePosition();

			}
		});
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				onMusicStop();
				mBtnPlay.setBackgroundResource(R.drawable.ic_play_arrow_grey600_36dp);
				nextTrack();

			}
		});
		mPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				showToast(R.string.info_play_error);
				onHiddenPlay();
				mCurrentTrack.setLinkStream("");
				return false;
			}
		});
		String url = mTrackObject.getLinkStream();
		try {
			mPlayer.setDataSource(url);
			mPlayer.prepareAsync();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			onHiddenPlay();
		}
		catch (SecurityException e) {
			e.printStackTrace();
			onHiddenPlay();
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			onHiddenPlay();
		}
		catch (IOException e) {
			e.printStackTrace();
			onHiddenPlay();
		}

	}

	private void startUpdatePosition() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mPlayer != null && mCurrentTrack != null) {
					int current = mPlayer.getCurrentPosition() / 1000;
					String minute = String.valueOf((int) (current / 60));
					String seconds = String.valueOf((int) (current % 60));
					if (minute.length() < 2) {
						minute = "0" + minute;
					}
					if (seconds.length() < 2) {
						seconds = "0" + seconds;
					}
					mTvCurrentTime.setText(minute + ":" + seconds);

					int percent = (int) (100f * ((float) mPlayer.getCurrentPosition() / (float) mCurrentTrack.getDuration()));
					mSeekbar.setProgress(percent);
					if (current < mCurrentTrack.getDuration()) {
						startUpdatePosition();
					}
				}
			}
		}, 1000);
	}

	private void onHiddenPlay() {
		onMusicStop();
		mProgressBar.setVisibility(View.GONE);
		mBtnPlay.setVisibility(View.VISIBLE);
		mBtnPlay.setBackgroundResource(R.drawable.ic_play_arrow_grey600_36dp);
		mLayoutPlayMusic.setVisibility(View.GONE);
	}

	private void showAlertDownload(final TrackObject mTrackObject) {
		showFullDialog(R.string.title_confirm, R.string.info_download, R.string.title_ok, R.string.title_cancel, new IDBCallback() {

			@Override
			public void onAction() {
				mCurrentTrack = mTrackObject;
				startGetLinkStream(new IDBCallback() {

					@Override
					public void onAction() {
						dowloadSong(mTrackObject);

					}
				}, true);
			}
		});
	}

	private void showDiaglogAboutUs() {
		AlertDialog mDialog = new AlertDialog.Builder(this).setTitle(R.string.title_about_us).setItems(R.array.list_share, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					ShareActionUtils.shareViaEmail(MainActivity.this, YOUR_EMAIL_CONTACT, "", "");
				}
				else if (which == 1) {
					Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
					mIntent.putExtra(KEY_URL, URL_YOUR_WEBSITE);
					mIntent.putExtra(KEY_HEADER, getString(R.string.title_website));
					startActivity(mIntent);
				}
				else if (which == 2) {
					Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
					mIntent.putExtra(KEY_URL, URL_YOUR_FACE_BOOK);
					mIntent.putExtra(KEY_HEADER, getString(R.string.title_facebook));
					startActivity(mIntent);
				}
				else if (which == 3) {
					Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
					mIntent.putExtra(KEY_URL, URL_YOUR_TWITTER);
					mIntent.putExtra(KEY_HEADER, getString(R.string.title_twitter));
					startActivity(mIntent);
				}
				else if (which == 4) {
					String url = String.format(URL_FORMAT_LINK_APP, getPackageName());
					ShareActionUtils.goToUrl(MainActivity.this, url);
				}

			}
		}).setPositiveButton(getString(R.string.title_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).create();
		mDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menuItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) menuItem.getActionView();
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.title_search) + "</font>"));

		SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				processSearchData(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				startSuggestion(newText);
				return false;
			}
		});
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean queryTextFocused) {
				if (!queryTextFocused) {
					MenuItem mMenuSearchItem = mMenu.findItem(R.id.action_search);
					if (mMenuSearchItem != null) {
						mMenuSearchItem.collapseActionView();
					}
					searchView.setQuery("", false);
				}
			}
		});

		searchView.setOnSuggestionListener(new OnSuggestionListener() {

			@Override
			public boolean onSuggestionSelect(int position) {
				return false;
			}

			@Override
			public boolean onSuggestionClick(int position) {
				if (mListSuggestionStr != null && mListSuggestionStr.size() > 0) {
					searchView.setQuery(mListSuggestionStr.get(position), false);
					processSearchData(mListSuggestionStr.get(position));
				}
				return false;
			}
		});

		this.mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_info:
			showDiaglogAboutUs();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void hiddenVirtualKeyBoard() {
		if (searchView != null) {
			searchView.clearFocus();
			ApplicationUtils.hiddenVirtualKeyboard(this, searchView);
		}
	}

	private void startSuggestion(final String search) {
		if (!StringUtils.isEmptyString(search)) {
			DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
				@Override
				public void onAction() {
					String url = String.format(URL_FORMAT_SUGESSTION, StringUtils.urlEncodeString(search));
					DBLog.d(TAG, "===============>url suggest=" + url);
					InputStream mInputStream = DownloadUtils.download(url);
					if (mInputStream != null) {
						final ArrayList<String> mListSuggestionStr = XMLParsingData.parsingSuggestion(mInputStream);
						if (mListSuggestionStr != null) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									searchView.setSuggestionsAdapter(null);
									mSuggestAdapter = null;
									if (MainActivity.this.mListSuggestionStr != null) {
										MainActivity.this.mListSuggestionStr.clear();
										MainActivity.this.mListSuggestionStr = null;
									}
									MainActivity.this.mListSuggestionStr = mListSuggestionStr;

									mTempData = null;
									mColumns = null;
									if (mCursor != null) {
										mCursor.close();
										mCursor = null;
									}
									mColumns = new String[] { "_id", "text" };
									mTempData = new Object[] { 0, "default" };
									mCursor = new MatrixCursor(mColumns);
									int size = mListSuggestionStr.size();
									mCursor.close();
									for (int i = 0; i < size; i++) {
										mTempData[0] = i;
										mTempData[1] = mListSuggestionStr.get(i);
										mCursor.addRow(mTempData);
									}
									mSuggestAdapter = new SuggestionAdapter(MainActivity.this, mCursor, mListSuggestionStr);
									searchView.setSuggestionsAdapter(mSuggestAdapter);
								}
							});
						}
					}
				}
			});
		}
	}

	private void dowloadSong(final TrackObject mTrackObject) {
		final File mCacheDir = new File(Environment.getExternalStorageDirectory(), NAME_FOLDER_DOWNLOAD);
		if (!mCacheDir.exists()) {
			mCacheDir.mkdirs();
		}
		final String url = mTrackObject.getLinkStream();
		DBLog.d(TAG, "=================>download url=" + url);

		final String nameFile = mTrackObject.getTitle() + ".mp3";
		final File mFile = new File(mCacheDir, nameFile);
		if (mFile.exists() && mFile.isFile()) {
			showToast(R.string.info_download_exits);
			return;
		}
		mDBTask = new DBTask(new IDBTaskListener() {

			private boolean isDownloadSuccess = false;

			@Override
			public void onPreExcute() {
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setTitle(R.string.title_download);
				progressDialog.setCancelable(false);
				progressDialog.setMax(100);
				progressDialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							return true;
						}
						return false;
					}
				});
				progressDialog.show();
			}

			@Override
			public void onDoInBackground() {
				InputStream is = null;
				URL u = null;
				try {
					u = new URL(url);
					is = u.openStream();
					HttpURLConnection huc = (HttpURLConnection) u.openConnection();
					huc.setReadTimeout(5000);

					boolean redirect = false;
					int status = huc.getResponseCode();
					if (status != HttpURLConnection.HTTP_OK) {
						if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
							redirect = true;
						}
					}
					if (redirect) {
						String newUrl = huc.getHeaderField("Location");
						DBLog.d(TAG, "==============>Redirect to URL : " + newUrl);

						u = new URL(newUrl);
						is = u.openStream();

						huc = (HttpURLConnection) u.openConnection();
						huc.setReadTimeout(5000);
						status = huc.getResponseCode();
					}
					DBLog.d(TAG, "=============>HttpURLConnection=" + status);
					if (huc != null && is != null && status == HttpURLConnection.HTTP_OK) {
						int size = huc.getContentLength();
						FileOutputStream fos = new FileOutputStream(mFile);
						byte[] buffer = new byte[BUFFER_SIZE];
						long total = 0;
						int len1 = 0;
						while ((len1 = is.read(buffer)) > 0) {
							total += len1;
							fos.write(buffer, 0, len1);
							final int progress = (int) ((total * 100) / size);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (progressDialog != null) {
										progressDialog.setProgress(progress);
									}
								}
							});
						}
						if (fos != null) {
							fos.close();
						}
						isDownloadSuccess = true;
					}
				}
				catch (MalformedURLException mue) {
					mue.printStackTrace();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
				finally {
					try {
						if (is != null) {
							is.close();
						}
					}
					catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}

			@Override
			public void onPostExcute() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if (isDownloadSuccess) {
					String pathImage = mCacheDir.getAbsolutePath() + "/" + nameFile;
					String info = String.format(getString(R.string.info_download_success), pathImage);
					showToast(info);
				}
				else {
					showToast(R.string.info_download_error);
				}
			}

		});
		mDBTask.execute();
	}

	private void onMusicStop() {
		mHandler.removeCallbacksAndMessages(null);
		try {
			if (mPlayer != null) {
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void nextTrack() {
		if (mListTrackObjects != null && mListTrackObjects.size() > 0 && mCurrentTrack != null) {
			int size = mListTrackObjects.size();
			int currentIndex = mListTrackObjects.indexOf(mCurrentTrack);
			currentIndex++;
			if (currentIndex < size) {
				TrackObject mTrackObject = mListTrackObjects.get(currentIndex);
				onMusicStop();
				onListenMusicDemo(mTrackObject);
			}
		}
	}

	private void prevTrack() {
		if (mListTrackObjects != null && mListTrackObjects.size() > 0 && mCurrentTrack != null) {
			int currentIndex = mListTrackObjects.indexOf(mCurrentTrack);
			currentIndex--;
			if (currentIndex >= 0) {
				TrackObject mTrackObject = mListTrackObjects.get(currentIndex);
				onMusicStop();
				onListenMusicDemo(mTrackObject);
			}
		}
	}

	private void startGetLinkStream(final IDBCallback mCallback, final boolean isShowProgress) {
		if (mCurrentTrack != null) {
			String linkStream = mCurrentTrack.getLinkStream();
			if (!StringUtils.isEmptyString(linkStream)) {
				if (mCallback != null) {
					mCallback.onAction();
				}
				return;
			}
			DBTask mDBTask = new DBTask(new IDBTaskListener() {
				private String finalUrl;

				@Override
				public void onPreExcute() {
					if (isShowProgress) {
						showProgressDialog();
					}
				}

				@Override
				public void onDoInBackground() {
					if (mCurrentTrack.isStreamable()) {
						finalUrl = getLinkStreamFromSoundClound(mCurrentTrack.getId());
					}
					if (StringUtils.isEmptyString(finalUrl)) {
						finalUrl = String.format(FORMAT_URL_SONG, mCurrentTrack.getId(), SOUNDCLOUND_CLIENT_ID);
					}
				}

				@Override
				public void onPostExcute() {
					if (isShowProgress) {
						dimissProgressDialog();
					}
					DBLog.d(TAG, "========>final Url=" + finalUrl);
					if (!StringUtils.isEmptyString(finalUrl)) {
						mCurrentTrack.setLinkStream(finalUrl);
					}
					if (mCallback != null) {
						mCallback.onAction();
					}
				}

			});
			mDBTask.execute();
		}
	}

	public static String getLinkStreamFromSoundClound(long id) {
		final String manualUrl = String.format(FORMAT_URL_SONG, String.valueOf(id), SOUNDCLOUND_CLIENT_ID);
		String dataServer = DownloadUtils.downloadString(manualUrl);
		DBLog.d(TAG, "=========>dataServer=" + dataServer);
		String finalUrl = null;
		if (!StringUtils.isEmptyString(dataServer)) {
			try {
				JSONObject mJsonObject = new JSONObject(dataServer);
				finalUrl = mJsonObject.getString("http_mp3_128_url");
			}
			catch (Exception e) {
				e.printStackTrace();
				finalUrl = manualUrl;
			}
		}
		return finalUrl;
	}

}
