package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;import android.print.PrintAttributes.Resolution;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Registration extends Activity implements OnClickListener{
	Spinner spinner ;
	ArrayList<String>regions;
	ArrayAdapter<String>dataAdapter;
	EditText name,address,email,mobileno;
	String regionStr;
	TextView termstv;
	Button regBtn;
	CheckBox checkbox;
	String terms="By Using this product, you agree to be legally bound by the terms and conditions, Privacy policies and terms of services (Including without limitation all disclaimers, exclusion of warranties and limitation of liability contained therein).<br /><br /><strong>Key Terms and Conditions</strong><br /We collect following information when you use this application in your phone. Your register mobile number, Mobile device's hardware information , unique id & location. This information is kept private to PiCE. In future we may share it to local venders so that they may could promote their product and/or services to you directly. <br /><br />We collect this information to give you location base services & relevant advertisements and news.<br /><br /Advertisements are accepted when they confirm their ethical standards and practices. Core Developers/Click Mee is/are not verify the authenticity of advertiser or the claims made in advertisements. Core Developers/Click Mee Shall not be responsible or liable for any loss or damage of any kind incurred as the result of your relationship with the advertiser.Advertisements are accepted only when they conform to the ethical standards. Core Developers/Click Mee does not verify the claims made in Advertisements. Core Developers/Click Mee shall not be responsible or liable for any loss or damage of any kind incurred as the result of your relationship with the advertiser.";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		regions=new ArrayList<String>();
		regions.add("Select Region");
		
		termstv=(TextView) findViewById(R.id.terms);
		name=(EditText) findViewById(R.id.name);
		address=(EditText) findViewById(R.id.address);
		email=(EditText) findViewById(R.id.email);
		mobileno=(EditText) findViewById(R.id.number);
		spinner = (Spinner) findViewById(R.id.spinner);
		regBtn=(Button) findViewById(R.id.regBtn);
		checkbox=(CheckBox) findViewById(R.id.checkbox);
		
		termstv.setText(Html.fromHtml(terms));
		
		regBtn.setOnClickListener(this);
		
		dataAdapter=new ArrayAdapter<String>(Registration.this,android.R.layout.simple_spinner_item, regions);
	
		// Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     
        spinner.setAdapter(dataAdapter);
        
        new GetRegion().execute("");
        
        mobileno.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(position!=0){
					regionStr=regions.get(position);
				}
				else {
					regionStr="";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
        
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					regBtn.setVisibility(View.VISIBLE);
					termstv.setVisibility(View.GONE);
				}
				else{
					regBtn.setVisibility(View.GONE);
					termstv.setVisibility(View.VISIBLE);
				}
			}
		});
 
	}
	
	private class GetRegion extends AsyncTask<String, Void, Void>{
		ProgressDialog pdialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog=new ProgressDialog(Registration.this);
			pdialog.setMessage("Processing...");
			pdialog.setCancelable(false);
			pdialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.getRegion, ServiceHandler.POST);
				
				JSONArray mainarray=new JSONArray(response);
				
				for(int i=0;i<mainarray.length();i++){
					regions.add(mainarray.getJSONObject(i).getString("regionname").toString());
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				
				dataAdapter.notifyDataSetChanged();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally {

				if(pdialog.isShowing())
					pdialog.dismiss();
			}
		}
		
	}
	
	private class GetRegistred extends AsyncTask<String, String, String>{
		ProgressDialog dialog;
		ArrayList<NameValuePair>parameters;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(Registration.this);
			dialog.setMessage("Processing...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				parameters=new ArrayList<NameValuePair>();
				
				parameters.add(new BasicNameValuePair("name", params[0]));
				parameters.add(new BasicNameValuePair("address", params[1]));
				parameters.add(new BasicNameValuePair("email", params[2]));
				parameters.add(new BasicNameValuePair("cnumber", params[3]));
				parameters.add(new BasicNameValuePair("image", "abc.jpg"));
				parameters.add(new BasicNameValuePair("region", params[4]));
				
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.registration, ServiceHandler.POST, parameters);
				
				Log.d("Response==>", response.toString());
				
				return response.toString();
				
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				if(result.toString().equalsIgnoreCase("Data Insert Successfully")){
					if(new Smartcitydatabase(Registration.this).insertUserData(name.getText().toString(), mobileno.getText().toString(), email.getText().toString(),regionStr))
					{
						Log.d("data save zala re", "confirm zala");
						startActivity(new Intent(Registration.this,MainActivity.class));
					}
						Toast.makeText(Registration.this, result, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(Registration.this,"Error Occured!!!", Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally {
				// TODO: handle finally clause
				if(dialog.isShowing())
					dialog.dismiss();
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String fullname=name.getText().toString();
		new GetRegistred().execute(new String[]{fullname,address.getText().toString(),email.getText().toString(),mobileno.getText().toString(),regionStr});
	}
}
