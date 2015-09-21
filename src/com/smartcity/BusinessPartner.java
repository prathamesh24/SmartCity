package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.smartcity.adapters.BusinesslistAdapter;
import com.smartcity.adapters.Poliitianadapter;
import com.smartcity.beans.BusinessItems;
import com.smartcity.beans.ContactsItems;
import com.smartcity.beans.PoliticsItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BusinessPartner extends Fragment implements OnItemClickListener{

	View rootview;
	ListView listview;
	ArrayList<BusinessItems>items;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.layout_businesslist, container, false);
		listview=(ListView) rootview.findViewById(R.id.listview);
		listview.setOnItemClickListener(this);
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		new GetPolitics().execute();
	}
	
	private class GetPolitics extends AsyncTask<Void, Boolean, Boolean>{
		ProgressDialog dialog;
		ArrayList<NameValuePair>parameters;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(getActivity());
			dialog.setMessage("Loading..");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				parameters=new ArrayList<NameValuePair>();
				
				parameters.add(new BasicNameValuePair("region", new Smartcitydatabase(getActivity()).getRegion()));
				
				items=new ArrayList<BusinessItems>();
				
				String response=new ServiceHandler().makeServiceCall(webservices.GetBusinessPartners,ServiceHandler.POST, parameters);
				
				JSONArray mainarray=new JSONArray(response);
				
				for (int i = 0; i < mainarray.length(); i++) {
					JSONObject obj=mainarray.getJSONObject(i);
					BusinessItems pol=new BusinessItems();
					pol.setImage(obj.getString("Image"));
					pol.setName(obj.getString("Name"));
					pol.setAddress(obj.getString("Address"));
					pol.setMobile(obj.getString("Mobile"));
					items.add(pol);				
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
				if(result==true)
					listview.setAdapter(new BusinesslistAdapter(getActivity(), items));
					
			} catch (Exception e) {
				// TODO: handle exception
			}finally {
				if(dialog.isShowing())
					dialog.dismiss();
			}
		}
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		final int inddex=position;
		AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
		alert.setMessage("Are you sure to continue for call?");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				BusinessItems row=items.get(inddex);
				new GetCallContact().execute(new String[]{row.getMobile()});
			}
		});
		alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		AlertDialog dial=alert.create();
		dial.show();
		
	}
	
	private class GetCallContact extends AsyncTask<String, Boolean, Boolean>{
		ProgressDialog dialog;
		ArrayList<NameValuePair>parameters;
		String phonenumber;
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
				parameters.add(new BasicNameValuePair("region",new Smartcitydatabase(getActivity()).getRegion()));
				parameters.add(new BasicNameValuePair("number",params[0]));
				parameters.add(new BasicNameValuePair("unumber",new Smartcitydatabase(getActivity()).getNumber()));
				parameters.add(new BasicNameValuePair("name",new Smartcitydatabase(getActivity()).getName()));
				
				phonenumber=params[0];
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.callerAPI, ServiceHandler.POST, parameters);
			
				if(response.toString().equalsIgnoreCase("Data Insert Successfully")){
					return true;
				}
				else{
					return false;
				}
				
				
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
					Toast.makeText(getActivity(),"Data has been entered", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:"+phonenumber.trim()));
					startActivity(intent); 
				}
				else{
					Toast.makeText(getActivity(),"Network error occured", Toast.LENGTH_LONG).show();
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
}
