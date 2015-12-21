package com.example.liveplaylist;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Login_fragment extends android.support.v4.app.Fragment implements OnClickListener {

	EditText company_id, code_word;
	TextView tvIsConnected;
	Button btn;
	ProgressBar progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.main_page, null);
		company_id = (EditText) v.findViewById(R.id.editText1);
		code_word = (EditText) v.findViewById(R.id.editText2);
		tvIsConnected = (TextView) v.findViewById(R.id.textView3);
		btn = (Button) v.findViewById(R.id.button1);
		btn.setOnClickListener(this);	
		progress=(ProgressBar)v.findViewById(R.id.progressBar1);

		if (isConnected()) {
			tvIsConnected.setTextColor(0xFF00CC00);
			tvIsConnected.setText("You are conncted");
			tvIsConnected.setVisibility(View.INVISIBLE);
		} else {
			tvIsConnected.setText("You are NOT conncted");
		}

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	
        
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getActivity()
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	public void GetCompanyJson(String company_id, String code_word) {
		

		LivePlaylistRest.get("company/id/" + company_id+"/code/"+code_word, null, new JsonHttpResponseHandler() {
       
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.d("response", "start");
				progress.setVisibility(View.VISIBLE);
				btn.setEnabled(false);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.d("response", "finish");
				progress.setVisibility(View.INVISIBLE);
				btn.setEnabled(true);
			}
			
			
		
			
		
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				
				super.onSuccess(statusCode, headers, response);
				Log.d("response", "succes: "+String.valueOf(statusCode));
				
				try {
					String id = response.getString("company_id");
					String name = response.getString("company_name");
					String desc = response.getString("company_description");
					String adres = response.getString("company_adress");
					String logo = response.getString("company_logo");
					String phone = response.getString("company_phone");
					String cat = response.getString("category_name");

					
					if(id!=null) StartTracksFragment(id);
					

				} catch (JSONException e) {
					e.printStackTrace();
				}

				// new Company(id, name, desc, logo, adres, phone, cat);
				
				
			}			
		

			 @Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				//super.onFailure(statusCode, headers, throwable, errorResponse);
		
				Log.d("response", "fail: "+String.valueOf(statusCode));

				try {
					String error=errorResponse.getString("error");
					if (error!= null)
						Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getActivity(), "Не удалось соедениться..", Toast.LENGTH_SHORT).show();
				
				} catch (JSONException e) {

					e.printStackTrace();
				}
				

			}
			
			@Override
			public void onRetry(int retryNo) {
				// TODO Auto-generated method stub
				super.onRetry(retryNo);
				Log.d("response", "retry");
			}
			
			
			

		});
	}
	
	
	public void StartTracksFragment(String company_id)
	{
		
		FragmentTransaction fTrans = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
		Tracklist tracks=new Tracklist();
	      Bundle args=new Bundle();
	      args.putString("id",company_id);
	      tracks.setArguments(args);

	      fTrans.replace(R.id.fragments_container, tracks);				     
	      fTrans.commit();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String id = company_id.getText().toString();
        String word= code_word.getText().toString();
		if (id.length() > 0) {
			// Toast.makeText(this, company_id.getText(),
			// Toast.LENGTH_SHORT).show();
			if (word.length() > 0) 
			{
			 GetCompanyJson(id,word);
			}
			else
			{
				Toast.makeText(getActivity(), "Введите кодовое слово", Toast.LENGTH_SHORT).show();
			}
	
         

		} else {
			Toast.makeText(getActivity(), "Введите ID компании", Toast.LENGTH_SHORT).show();
		}
	}

}
