package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartcity.adapters.Poliitianadapter;
import com.smartcity.adapters.ServiceAdapter;
import com.smartcity.beans.PoliticsItems;
import com.smartcity.beans.ServiceItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Politian_list extends Fragment implements OnItemClickListener{

	ArrayList<PoliticsItems>poilitics;
	View rootview;
	GridView gridlist;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.activity_politian_list,container,false);
		gridlist=(GridView) rootview.findViewById(R.id.gridView1);
		gridlist.setOnItemClickListener(this);
		
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
				
				parameters.add(new BasicNameValuePair("region",new Smartcitydatabase(getActivity()).getRegion()));
				
				poilitics=new ArrayList<PoliticsItems>();
				
				String response=new ServiceHandler().makeServiceCall(webservices.PolititianList,ServiceHandler.POST, parameters);
				
				JSONArray mainarray=new JSONArray(response);
				
				for (int i = 0; i < mainarray.length(); i++) {
					JSONObject obj=mainarray.getJSONObject(i);
					PoliticsItems pol=new PoliticsItems();
					pol.setImage(obj.getString("Image"));
					pol.setParty(obj.getString("Party"));			
					poilitics.add(pol);				
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
					gridlist.setAdapter(new MyAdapter(getActivity(),poilitics));
					
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
		PoliticsItems row=poilitics.get(position);
		SharedPreferences pre=getActivity().getSharedPreferences("political",0);
		Editor editor=pre.edit();
		editor.putString("party",row.getParty());
		editor.commit();
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new Polititianinner()).addToBackStack("").commit();
	}
	
	public class MyAdapter extends BaseAdapter{
		Context context;
		ArrayList<PoliticsItems>services;
		
		public MyAdapter(Context context, ArrayList<PoliticsItems> services) {
			this.context = context;
			this.services = services;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return services.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return services.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return services.indexOf(services.get(position));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				LayoutInflater inflator=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				convertView=inflator.inflate(R.layout.custom_grid, null);
			}
			
			ImageView image=(ImageView) convertView.findViewById(R.id.imageView1);
			TextView title=(TextView) convertView.findViewById(R.id.textView1);
			
			PoliticsItems row_pos=services.get(position);
			
			Picasso.with(context)
	        .load(row_pos.getImage())
	        .placeholder(R.drawable.stub) // optional
	        .error(R.drawable.stub)         // optional
	        .into(image);
			
			
			title.setText(row_pos.getParty());
			
			return convertView;
		}

	}
}
