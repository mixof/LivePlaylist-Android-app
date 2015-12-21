package com.example.liveplaylist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Tracklist extends SwipeRefreshListFragment {

	ArrayList<Track> tracks = new ArrayList<Track>();
	TracksAdapter adapter;
	ImageView play;
	String idd;
	private boolean loadnexdata = false;
	View footer;
	int mLastFirstVisibleItem = 0, visibleItemCount = 0, firstVisibleItem = 0, totalItemCount = 0;
	DbHelper database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (getArguments() != null)
			idd = getArguments().getString("id");

		Log.d("listfrag", "create");

		footer = getActivity().getLayoutInflater().inflate(R.layout.tracks_footer, null, false);
		footer.setVisibility(View.INVISIBLE);

		adapter = new TracksAdapter(getActivity());
		GetTracklist(idd, 0, true);
		database = DbHelper.getInstance(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		getListView().addFooterView(footer);

		getListView().setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				final int currentFirstVisibleItem = view.getFirstVisiblePosition();
				if (currentFirstVisibleItem > mLastFirstVisibleItem) {

					if (visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount) {

						GetNextTracks(idd, totalItemCount);

					}

				}
				mLastFirstVisibleItem = currentFirstVisibleItem;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				Tracklist.this.firstVisibleItem = firstVisibleItem;
				Tracklist.this.visibleItemCount = visibleItemCount;
				Tracklist.this.totalItemCount = totalItemCount;

			}
		});

		setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

				initiateRefresh();
			}
		});

	}

	private void initiateRefresh() {
		Log.i("refresh", "initiateRefresh");

		/**
		 * Execute the background task, which uses {@link android.os.AsyncTask}
		 * to load the data.
		 */

		GetTracklist(idd, 0, false);
	}

	private void onRefreshComplete() {
		Log.i("refresh", "onRefreshComplete");

		// Stop the refreshing indicator
		Toast.makeText(getActivity(), "Обновление завершено", Toast.LENGTH_SHORT).show();
		deleteOldTracks();	
		setRefreshing(false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Track current = adapter.getTrack(position);

		Detailtrack detail = new Detailtrack();
		Bundle data = new Bundle();
		data.putString("id", current.getTrack_id());
		data.putString("artist", current.getArtist());
		data.putString("name", current.getTrack_name());
		data.putString("url", current.getUrl());
		detail.setArguments(data);

		FragmentTransaction fTrans = getActivity().getSupportFragmentManager().beginTransaction();

		fTrans.replace(R.id.fragments_container, detail);
		fTrans.addToBackStack("detail");
		fTrans.commit();

		// getActivity().showDialog(1);
	}

	/*
	 * Gettracklist action: 0 - first load tracks | 1 - update tracklist | 2 -
	 * get next tracks
	 */
	public void GetTracklist(String company_id, int position, final boolean isfirstload) {

		LivePlaylistRest.get("tracks/id/" + company_id + "/pos/" + position, null, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				// TODO Auto-generated method stub

				super.onSuccess(statusCode, headers, response);
				Log.d("response", "succes: " + String.valueOf(statusCode));

				try {
					adapter.Clear();

					for (int i = 0; i < response.length(); i++) {
						JSONObject track = response.getJSONObject(i);

						adapter.Add(new Track(track.getString("track_id"), track.getString("artist_name"),
								track.getString("track_name"),
								track.getString("track_url").replace("http://", "http://10.0.2.2/")));
					}

					if (isfirstload)
						setListAdapter(adapter);
					else {

						onRefreshComplete();
						adapter.notifyDataSetChanged();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (!isfirstload)
					onRefreshComplete();
				
				Log.d("response", "fail: " + String.valueOf(statusCode));

				try {
					String error = errorResponse.getString("error");
					if (error != null)
						Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getActivity(), "Не удалось соедениться..", Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

		});

	}

	public void GetNextTracks(String company_id, int position) {

		if (!loadnexdata) {
			Log.d("scroll", "Подгрузка новых");
			footer.setVisibility(View.VISIBLE);
			loadnexdata = true;

			LivePlaylistRest.get("tracks/id/" + company_id + "/pos/" + position, null, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
					// TODO Auto-generated method stub

					super.onSuccess(statusCode, headers, response);
					Log.d("response", "succes: " + String.valueOf(statusCode));

					try {

						for (int i = 0; i < response.length(); i++) {
							JSONObject track = response.getJSONObject(i);

							adapter.Add(new Track(track.getString("track_id"), track.getString("artist_name"),
									track.getString("track_name"),
									track.getString("track_url").replace("http://", "http://10.0.2.2/")));
						}

						loadnexdata = false;
						adapter.notifyDataSetChanged();
						footer.setVisibility(View.INVISIBLE);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
					loadnexdata = false;
					footer.setVisibility(View.INVISIBLE);

				}

			});

		} // end if(loadnexdata)

	}

	public void deleteOldTracks() {
		long timenow = System.currentTimeMillis() - (long) 300000;

		database.delTracks(timenow);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("listfrag", "destriy");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("listfrag", "resume");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

		Log.d("listfrag", "detach");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.d("listfrag", "stop");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("listfrag", "start");
	}

}
