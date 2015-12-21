package com.example.liveplaylist;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Detailtrack extends Fragment {

	private String track_id;
	private String artist;
	private String name;
	private String url;

	TextView artist_view, name_view;
	ImageView play, rate_up;
	PlayTrackClass player;
	DbHelper database;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.detailtrack_view, container, false);

		artist_view = (TextView) v.findViewById(R.id.detail_artist);
		name_view = (TextView) v.findViewById(R.id.detail_track_name);
		play = (ImageView) v.findViewById(R.id.imageView2);
		rate_up = (ImageView) v.findViewById(R.id.imageView1);
		rate_up.setBackgroundResource(R.drawable.rate);
		play.setBackgroundResource(R.drawable.play_btn);
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.getTag() == null) {
					v.setBackgroundResource(R.drawable.stop);
					v.setTag(1);
					player.play_track(url);

				} else {
					player.stop_track();
					v.setBackgroundResource(R.drawable.play_icon);
					v.setTag(null);

				}
			}

		});
		
		rate_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				if (v.getTag() == null) {
					Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_rotate);

					animation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub

							
							Cursor cursor = database.getTrack(track_id);

							if (cursor.getCount()>0) {
						
								if ((System.currentTimeMillis()- cursor.getLong(cursor.getColumnIndex(DbHelper.KEY_TRACK_CHECKDATE))) > (long)300000) {
									Log.d("tttt", "track deleted, update started..");
									v.setBackgroundResource(R.drawable.rate_grey);
									v.setTag(1);									
									updateRating(track_id,true);
									

								} else {
									
									Toast.makeText(getActivity(), "Вы недавно заказывали этот трек! Попробуйте позднее..", Toast.LENGTH_SHORT).show();
								}

							} else {
								
								v.setBackgroundResource(R.drawable.rate_grey);
								v.setTag(1);
								Log.d("tttt", "only update started..");
								updateRating(track_id,false);
								
								
							}
							
							cursor.close();

						}
					});
					// запуск анимации
					v.startAnimation(animation);
					
				

						
				}
			}
		});

		artist_view.setText(artist);
		name_view.setText(name);

		return v;
	}
	
	
	private void updateRating(final String track_id, final boolean isupdate) {

		RequestParams params = new RequestParams();
		params.put("id", track_id);
		LivePlaylistRest.update("track", params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				Log.d("tttt", "update complete: " + String.valueOf(statusCode));
				
				if(isupdate)database.updateTrack(track_id,System.currentTimeMillis());
				else database.addTrack(track_id, System.currentTimeMillis());
				
				Toast.makeText(getActivity(), "Трек заказан!", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);

				Log.d("response", "fail: " + String.valueOf(statusCode));
				Toast.makeText(getActivity(), "Невышло:(", Toast.LENGTH_SHORT).show();

			}

		});
	}

		

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		database = DbHelper.getInstance(getActivity());
		if (getArguments() != null) {

			track_id = getArguments().getString("id");
			artist = getArguments().getString("artist");
			name = getArguments().getString("name");
			url = getArguments().getString("url");

		}

		player = new PlayTrackClass(getContext());

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.releaseMP();
	}

}
