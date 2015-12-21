package com.example.liveplaylist;

public class Track {
	
	private String track_id;
	private String artist_name;
	private String track_name;
	private String url;
	
	
	Track(String id,String artist, String track, String track_url)
	{
		this.setTrack_id(id);
		this.setArtist(artist);
		this.setTrack_name(track);
		this.setUrl(track_url);
	}


	public String getArtist() {
		return artist_name;
	}


	public void setArtist(String artist_name) {
		this.artist_name = artist_name;
	}


	public String getTrack_name() {
		return track_name;
	}


	public void setTrack_name(String track_name) {
		this.track_name = track_name;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getTrack_id() {
		return track_id;
	}


	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

}
