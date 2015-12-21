package com.example.liveplaylist;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TracksAdapter extends BaseAdapter {

	Context ctx;
	LayoutInflater lInflater;
	ArrayList<Track> tracks;
	DbHelper database;

	TracksAdapter(Context context, ArrayList<Track> tracks) {
		this.ctx = context;
		this.tracks = tracks;
		this.lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		database = DbHelper.getInstance(context);
	}

	TracksAdapter(Context context) {
		this.ctx = context;
		this.tracks = new ArrayList<Track>();
		this.lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		database = DbHelper.getInstance(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.tracks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.tracks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void Clear() {
		this.tracks.clear();
	}

	public void Add(Track t) {

		if (t != null) {
			this.tracks.add(t);
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.listitem_view, parent, false);
		}

		final Track track = getTrack(position);

		((TextView) view.findViewById(R.id.artist)).setText(track.getArtist());
		((TextView) view.findViewById(R.id.track_name)).setText(track.getTrack_name());
		final ImageView image = ((ImageView) view.findViewById(R.id.imageView1));
		image.setBackgroundResource(R.drawable.rate);
		image.setTag(null);
		
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (v.getTag() == null) {
					Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.button_rotate);

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

							
							Cursor cursor = database.getTrack(track.getTrack_id());

							if (cursor.getCount()>0) {
						
								if ((System.currentTimeMillis()- cursor.getLong(cursor.getColumnIndex(DbHelper.KEY_TRACK_CHECKDATE))) > (long)300000) {
									Log.d("tttt", "track deleted, update started..");
									image.setBackgroundResource(R.drawable.rate_grey);
									v.setTag(1);									
									updateRating(track.getTrack_id(),true);
									

								} else {
									
									Toast.makeText(ctx, "Вы недавно заказывали этот трек! Попробуйте позднее..", Toast.LENGTH_SHORT).show();
								}

							} else {
								
								image.setBackgroundResource(R.drawable.rate_grey);
								v.setTag(1);
								Log.d("tttt", "only update started..");
								updateRating(track.getTrack_id(),false);
								
								
							}
							
							cursor.close();

						}
					});
					// запуск анимации
					image.startAnimation(animation);

				}
			}
		});

		return view;
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
				
				Toast.makeText(ctx, "Трек заказан!", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);

				Log.d("response", "fail: " + String.valueOf(statusCode));
				Toast.makeText(ctx, "Невышло:(", Toast.LENGTH_SHORT).show();

			}

		});
	}

	Track getTrack(int position) {
		return ((Track) this.getItem(position));
	}

}
