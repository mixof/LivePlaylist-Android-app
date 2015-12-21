package com.example.liveplaylist;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class PlayTrackClass implements OnPreparedListener, OnCompletionListener {

	private MediaPlayer mediaPlayer;
	private AudioManager am;
	private Context context;

	public PlayTrackClass(Context context) {
		if (context != null) {
			this.context = context;
			this.am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);

		}
	}

	public void play_track(String url) {
		releaseMP();
		mediaPlayer = new MediaPlayer();

		try {
			mediaPlayer.setDataSource(url);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.prepareAsync();	
		mediaPlayer.setOnPreparedListener(this);

	}

	public void stop_track() {
		mediaPlayer.reset();
	}

	public void releaseMP() {
		if (mediaPlayer != null) {
			try {
				mediaPlayer.release();
				mediaPlayer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	
}
