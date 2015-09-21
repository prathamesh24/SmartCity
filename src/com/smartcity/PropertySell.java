package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartcity.adapters.BuyAdapter;
import com.smartcity.beans.BuyItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class PropertySell extends Fragment implements OnClickListener{

	View rootview;
	ArrayList<BuyItems> items;
	ListView list;
	Button addproperty;
	String propname,addresstxt,numbertxt,plocationtxt,areatxt,amounttxt;
	private static final String KEY_POSITION = "position";

	public static PropertySell newInstance(int position) {
		PropertySell frag = new PropertySell();
		Bundle args = new Bundle();

		args.putInt(KEY_POSITION, position);
		frag.setArguments(args);

		return (frag);
	}
	
	public static String getTitle(Context ctxt, int position) {
		return (String.format("Sell",position + 1));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.layout_property_sell, container, false);
		list=(ListView) rootview.findViewById(R.id.list);
		addproperty=(Button) rootview.findViewById(R.id.addproperty);
		
		addproperty.setOnClickListener(this);
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
		new GetPropertySell().execute();
		else{
			items=new Smartcitydatabase(getActivity()).getProperty("sell");
			list.setAdapter(new BuyAdapter(getActivity(), items));
		}
	}

		private class GetPropertySell extends AsyncTask<Void, Boolean, Boolean>{
				
				ProgressDialog dialg;
				ArrayList<NameValuePair>parameters;
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					dialg=new ProgressDialog(getActivity());
					dialg.setMessage("Loading...");
					dialg.setCancelable(false);
					dialg.show();
				}
		
				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub
					try {
						ServiceHandler handler=new ServiceHandler();
						
						parameters=new ArrayList<NameValuePair>();
						
						String regionString=new Smartcitydatabase(getActivity()).getRegion();
						parameters.add(new BasicNameValuePair("region",regionString));
						
						String response=handler.makeServiceCall(webservices.GetSellList,ServiceHandler.POST, parameters);
						items=new ArrayList<BuyItems>();
						JSONArray mainarray=new JSONArray(response);
						
						ArrayList<String>temparr=new Smartcitydatabase(getActivity()).getPropertyname("sell");
						
						for (int i = 0; i < mainarray.length(); i++) {
							BuyItems oneitem=new BuyItems();
							JSONObject obj=mainarray.getJSONObject(i);
							
							String title=obj.getString("title");
							String Image=obj.getString("Image");
							String location=obj.getString("location");
							String Mobile=obj.getString("Mobile");
							String amount=obj.getString("amount");
							
							oneitem.setImage(Image);
							oneitem.setTitle(title);
							oneitem.setLocation(location);
							oneitem.setMobile(Mobile);
							oneitem.setAmount(amount);
							
							items.add(oneitem);
							
							if(!temparr.contains(title)){
								if(new Smartcitydatabase(getActivity()).InsertProperties("sell", Image, title, location, Mobile, amount))
									Log.d("Data saved", "zala");
							}
							else {
								Log.d("Data saved", "ahe re");
							}
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
						list.setAdapter(new BuyAdapter(getActivity(), items));
					} catch (Exception e) {
						// TODO: handle exception
					}
					finally {
						if(dialg.isShowing())
							dialg.dismiss();
					}
				}
		}
		
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showInputDialog();
		}
		
		protected void showInputDialog() {

			// get prompts.xml view
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			View promptView = layoutInflater.inflate(R.layout.custom_add_property, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setView(promptView);

			final EditText name = (EditText) promptView.findViewById(R.id.name);
			final EditText address = (EditText) promptView.findViewById(R.id.address);
			final EditText number = (EditText) promptView.findViewById(R.id.number);
			final EditText plocation= (EditText) promptView.findViewById(R.id.plocation);
			final EditText area = (EditText) promptView.findViewById(R.id.area);
			final EditText amount = (EditText) promptView.findViewById(R.id.amount);
			
			// setup a dialog window
			alertDialogBuilder.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							propname=name.getText().toString();
							addresstxt=address.getText().toString();
							numbertxt=number.getText().toString();						
							plocationtxt=plocation.getText().toString();
							areatxt=area.getText().toString();
							amounttxt=amount.getText().toString();
							
							if(propname.length()!=0&&addresstxt.length()!=0&&numbertxt.length()!=0&&plocationtxt.length()!=0&&areatxt.length()!=0&&amounttxt.length()!=0){
								new EnterBlog().execute(propname,addresstxt,plocationtxt,numbertxt,areatxt,amounttxt);
 						    }
						}
					})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

			// create an alert dialog
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
		}
		
		private class EnterBlog extends AsyncTask<String, Boolean, Boolean>{
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
				protected Boolean doInBackground(String... params) {
					// TODO Auto-generated method stub
					try {
						ServiceHandler handler=new ServiceHandler();
						Smartcitydatabase db=new Smartcitydatabase(getActivity());
						parameters=new ArrayList<NameValuePair>();
						
						parameters.add(new BasicNameValuePair("region",db.getRegion()));
						parameters.add(new BasicNameValuePair("name",params[0]));
						parameters.add(new BasicNameValuePair("address",params[1]));
						parameters.add(new BasicNameValuePair("plocation",params[2]));
						parameters.add(new BasicNameValuePair("number",params[3]));
						parameters.add(new BasicNameValuePair("area",params[4]));
						parameters.add(new BasicNameValuePair("amount",params[5]));
						parameters.add(new BasicNameValuePair("image","https://cdn2.iconfinder.com/data/icons/metro-ui-dock/512/User_No-Frame.png"));
						
						String response=handler.makeServiceCall(webservices.AddProperty, ServiceHandler.POST, parameters);
						
						if(response.toString().equalsIgnoreCase("Data Insert Successfully"))
							return true;
						else
							return false;
						
					} catch (Exception e) {
						return false;
					}
					
				}
				@Override
				protected void onPostExecute(Boolean result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					try {
						if(result){
							Toast.makeText(getActivity(), "Property added sucessfully", Toast.LENGTH_LONG).show();
							if(items.size()!=0)
								items.clear();
							new GetPropertySell().execute();
						}
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
