package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.smartcity.adapters.ContactAdapter;
import com.smartcity.adapters.ServiceAdapter;
import com.smartcity.beans.ContactsItems;
import com.smartcity.beans.ServiceItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
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
import android.widget.Toast;

public class Emergency extends Fragment implements OnItemClickListener{
	View rootview;
	ListView list;
	ArrayList<ContactsItems>contacts;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.layout_contacts, container,false);
		list=(ListView) rootview.findViewById(R.id.listview);
		list.setOnItemClickListener(this);
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		SharedPreferences preff = getActivity().getSharedPreferences("DrawerClick", Context.MODE_PRIVATE);
		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
			new GetContacts().execute(new String[]{"emergency",new Smartcitydatabase(getActivity()).getRegion(),"emergency"});
		}
		else {
			contacts=new Smartcitydatabase(getActivity()).getContacts("emergency");
			list.setAdapter(new ContactAdapter(getActivity(),contacts));
		}
		
		
	}
	
	private class GetContacts extends AsyncTask<String, Boolean, Boolean>{
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
				parameters.add(new BasicNameValuePair("region",params[1]));
				
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.emergency, ServiceHandler.POST, parameters);
				JSONArray mainarray=new JSONArray(response);
				contacts=new ArrayList<ContactsItems>();
				
				ArrayList<String>temparr=new Smartcitydatabase(getActivity()).getContactNumber(params[0]);
				
				for (int i = 0; i <mainarray.length(); i++) {
					ContactsItems contact=new ContactsItems();
					
					JSONObject mainobj=mainarray.getJSONObject(i);
					
					String Name=mainobj.getString("Name");
					String Image=mainobj.getString("Image");
					String Adress=mainobj.getString("Adress");
					String contactnumber=mainobj.getString("contactnumber");
					String website=mainobj.getString("website");
					
					contact.setName(Name);
					contact.setImage(Image);
					contact.setAdress(Adress);
					contact.setContactnumber(contactnumber);
					contact.setWebsite(website);
					
					contacts.add(contact);
					
					if(!temparr.contains(contactnumber)){
						if(new Smartcitydatabase(getActivity()).InsertContacts(params[0], Name, Image, Adress, contactnumber, website))
						{
							Log.d("data saved zala","re");
						}
					}
					else{
						Log.d("data saved ","ahe re");
					}
					
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
					if(contacts.size()!=0)
					{
						list.setAdapter(new ContactAdapter(getActivity(),contacts));
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
				ContactsItems row=contacts.get(inddex);
				new GetCallContact().execute(new String[]{row.getContactnumber()});
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

}
