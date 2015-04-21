
package com.johnlam.soundcloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;
import android.util.JsonToken;

import com.johnlam.soundcloud.object.CommentObject;
import com.johnlam.soundcloud.object.TrackObject;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.DateTimeUtils;
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
public class SoundCloudJsonParsingUtils {
	
	public static final String TAG = SoundCloudJsonParsingUtils.class.getSimpleName();
	public static final String DATE_PATTERN_ORI = "yyyy/MM/dd hh:mm:ss Z"; 
	
	private static TrackObject parsingTrackObject(JSONObject mJsonObject){
		if(mJsonObject!=null){
			try {
				long id = mJsonObject.getLong("id");
				String createdAt = mJsonObject.getString("created_at");
				Date mDate = DateTimeUtils.getDateFromString(createdAt, DATE_PATTERN_ORI);
				
				long userId = mJsonObject.getLong("user_id");
				long duration = mJsonObject.getLong("duration");
				String sharing = mJsonObject.getString("sharing");
				String tagList = mJsonObject.getString("tag_list");
				String genre = mJsonObject.getString("genre");
				String title = mJsonObject.getString("title");
				String description= mJsonObject.getString("description");
				
				JSONObject mJsUser = mJsonObject.getJSONObject("user");
				String username = mJsUser.getString("username");
				String avartar = mJsUser.getString("avatar_url");
				String permalinkUrl= mJsonObject.getString("permalink_url");
				String artworkUrl = mJsonObject.getString("artwork_url");
				String waveformUrl = mJsonObject.getString("waveform_url");
				long playCount = mJsonObject.getLong("playback_count");
				long favoritingsCount = mJsonObject.getLong("favoritings_count");
				long commentCount = mJsonObject.getLong("comment_count");
				boolean streamable = mJsonObject.getBoolean("streamable");
				
				TrackObject mTrackObject = new TrackObject(id, mDate, userId, duration, sharing,
						tagList, genre, title, description, username, avartar, permalinkUrl, artworkUrl, waveformUrl,
						playCount, favoritingsCount, commentCount);
				mTrackObject.setStreamable(streamable);
				
				
				return mTrackObject;
				
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static TrackObject parsingNewTrackObject(JsonReader reader){
		if(reader!=null){
			try {
				TrackObject mTrackObject=null;
				while (reader.hasNext()) {
					String name = reader.nextName();
					if (name.equals("id")) {
						mTrackObject = new TrackObject();
						mTrackObject.setId(reader.nextLong());
					}
					else if(name.equals("created_at")){
						if(mTrackObject!=null){
							String createdAt = reader.nextString();
							Date mDate = DateTimeUtils.getDateFromString(createdAt, DATE_PATTERN_ORI);
							mTrackObject.setCreatedDate(mDate);
						}
					}
					else if(name.equals("user_id")){
						if(mTrackObject!=null){
							mTrackObject.setUserId(reader.nextLong());
						}
					}
					else if(name.equals("duration")){
						if(mTrackObject!=null){
							mTrackObject.setDuration(reader.nextLong());
						}
					}
					else if(name.equals("sharing")){
						if(mTrackObject!=null){
							mTrackObject.setSharing(reader.nextString());
						}
					}
					else if(name.equals("tag_list") &&  reader.peek() != JsonToken.NULL){
						if(mTrackObject!=null){
							mTrackObject.setTagList(reader.nextString());
						}
					}
					else if (name.equals("streamable") && reader.peek() != JsonToken.NULL) {
						if (mTrackObject != null) {
							mTrackObject.setStreamable(reader.nextBoolean());
						}
					}
					else if (name.equals("downloadable") && reader.peek() != JsonToken.NULL) {
						if (mTrackObject != null) {
							mTrackObject.setDownloadable(reader.nextBoolean());
						}
					}
					else if(name.equals("genre") &&  reader.peek() != JsonToken.NULL){
						if(mTrackObject!=null){
							mTrackObject.setGenre(reader.nextString());
						}
					}
					else if(name.equals("title") &&  reader.peek() != JsonToken.NULL){
						if(mTrackObject!=null){
							mTrackObject.setTitle(reader.nextString());
						}
					}
					else if(name.equals("description") &&  reader.peek() != JsonToken.NULL){
						if(mTrackObject!=null){
							mTrackObject.setDescription(reader.nextString());
						}
					}
					else if(name.equals("user")){
						reader.beginObject();
						while (reader.hasNext()) {
							String nameTagUser = reader.nextName();
							if(nameTagUser.equals("username")){
								if(mTrackObject!=null){
									mTrackObject.setUsername(reader.nextString());
								}
							}
							else if(nameTagUser.equals("avatar_url") &&  reader.peek() != JsonToken.NULL){
								if(mTrackObject!=null){
									mTrackObject.setAvatarUrl(reader.nextString());
								}
							}
							else{
								reader.skipValue();
							}
						}
						reader.endObject();
					}
					else if(name.equals("permalink_url")){
						if(mTrackObject!=null){
							mTrackObject.setPermalinkUrl(reader.nextString());
						}
					}
					else if(name.equals("artwork_url") &&  reader.peek() != JsonToken.NULL){
						if(mTrackObject!=null){
							mTrackObject.setArtworkUrl(reader.nextString());
						}
					}
					else if(name.equals("waveform_url") ){
						if(mTrackObject!=null){
							mTrackObject.setWaveForm(reader.nextString());
						}
					}
					else if(name.equals("playback_count")){
						if(mTrackObject!=null){
							mTrackObject.setPlaybackCount(reader.nextLong());
						}
					}
					else if(name.equals("favoritings_count")){
						if(mTrackObject!=null){
							mTrackObject.setFavoriteCount(reader.nextLong());
						}
					}
					else if(name.equals("comment_count")){
						if(mTrackObject!=null){
							mTrackObject.setCommentCount(reader.nextLong());
						}
					}
					else{
						reader.skipValue();
					}
				}
				return mTrackObject;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static ArrayList<TrackObject> parsingListTrackObject(InputStream in) {
		if (in == null) {
			new Exception(TAG + " data can not null").printStackTrace();
			return null;
		}
		else {
			try {
				JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
				ArrayList<TrackObject> listTrackObjects = new ArrayList<TrackObject>();
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					TrackObject mTrackObject= parsingNewTrackObject(reader);
					if (mTrackObject != null && mTrackObject.isStreamable()) {
						listTrackObjects.add(mTrackObject);
					}
					reader.endObject();
				}
				reader.endArray();
				reader.close();
				DBLog.d(TAG, "================>listTrackObjects size="+listTrackObjects.size());
				return listTrackObjects;

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (in != null) {
					try {
						in.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	public static ArrayList<TrackObject> parsingListTrackObject(String data) {
		if (!StringUtils.isEmptyString(data)) {
			try {
				JSONArray mJsonArray = new JSONArray(data);
				int size = mJsonArray.length();
				if(size>0){
					ArrayList<TrackObject> listTrackObjects = new ArrayList<TrackObject>();
					for(int i=0;i<size;i++){
						TrackObject mTrackObject = parsingTrackObject(mJsonArray.getJSONObject(i));
						if(mTrackObject!=null){
							listTrackObjects.add(mTrackObject);
						}
					}
					DBLog.d(TAG, "================>listTrackObjects size=" + listTrackObjects.size());
					return listTrackObjects;
				}

			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static CommentObject parsingCommentObject(JsonReader reader){
		if(reader!=null){
			try {
				CommentObject mCommentObject=null;
				while (reader.hasNext()) {
					String name = reader.nextName();
					if (name.equals("id")) {
						mCommentObject = new CommentObject();
						mCommentObject.setId(reader.nextLong());
					}
					else if(name.equals("created_at")){
						if(mCommentObject!=null){
							mCommentObject.setCreatedAt(reader.nextString());
						}
					}
					else if(name.equals("user_id")){
						if(mCommentObject!=null){
							mCommentObject.setUserId(reader.nextLong());
						}
					}
					else if(name.equals("track_id")){
						if(mCommentObject!=null){
							mCommentObject.setTrackid(reader.nextLong());
						}
					}
					else if(name.equals("timestamp") && reader.peek() != JsonToken.NULL){
						if(mCommentObject!=null){
							mCommentObject.setTimeStamp(reader.nextInt());
						}
					}
					else if(name.equals("body")){
						if(mCommentObject!=null){
							mCommentObject.setBody(reader.nextString());
						}
					}
					else if(name.equals("user")){
						reader.beginObject();
						while (reader.hasNext()) {
							String nameTagUser = reader.nextName();
							if(nameTagUser.equals("username")){
								if(mCommentObject!=null){
									mCommentObject.setUsername(reader.nextString());
								}
							}
							else if(nameTagUser.equals("avatar_url") &&  reader.peek() != JsonToken.NULL){
								if(mCommentObject!=null){
									mCommentObject.setAvatarUrl(reader.nextString());
								}
							}
							else{
								reader.skipValue();
							}
						}
						reader.endObject();
					}
					else{
						reader.skipValue();
					}
				}
				return mCommentObject;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static TrackObject parsingTrackObject(String data){
		if(!StringUtils.isEmptyString(data)){
			JSONObject mJsonObject;
			try {
				mJsonObject = new JSONObject(data);
				return parsingTrackObject(mJsonObject);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static ArrayList<CommentObject> parsingListCommentObject(InputStream in) {
		if (in == null) {
			new Exception(TAG + " data can not null").printStackTrace();
			return null;
		}
		else {
			try {
				JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
				ArrayList<CommentObject> listCommentObjects = new ArrayList<CommentObject>();
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					CommentObject mComment= parsingCommentObject(reader);
					if(mComment!=null){
						listCommentObjects.add(mComment);
					}
					reader.endObject();
				}
				reader.endArray();
				reader.close();
				DBLog.d(TAG, "================>listCommentObjects size="+listCommentObjects.size());
				return listCommentObjects;

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (in != null) {
					try {
						in.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
