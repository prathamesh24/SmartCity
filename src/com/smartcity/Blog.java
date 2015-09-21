package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartcity.adapters.BlogAdapters;
import com.smartcity.beans.BlogItems;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.ProgressDialog;
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

public class Blog extends Fragment implements OnClickListener{

	ArrayList<BlogItems>blogs;
	ListView list;
	View rootview;
	Button dialogBtn;
	String blogText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.layout_blog, container,false);
		list=(ListView) rootview.findViewById(R.id.blogList);
		dialogBtn=(Button) rootview.findViewById(R.id.dialogBtn);
		
		dialogBtn.setOnClickListener(this);
		return rootview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
			new GetBlogs().execute();
		else {
			ArrayList<BlogItems>storedblogs=new Smartcitydatabase(getActivity()).getBlogs();
			list.setAdapter(new BlogAdapters(getActivity(), storedblogs));
		}
		
		
	}
	
	private class GetBlogs extends AsyncTask<Void, Boolean, Boolean>{
		
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
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.GetBlogs, ServiceHandler.POST);
				
				JSONArray mainarray=new JSONArray(response);
				
				ArrayList<String>blognames=new Smartcitydatabase(getActivity()).getblogname();
				
				blogs=new ArrayList<BlogItems>();
				for (int i = 0; i < mainarray.length(); i++) {
					JSONObject mainobj=mainarray.getJSONObject(i);
					BlogItems oneitem=new BlogItems();
					
					String name=mainobj.getString("Name");
					String blog=mainobj.getString("Blog");
					String Region=mainobj.getString("Region");
					String date=mainobj.getString("Date");
					
					oneitem.setName(name);
					oneitem.setBlog(blog);
					oneitem.setRegion(Region);
					oneitem.setDate(date);
					
					if(!blognames.contains(name)){
						if(new Smartcitydatabase(getActivity()).InsertBlogs(name, blog, date, Region)){
							Log.d("data","save zala");
						}
					}
					else {
						Log.d("data","ahe re");
					}
					
					
					blogs.add(oneitem);
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
				if(result){
					list.setAdapter(new BlogAdapters(getActivity(), blogs));
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showInputDialog();
	}
	
	protected void showInputDialog() {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						blogText=editText.getText().toString();
						if(blogText.length()!=0){
							if(new ConnectionDetector(getActivity()).isConnectingToInternet())
							 new EnterBlog().execute(blogText);
							else
								Toast.makeText(getActivity(), "No Internet Connection",Toast.LENGTH_LONG).show();
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
					parameters.add(new BasicNameValuePair("name",db.getName()));
					parameters.add(new BasicNameValuePair("blog",params[0]));
					String response=handler.makeServiceCall(webservices.PostBlog, ServiceHandler.POST, parameters);
					
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
						Toast.makeText(getActivity(), "Blog added sucessfully", Toast.LENGTH_LONG).show();
						if(blogs.size()!=0)
							blogs.clear();
						new GetBlogs().execute();
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
