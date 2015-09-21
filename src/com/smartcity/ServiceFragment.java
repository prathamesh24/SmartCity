package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartcity.adapters.ServiceAdapter;
import com.smartcity.beans.ServiceItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class ServiceFragment extends Fragment implements OnItemClickListener{
	
	View rootview;
	//ListView list;
	GridView gridlist;
	ArrayList<ServiceItems>services;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		rootview=inflater.inflate(R.layout.layout_services, container,false);
		
//		list=(ListView) rootview.findViewById(R.id.listview);
//		list.setOnItemClickListener(this);
		
		gridlist=(GridView) rootview.findViewById(R.id.gridView1);
		gridlist.setOnItemClickListener(this);
		
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		SharedPreferences preff=getActivity().getSharedPreferences("DrawerClick",Context.MODE_PRIVATE);
		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
			new Getservices().execute(new String[]{preff.getString("category",""),new Smartcitydatabase(getActivity()).getRegion()});
		}
		else {
			services=new Smartcitydatabase(getActivity()).getServices(preff.getString("category",""));
			gridlist.setAdapter(new ServiceAdapter(getActivity(),services));
		}
		
	}
	
	private class Getservices extends AsyncTask<String, Boolean, Boolean>{
		ProgressDialog dialog;
		ArrayList<NameValuePair>parameters;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(getActivity());
			dialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				parameters=new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("category",params[0]));
				parameters.add(new BasicNameValuePair("region",params[1]));
				
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.SERVICEAPI, ServiceHandler.POST, parameters);
				JSONArray mainarray=new JSONArray(response);
				services=new ArrayList<ServiceItems>();
				
				ArrayList<String>temparray=new Smartcitydatabase(getActivity()).getServiceNames(params[0]);
				
				for (int i = 0; i <mainarray.length(); i++) {
					ServiceItems oneservice=new ServiceItems();
					
					JSONObject mainobj=mainarray.getJSONObject(i);
					
					String serviceimage=mainobj.getString("image");
					oneservice.setImage(serviceimage);
					String servicename=mainobj.getString("name");
					oneservice.setCategory(servicename);
					
					if(!temparray.contains(servicename)){
						
						if(new Smartcitydatabase(getActivity()).InsertServices(params[0], serviceimage, servicename))
							Log.d("data ", "saved");
						
					}else {
						Log.d("data ahere", "ahe");
					}
					
					services.add(oneservice);
				}
						
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				if(result==true){
					if(services.size()!=0)
					{
						gridlist.setAdapter(new ServiceAdapter(getActivity(),services));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
			
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		SharedPreferences preff = getActivity().getSharedPreferences("DrawerClick", Context.MODE_PRIVATE);
		Editor editor = preff.edit();
		ServiceItems row=services.get(position);
		editor.putString("service",row.getCategory());
		editor.commit();
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Subservice()).addToBackStack("").commit();
	
	}
}
