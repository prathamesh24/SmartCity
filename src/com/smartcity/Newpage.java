package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.smartcity.beans.NewsItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Newpage extends Fragment{
	
	ArrayList<NewsItems>news;
	ListView listview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.layout_newslist,container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		new GetNews().execute();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		listview=(ListView) view.findViewById(R.id.listview);
	}
	
	private class GetNews extends AsyncTask<Void, Void, Void> {
		ArrayList<NameValuePair> parameters;

		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(getActivity());
			dialog.setMessage("Processing");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				parameters = new ArrayList<NameValuePair>();

				String regionString = new Smartcitydatabase(getActivity()).getRegion();
				parameters.add(new BasicNameValuePair("region", regionString));

				String response = new ServiceHandler().makeServiceCall(webservices.GetNews, ServiceHandler.POST,
						parameters);
				news=new ArrayList<NewsItems>();
				JSONArray mainArray = new JSONArray(response);
				for (int i = 0; i < mainArray.length(); i++) {
					NewsItems row=new NewsItems();
					String desctxt = mainArray.getJSONObject(i).getString("description");
					String titletxt = mainArray.getJSONObject(i).getString("title");
				
					row.setTitle(titletxt);
					row.setDescription(desctxt);
					
					news.add(row);
				}

			} catch (Exception e) {
				return null;
			}
			return null;

		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				listview.setAdapter(new Myadapter(getActivity(), news));
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally {
				if(dialog.isShowing())
					dialog.dismiss();
			}
		}

	}
	
	private class Myadapter extends BaseAdapter{
		
		
		Context context;
		public Myadapter(Context context, ArrayList<NewsItems> newss) {
			super();
			this.context = context;
			this.newss = newss;
		}

		ArrayList<NewsItems>newss;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newss.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return newss.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return newss.indexOf(newss.get(position));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			try {
				
				LayoutInflater inflator=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				convertView=inflator.inflate(R.layout.custom_newslayout,null);
				
				TextView title,desc;
				title=(TextView) convertView.findViewById(R.id.title);
				desc=(TextView) convertView.findViewById(R.id.desc);
				NewsItems ite=newss.get(position);
				
				title.setText(ite.getTitle());
				desc.setText(ite.getDescription());
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return convertView;
		}
		
	}

}
