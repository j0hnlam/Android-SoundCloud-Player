package com.johnlam.soundcloud;

import java.io.InputStream;
import java.util.ArrayList;

import com.johnlam.soundcloud.object.CommentObject;
import com.johnlam.soundcloud.object.TrackObject;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;

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
public class SoundCloudAPI implements ISoundCloudConstants {
	
	private static final String TAG = SoundCloudAPI.class.getSimpleName();
	
	private String clientId;
	private String clientSecret;
	private String mPrefixClientId;
	
	public SoundCloudAPI(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.mPrefixClientId = String.format(FORMAT_CLIENT_ID, clientId);
	}
	
	public ArrayList<TrackObject> getListTrackObjectsByGenre(String genre,int offset, int limit){
		StringBuilder mStringBuilder = new StringBuilder();
		mStringBuilder.append(URL_API);
		mStringBuilder.append(METHOD_TRACKS);
		mStringBuilder.append(JSON_PREFIX);
		mStringBuilder.append(mPrefixClientId);
		mStringBuilder.append(String.format(FILTER_GENRE, genre));
		mStringBuilder.append(String.format(OFFSET, String.valueOf(offset),String.valueOf(limit)));
		
		String url = mStringBuilder.toString();
		mStringBuilder=null;
		
		DBLog.d(TAG, "==============>getListTrackObjectsByGenre="+url);
		InputStream data = DownloadUtils.download(url);
		if(data!=null){
			return SoundCloudJsonParsingUtils.parsingListTrackObject(data);
		}
		return null;
	}
	
	public ArrayList<TrackObject> getListTrackObjectsByQuery(String query,int offset, int limit){
		StringBuilder mStringBuilder = new StringBuilder();
		mStringBuilder.append(URL_API);
		mStringBuilder.append(METHOD_TRACKS);
		mStringBuilder.append(JSON_PREFIX);
		mStringBuilder.append(mPrefixClientId);
		mStringBuilder.append(String.format(FILTER_QUERY, query));
		mStringBuilder.append(String.format(OFFSET, String.valueOf(offset),String.valueOf(limit)));
		
		String url = mStringBuilder.toString();
		mStringBuilder=null;
		
		DBLog.d(TAG, "==============>getListTrackObjectsByQuery="+url);
		InputStream data = DownloadUtils.download(url);
		if(data!=null){
			return SoundCloudJsonParsingUtils.parsingListTrackObject(data);
		}
		return null;
	}
	public ArrayList<TrackObject> getListTrackObjectsByTypes(String types,int offset, int limit){
		StringBuilder mStringBuilder = new StringBuilder();
		mStringBuilder.append(URL_API);
		mStringBuilder.append(METHOD_TRACKS);
		mStringBuilder.append(JSON_PREFIX);
		mStringBuilder.append(mPrefixClientId);
		mStringBuilder.append(String.format(FILTER_TYPES, types));
		mStringBuilder.append(String.format(OFFSET, String.valueOf(offset),String.valueOf(limit)));
		
		String url = mStringBuilder.toString();
		mStringBuilder=null;
		
		DBLog.d(TAG, "==============>getListTrackObjectsByQuery="+url);
		InputStream data = DownloadUtils.download(url);
		if(data!=null){
			return SoundCloudJsonParsingUtils.parsingListTrackObject(data);
		}
		return null;
	}
	
	public ArrayList<TrackObject> getListTrackObjectsOfUser(long userId){
		StringBuilder mStringBuilder = new StringBuilder();
		mStringBuilder.append(URL_API);
		mStringBuilder.append(METHOD_USER+"/");
		mStringBuilder.append(String.valueOf(userId)+"/");
		mStringBuilder.append(METHOD_TRACKS);
		mStringBuilder.append(JSON_PREFIX);
		mStringBuilder.append(mPrefixClientId);
		String url = mStringBuilder.toString();
		mStringBuilder=null;
		
		DBLog.d(TAG, "==============>getListTrackObjectsOfUser="+url);
		InputStream data = DownloadUtils.download(url);
		if(data!=null){
			return SoundCloudJsonParsingUtils.parsingListTrackObject(data);
		}
		return null;
	}
	
	public ArrayList<CommentObject> getListCommentObject(long trackId){
		StringBuilder mStringBuilder = new StringBuilder();
		mStringBuilder.append(URL_API);
		mStringBuilder.append(METHOD_TRACKS+"/");
		mStringBuilder.append(String.valueOf(trackId)+"/");
		mStringBuilder.append(METHOD_COMMENTS);
		mStringBuilder.append(JSON_PREFIX);
		mStringBuilder.append(mPrefixClientId);
		String url = mStringBuilder.toString();
		mStringBuilder=null;
		
		DBLog.d(TAG, "==============>getListCommentObject="+url);
		InputStream data = DownloadUtils.download(url);
		if(data!=null){
			return SoundCloudJsonParsingUtils.parsingListCommentObject(data);
		}
		return null;
	}
	
	public TrackObject getTrackObject(long id){
		StringBuilder mStringBuilder = new StringBuilder();
		mStringBuilder.append(URL_API);
		mStringBuilder.append(METHOD_TRACKS);
		mStringBuilder.append("/");
		mStringBuilder.append(String.valueOf(id));
		mStringBuilder.append(JSON_PREFIX);
		mStringBuilder.append(mPrefixClientId);
		String url = mStringBuilder.toString();
		mStringBuilder=null;
		
		DBLog.d(TAG, "==============>getTrackObject="+url);
		String data = DownloadUtils.downloadString(url);
		if(!StringUtils.isEmptyString(data)){
			return SoundCloudJsonParsingUtils.parsingTrackObject(data);
		}
		return null;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	
	
	
	
}
