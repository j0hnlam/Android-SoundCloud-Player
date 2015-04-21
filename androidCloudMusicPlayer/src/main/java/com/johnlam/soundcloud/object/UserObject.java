package com.johnlam.soundcloud.object;

/**
 * 
 * UserObject.java
 * 
 * @author :DOBAO
 * @Email :dotrungbao@gmail.com
 * @Skype :baopfiev_k50
 * @Phone :+84983028786
 * @Date :6 Oct, 2014
 * @project :Player de Musicas
 * @Package :com.ypyproductions.soundclound.object
 */
public class UserObject {

	private long id;
	private String permalink;
	private String username;
	private String permalinkUrl;
	private String avatarUrl;
	private String country;
	private String fullName;
	private String description;
	private String city;
	private int trackCount;
	private int playlistCount;
	private int followers;
	private int following;

	public UserObject(long id, String permalink, String username, String permalinkUrl, String avatarUrl, String country, String fullName, String description, String city,
			int trackCount, int playlistCount, int followers, int following) {
		super();
		this.id = id;
		this.permalink = permalink;
		this.username = username;
		this.permalinkUrl = permalinkUrl;
		this.avatarUrl = avatarUrl;
		this.country = country;
		this.fullName = fullName;
		this.description = description;
		this.city = city;
		this.trackCount = trackCount;
		this.playlistCount = playlistCount;
		this.followers = followers;
		this.following = following;
	}

	public UserObject(long id, String permalink, String username, String permalinkUrl, String avatarUrl) {
		super();
		this.id = id;
		this.permalink = permalink;
		this.username = username;
		this.permalinkUrl = permalinkUrl;
		this.avatarUrl = avatarUrl;
	}

	public UserObject() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPermalinkUrl() {
		return permalinkUrl;
	}

	public void setPermalinkUrl(String permalinkUrl) {
		this.permalinkUrl = permalinkUrl;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	public int getPlaylistCount() {
		return playlistCount;
	}

	public void setPlaylistCount(int playlistCount) {
		this.playlistCount = playlistCount;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

}
