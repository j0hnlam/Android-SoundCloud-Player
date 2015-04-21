package com.johnlam.soundcloud.object;

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
public class CommentObject {
	private long id;
	private String createdAt;
	private long userId;
	private long trackid;
	private int timeStamp;
	private String body;
	private String username;
	private String avatarUrl;

	public CommentObject(long id, String createdAt, long userId, long trackid, int timeStamp, String body, String username, String avatarUrl) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.userId = userId;
		this.trackid = trackid;
		this.timeStamp = timeStamp;
		this.body = body;
		this.username = username;
		this.avatarUrl = avatarUrl;
	}
	

	public CommentObject() {
		super();
	}



	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getTrackid() {
		return trackid;
	}

	public void setTrackid(long trackid) {
		this.trackid = trackid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

}
