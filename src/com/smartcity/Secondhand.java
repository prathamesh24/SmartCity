package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartcity.adapters.BuyAdapter;
import com.smartcity.adapters.SecondhandAdapter;
import com.smartcity.beans.BuyItems;
import com.smartcity.beans.SecondhandItems;
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

public class Secondhand extends Fragment {

	View rootview;
	ArrayList<SecondhandItems>items;
	ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.layout_secondhand, container, false);
		list=(ListView) rootview.findViewById(R.id.listview);
		return rootview;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		new GetSecondHandData().execute();
	}

	private class GetSecondHandData extends AsyncTask<Void, Boolean, Boolean> {

		ProgressDialog dialg;
		ArrayList<NameValuePair> parameters;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialg = new ProgressDialog(getActivity());
			dialg.setMessage("Loading...");
			dialg.setCancelable(false);
			dialg.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				ServiceHandler handler = new ServiceHandler();

				parameters = new ArrayList<NameValuePair>();

				String regionString = new Smartcitydatabase(getActivity()).getRegion();
				parameters.add(new BasicNameValuePair("region", regionString));

				String response = handler.makeServiceCall(webservices.GetSecondHand, ServiceHandler.POST, parameters);
				items = new ArrayList<SecondhandItems>();
				JSONArray mainarray = new JSONArray(response);

				for (int i = 0; i < mainarray.length(); i++) {
					SecondhandItems oneitem = new SecondhandItems();
					JSONObject obj = mainarray.getJSONObject(i);
					oneitem.setImage(obj.getString("Image"));
					oneitem.setName(obj.getString("name"));
					oneitem.setAddress(obj.getString("address"));
					oneitem.setMobile(obj.getString("contactnumber"));

					items.add(oneitem);
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
				list.setAdapter(new SecondhandAdapter(getActivity(), items));
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (dialg.isShowing())
					dialg.dismiss();
			}
		}

	}
}
