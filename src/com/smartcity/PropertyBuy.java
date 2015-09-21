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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PropertyBuy extends Fragment{

	View rootview;
	ListView list;
	ArrayList<BuyItems>items;
	
	private static final String KEY_POSITION = "position";

	public static PropertyBuy newInstance(int position) {
		PropertyBuy frag = new PropertyBuy();
		Bundle args = new Bundle();

		args.putInt(KEY_POSITION, position);
		frag.setArguments(args);

		return (frag);
	}
	
	public static String getTitle(Context ctxt, int position) {
		return (String.format("Buy",position + 1));
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.layout_propertybuy,container,false);
		list=(ListView) rootview.findViewById(R.id.listview  );
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
		new GetPropertyBuy().execute();
		else{
			items=new Smartcitydatabase(getActivity()).getProperty("buy");
			list.setAdapter(new BuyAdapter(getActivity(), items));
		}
	}
	
	private class GetPropertyBuy extends AsyncTask<Void, Boolean, Boolean>{
		
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
				
				String response=handler.makeServiceCall(webservices.GetBuyList,ServiceHandler.POST, parameters);
				items=new ArrayList<BuyItems>();
				JSONArray mainarray=new JSONArray(response);
				
				ArrayList<String>temparr=new Smartcitydatabase(getActivity()).getPropertyname("buy");
				
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
						if(new Smartcitydatabase(getActivity()).InsertProperties("buy", Image, title, location, Mobile, amount))
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
}
