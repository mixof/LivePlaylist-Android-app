package com.example.liveplaylist;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.widget.Toast;

public class LivePlaylistRestClient {

	private Context context = null;
    Company company = null;
	public LivePlaylistRestClient(Context ctx) {
		// TODO Auto-generated constructor stub

		this.context = ctx;
	}

	public Company getCompanyInfo(String company_id) throws JSONException {

	

		LivePlaylistRest.get("company/id/" + company_id, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				if (statusCode == 200) {
					try {

						String id = response.getString("company_id");
						String name=response.getString("company_name");
						String desc=response.getString("company_description");
						String adres=response.getString("company_adress");
						String logo=response.getString("company_logo");
						String phone=response.getString("company_phone");
						String cat=response.getString("category_name");
						
						
						company=new Company(id, name, desc, logo, adres, phone, cat);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else company=null;

			}

		});

		return company;
	}

}
