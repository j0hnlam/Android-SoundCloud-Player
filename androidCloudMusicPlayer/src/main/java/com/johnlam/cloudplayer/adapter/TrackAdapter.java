package com.johnlam.cloudplayer.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.johnlam.cloudplayer.constants.ICloudMusicPlayerConstants;
import com.johnlam.soundcloud.object.TrackObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.johnlam.cloudplayer.R;
import com.ypyproductions.utils.ResolutionUtils;
import com.ypyproductions.utils.StringUtils;

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
public class TrackAdapter extends DBBaseAdapter implements ICloudMusicPlayerConstants {
	public static final String TAG = TrackAdapter.class.getSimpleName();

	private Typeface mTypefaceBold;

	private DisplayImageOptions mImgOptions;
	private ITrackAdapterListener trackAdapter;

	private DisplayImageOptions mAvatarOptions;
	private Typeface mTypefaceLight;

	private Date mDate;

	public TrackAdapter(Activity mContext, ArrayList<TrackObject> listDrawerObjects, Typeface mTypefaceBold, Typeface mTypefaceLight, DisplayImageOptions mImgOptions,
			DisplayImageOptions mAvatarOptions) {
		super(mContext, listDrawerObjects);
		this.mTypefaceBold = mTypefaceBold;
		this.mTypefaceLight = mTypefaceLight;

		this.mImgOptions = mImgOptions;
		this.mAvatarOptions = mAvatarOptions;
		this.mDate = new Date();
	}

	@Override
	public View getAnimatedView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public View getNormalView(int position, View convertView, ViewGroup parent) {
		final ViewHolder mHolder;
		LayoutInflater mInflater;
		if (convertView == null) {
			mHolder = new ViewHolder();
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_track, null);
			convertView.setTag(mHolder);

			mHolder.mImgSongs = (ImageView) convertView.findViewById(R.id.img_track);
			mHolder.mImgUsername = (ImageView) convertView.findViewById(R.id.img_avatar);
			
			mHolder.mTvSongName = (TextView) convertView.findViewById(R.id.tv_song);
			mHolder.mTvUsername = (TextView) convertView.findViewById(R.id.tv_username);
			mHolder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
			mHolder.mTvPlayCount = (TextView) convertView.findViewById(R.id.tv_playcount);
			mHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.mBtnDownload = (Button) convertView.findViewById(R.id.btn_download);
			mHolder.mRootLayout = (RelativeLayout) convertView.findViewById(R.id.layout_root);
			mHolder.mImgLogo = (ImageView) convertView.findViewById(R.id.img_logo);

			mHolder.mTvSongName.setTypeface(mTypefaceBold);
			mHolder.mTvUsername.setTypeface(mTypefaceBold);

			mHolder.mTvDuration.setTypeface(mTypefaceLight);
			mHolder.mTvPlayCount.setTypeface(mTypefaceLight);
			mHolder.mTvTime.setTypeface(mTypefaceLight);
			
			if(!ALLOW_DOWNLOAD_ALL){
				RelativeLayout.LayoutParams mLayoutParams= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				mLayoutParams.rightMargin=(int) ResolutionUtils.convertDpToPixel(mContext, 5);
				mHolder.mImgLogo.setLayoutParams(mLayoutParams);
			}
		}
		else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final TrackObject mTrackObject = (TrackObject) mListObjects.get(position);

		mHolder.mTvSongName.setText(mTrackObject.getTitle());
		mHolder.mTvUsername.setText(mTrackObject.getUsername());

		String strPlayerCount = formatVisualNumber(mTrackObject.getPlaybackCount(), ",");
		mHolder.mTvPlayCount.setText(strPlayerCount);

		Date mTrackDate = mTrackObject.getCreatedDate();
		if (mTrackDate != null) {
			String mTime = getStringTimeAgo((mDate.getTime() - mTrackDate.getTime()) / 1000);
			mHolder.mTvTime.setText(mTime);
		}

		long duration = mTrackObject.getDuration() / 1000;
		String minute = String.valueOf((int) (duration / 60));
		String seconds = String.valueOf((int) (duration % 60));
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		if (seconds.length() < 2) {
			seconds = "0" + seconds;
		}
		mHolder.mTvDuration.setText(minute + ":" + seconds);

		String urlTrack = mTrackObject.getArtworkUrl();
		if (StringUtils.isEmptyString(urlTrack) || urlTrack.equals("null")) {
			urlTrack = mTrackObject.getAvatarUrl();
		}
		if (!StringUtils.isEmptyString(urlTrack) && urlTrack.startsWith("http")) {
			urlTrack = urlTrack.replace("large", "crop");
			ImageLoader.getInstance().displayImage(urlTrack, mHolder.mImgSongs, mImgOptions);
		}
		else {
			mHolder.mImgSongs.setImageResource(R.drawable.music_note);
		}
		
		if(!StringUtils.isEmptyString(mTrackObject.getAvatarUrl()) && mTrackObject.getAvatarUrl().startsWith("http")){
			ImageLoader.getInstance().displayImage(mTrackObject.getAvatarUrl(), mHolder.mImgUsername, mAvatarOptions);
		}
		else{
			mHolder.mImgUsername.setImageResource(R.drawable.ic_account_circle_grey);
		}
		if(ALLOW_DOWNLOAD_ALL){
			mHolder.mBtnDownload.setVisibility(View.VISIBLE);
			mHolder.mBtnDownload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (trackAdapter != null) {
						trackAdapter.onDownload(mTrackObject);
					}
				}
			});
		}
		else{
			mHolder.mBtnDownload.setVisibility(View.INVISIBLE);
		}
		mHolder.mRootLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (trackAdapter != null) {
					trackAdapter.onListenDemo(mTrackObject);
				}
			}
		});
		return convertView;
	}

	public String formatVisualNumber(long numberValue, String demiter) {
		String value = String.valueOf(numberValue);
		if (value.length() > 3) {
			try {
				int number = (int) Math.floor(value.length() / 3);
				int lenght = value.length();
				String total = "";
				for (int i = 0; i < number; i++) {
					for (int j = 0; j < 3; j++) {
						int index = lenght - 1 - (i * 3 + j);
						total = value.charAt(index) + total;
					}
					if (i != number - 1) {
						total = demiter + total;
					}
					else {
						int delta = lenght - number * 3;
						if (delta > 0) {
							total = demiter + total;
						}
					}
				}
				total = value.substring(0, lenght - number * 3) + total;
				return total;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return String.valueOf(value);
	}

	public String getStringTimeAgo(long second) {
		double minutes = second / 60f;
		if (second < 5) {
			return "Just now";
		}
		else if (second < 60) {
			return String.valueOf(second) + " seconds ago";
		}
		else if (second < 120) {
			return "A minute ago";
		}
		else if (minutes < 60) {
			return String.valueOf((int) minutes) + " minutes ago";
		}
		else if (minutes < 120) {
			return "A hour ago";
		}
		else if (minutes < 24 * 60) {
			minutes = Math.floor(minutes / 60);
			return String.valueOf((int) minutes) + " hours ago";
		}
		else if (minutes < 24 * 60 * 2) {
			return "Yesterday";
		}
		else if (minutes < 24 * 60 * 7) {
			minutes = Math.floor(minutes / (60 * 24));
			return String.valueOf((int) minutes) + " days ago";
		}
		else if (minutes < 24 * 60 * 14) {
			return "Last week";
		}
		else if (minutes < 24 * 60 * 31) {
			minutes = Math.floor(minutes / (60 * 24 * 7));
			return String.valueOf((int) minutes) + " weeks ago";
		}
		else if (minutes < 24 * 60 * 61) {
			return "Last Month";
		}
		else if (minutes < 24 * 60 * 365.25) {
			minutes = Math.floor(minutes / (60 * 24 * 30));
			return String.valueOf((int) minutes) + " months ago";
		}
		else if (minutes < 24 * 60 * 731) {
			return "Last year";
		}
		else if (minutes > 24 * 60 * 731) {
			minutes = Math.floor(minutes / (60 * 24 * 365));
			return String.valueOf((int) minutes) + " years ago";
		}
		return "Unknown";
	}

	public interface ITrackAdapterListener {
		public void onDownload(TrackObject mTrackObject);

		public void onListenDemo(TrackObject mTrackObject);
	}

	public void setTrackAdapterListener(ITrackAdapterListener trackAdapter) {
		this.trackAdapter = trackAdapter;
	}

	private static class ViewHolder {
		public RelativeLayout mRootLayout;
		public ImageView mImgSongs;
		public ImageView mImgUsername;
		public TextView mTvSongName;
		public TextView mTvUsername;
		public Button mBtnDownload;
		public TextView mTvDuration;
		public TextView mTvPlayCount;
		public TextView mTvTime;
		public ImageView mImgLogo;
	}

}
