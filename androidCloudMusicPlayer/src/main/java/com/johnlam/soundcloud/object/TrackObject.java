package com.johnlam.soundcloud.object;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentUris;
import android.net.Uri;

/**
 * 
 * TrackObject.java
 * 
 * @author :DOBAO
 * @Email :dotrungbao@gmail.com
 * @Skype :baopfiev_k50
 * @Phone :+84983028786
 * @Date :6 Oct, 2014
 * @project :Player de Musicas
 * @Package :com.ypyproductions.soundclound.object
 */
public class TrackObject {
	private long id;
	private Date createdDate;
	private long userId;
	private long duration;
	private String sharing;
	private String tagList;
	private boolean downloadable;
	private String genre;
	private String title;
	private String description;
	private String username;
	private String avatarUrl;
	private String permalinkUrl;
	private String artworkUrl;
	private String waveForm;
	private long playbackCount;
	private long favoriteCount;
	private long commentCount;

	private boolean isStreamable;
	private String linkStream;

	private boolean isLocalMusic;
	private String path;

	public TrackObject(long id, Date createdDate, long userId, long duration, String sharing, String tagList, String genre, String title, String description, String username,
			String avatarUrl, String permalinkUrl, String artworkUrl, String waveForm, long playbackCount, long favoriteCount, long commentCount) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.userId = userId;
		this.duration = duration;
		this.sharing = sharing;
		this.tagList = tagList;
		this.genre = genre;
		this.title = title;
		this.description = description;
		this.username = username;
		this.avatarUrl = avatarUrl;
		this.permalinkUrl = permalinkUrl;
		this.artworkUrl = artworkUrl;
		this.waveForm = waveForm;
		this.playbackCount = playbackCount;
		this.favoriteCount = favoriteCount;
		this.commentCount = commentCount;
	}

	public TrackObject() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getSharing() {
		return sharing;
	}

	public void setSharing(String sharing) {
		this.sharing = sharing;
	}

	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList = tagList;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getPermalinkUrl() {
		return permalinkUrl;
	}

	public void setPermalinkUrl(String permalinkUrl) {
		this.permalinkUrl = permalinkUrl;
	}

	public String getArtworkUrl() {
		return artworkUrl;
	}

	public void setArtworkUrl(String artworkUrl) {
		this.artworkUrl = artworkUrl;
	}

	public String getWaveForm() {
		return waveForm;
	}

	public void setWaveForm(String waveForm) {
		this.waveForm = waveForm;
	}

	public long getPlaybackCount() {
		return playbackCount;
	}

	public void setPlaybackCount(long playbackCount) {
		this.playbackCount = playbackCount;
	}

	public long getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(long favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isLocalMusic() {
		return isLocalMusic;
	}

	public void setLocalMusic(boolean isLocalMusic) {
		this.isLocalMusic = isLocalMusic;
	}

	public Uri getURI() {
		return ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
	}

	public JSONObject toLocalJsonObject() {
		JSONObject mJsonObject = new JSONObject();
		try {
			mJsonObject.put("id", id);
			mJsonObject.put("duration", duration);
			mJsonObject.put("title", title);
			mJsonObject.put("username", username);
			mJsonObject.put("description", description);
			mJsonObject.put("isLocalMusic", isLocalMusic);
			mJsonObject.put("path", path);
			return mJsonObject;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject toJsonObject() {
		JSONObject mJsonObject = new JSONObject();
		try {
			mJsonObject.put("id", id);
			mJsonObject.put("created_at", createdDate);
			mJsonObject.put("user_id", userId);
			mJsonObject.put("duration", duration);
			mJsonObject.put("sharing", sharing);
			mJsonObject.put("tag_list", tagList);
			mJsonObject.put("genre", genre);
			mJsonObject.put("title", title);
			mJsonObject.put("description", description);
			JSONObject mJsonObject2 = new JSONObject();
			mJsonObject2.put("username", username);
			mJsonObject2.put("avatar_url", avatarUrl);
			mJsonObject.put("user", mJsonObject2);
			mJsonObject.put("permalink_url", permalinkUrl);
			mJsonObject.put("artwork_url", artworkUrl == null ? "" : artworkUrl);
			mJsonObject.put("waveform_url", waveForm);
			mJsonObject.put("playback_count", playbackCount);
			mJsonObject.put("favoritings_count", favoriteCount);
			mJsonObject.put("comment_count", commentCount);

			return mJsonObject;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

	public boolean isStreamable() {
		return isStreamable;
	}

	public void setStreamable(boolean isStreamable) {
		this.isStreamable = isStreamable;
	}

	public String getLinkStream() {
		return linkStream;
	}

	public void setLinkStream(String linkStream) {
		this.linkStream = linkStream;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	public TrackObject clone() {
		return new TrackObject(id, createdDate, userId, duration, sharing, tagList, genre, title, description, username, avatarUrl, permalinkUrl, artworkUrl, waveForm,
				playbackCount, favoriteCount, commentCount);
	}
}
