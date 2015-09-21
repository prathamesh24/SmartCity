package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartcity.adapters.MediaAdapter;
import com.smartcity.beans.MediaItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Media extends Fragment {

	View rootview;
	ArrayList<MediaItems>medias;
	ListView listview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.activity_media, container, false);
		listview=(ListView) rootview.findViewById(R.id.medialist);
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		new GetMedia().execute();
	}
	
	private class GetMedia extends AsyncTask<Void, Boolean, Boolean>{
		ArrayList<NameValuePair>parameters;
		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(getActivity());
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				parameters=new ArrayList<NameValuePair>();
				
				String regionString=new Smartcitydatabase(getActivity()).getRegion();
				
				parameters.add(new BasicNameValuePair("region", regionString));
				parameters.add(new BasicNameValuePair("category", "Media"));
				
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.SERVICEAPI,ServiceHandler.POST, parameters);
				
				JSONArray mainarray=new JSONArray(response);
				medias=new ArrayList<MediaItems>();
				for (int i = 0; i < mainarray.length(); i++) {
					MediaItems onemedia=new MediaItems();
					JSONObject innerobj=mainarray.getJSONObject(i);
					onemedia.setImage(innerobj.getString("image"));
					onemedia.setTitle(innerobj.getString("name"));
					
					medias.add(onemedia);
				}
				
				return true;
				
			} catch (Exception e) {
				return false;
			}
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				listview.setAdapter(new MediaAdapter(getActivity(), medias));
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally {
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
		}

		
	}
}
