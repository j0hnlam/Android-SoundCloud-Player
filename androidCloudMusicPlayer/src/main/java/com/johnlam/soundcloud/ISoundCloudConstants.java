package com.johnlam.soundcloud;

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
public interface ISoundCloudConstants {
	
	public static final String URL_API="http://api.soundcloud.com/";
	public static final String METHOD_USER="users";
	public static final String METHOD_TRACKS="tracks";
	public static final String METHOD_COMMENTS="comments";
	public static final String METHOD_PLAYLISTS="playlists";
	public static final String METHOD_ME="me";
	
	public static final String FORMAT_CLIENT_ID="?client_id=%1$s";
	public static final String JSON_PREFIX=".json";
	
	public static final String OFFSET="&offset=%1$s&limit=%2$s";
	
	public static final String FILTER_QUERY="&q=%1$s";
	public static final String FILTER_GENRE="&genres=%1$s";
	public static final String FILTER_TYPES="&types=%1$s";
	public static final String FILTER_LICENSE="&license=%1$s";
	
	public static final String FORMAT_URL_SONG = "http://api.soundcloud.com/tracks/%1$s/stream?client_id=%2$s";
	public static final String FORMAT_URL_DOWNLOAD_SONG = "https://api.soundcloud.com/tracks/%1$s/stream?client_id=%2$s";


}
