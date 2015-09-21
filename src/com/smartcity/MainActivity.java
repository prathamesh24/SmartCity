package com.smartcity;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.smartcity.adapters.NavDrawerListAdapter;
import com.smartcity.beans.NavDrawerItem;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener,OnItemClickListener{
	
	ArrayList<NavDrawerItem>items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ImageButton drawerBtn,sharebtn;
    TextView title;
    ImageButton profileIcon,call1,call2,call3,call4;
    RelativeLayout partnerBtn,aboutus,emergencybtn,sharerelay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Init();
		
		setContentView(R.layout.activity_main);
		drawerBtn=(ImageButton) findViewById(R.id.naviationdrawerBtn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        title=(TextView) findViewById(R.id.title);
        aboutus=(RelativeLayout) findViewById(R.id.aboutus);
        partnerBtn=(RelativeLayout) findViewById(R.id.partner);
        emergencybtn=(RelativeLayout) findViewById(R.id.emergency);
        profileIcon=(ImageButton) findViewById(R.id.profileIcon);
        sharerelay=(RelativeLayout) findViewById(R.id.sharerelay);
        sharebtn=(ImageButton) findViewById(R.id.sharebtn);
        
        
        call1=(ImageButton) findViewById(R.id.call1);
        call2=(ImageButton) findViewById(R.id.call2);
        call3=(ImageButton) findViewById(R.id.call3);
        call4=(ImageButton) findViewById(R.id.call4);
        
        sharebtn.setOnClickListener(this);
        
        
        sharerelay.setOnClickListener(this);
        partnerBtn.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        emergencybtn.setOnClickListener(this);
        profileIcon.setOnClickListener(this);
        call1.setOnClickListener(this);
        call2.setOnClickListener(this);
        call3.setOnClickListener(this);
        call4.setOnClickListener(this);
        
        title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				drawerBtn.performClick();
			}
		});
        
        mDrawerList.setOnItemClickListener(this);
        drawerBtn.setOnClickListener(this);
        
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
        
        if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()){
        	new GetDrawerItems().execute();
        }
        else {
        	items=new ArrayList<NavDrawerItem>();
        	
        	NavDrawerItem item1=new NavDrawerItem();
			item1 =new NavDrawerItem();
			item1.setIcon("http://png-1.findicons.com/files/icons/1580/devine_icons_part_2/128/home.png");
			item1.setTitle("Home");
			
        	items=new Smartcitydatabase(MainActivity.this).getDrawerItems();
        	
        	items.add(item1);
        	
        	Collections.swap(items, items.size()-1,0);
        	
        	mDrawerList.setAdapter(new NavDrawerListAdapter(MainActivity.this, items));
		}
        
       
        
	}
	
	private int count=0;
	
	public void showInfo() {
		new ShowcaseView.Builder(MainActivity.this)
		.setTarget(new ViewTarget(findViewById(R.id.naviationdrawerBtn)))
		.setContentTitle("Drawer Menu Button")
		.setContentText("Click here to open drawer")
		.setShowcaseEventListener(new OnShowcaseEventListener() {
			
			@Override
			public void onShowcaseViewShow(ShowcaseView showcaseView) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onShowcaseViewHide(ShowcaseView showcaseView) {
				// TODO Auto-generated method stub
				if(count==0){
					new ShowcaseView.Builder(MainActivity.this)
					.setTarget(new ViewTarget(findViewById(R.id.profileIcon)))
					.setContentTitle("Profile Button")
					.setContentText("Click here to see your profile page")
					.setShowcaseEventListener(this)
					.build();
					++count;
				}
				else if(count==1){
					new ShowcaseView.Builder(MainActivity.this)
					.setTarget(new ViewTarget(findViewById(R.id.call1)))
					.setContentTitle("Emergency Contacts")
					.setContentText("Click here to contact in emergency case")
					.setShowcaseEventListener(this)
					.build();
					++count;
				}
				else if(count==2){
					new ShowcaseView.Builder(MainActivity.this)
					.setTarget(new ViewTarget(findViewById(R.id.call2)))
					.setContentTitle("About us")
					.setContentText("Click here to read more about Core Developers")
					.setShowcaseEventListener(this)
					.build();
					++count;
				}
				else if(count==3){
					new ShowcaseView.Builder(MainActivity.this)
					.setTarget(new ViewTarget(findViewById(R.id.sharebtn)))
					.setContentTitle("Share Button")
					.setContentText("Click here to Share any things")
					.setShowcaseEventListener(this)
					.build();
					++count;
				}
				
			}
			
			@Override
			public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
				// TODO Auto-generated method stub
				
			}
		}).build();
	}
	
//	private void Init() {
//		items=new ArrayList<NavDrawerItem>();
//		
//		NavDrawerItem homeBtn=new NavDrawerItem();
//		NavDrawerItem shareBtn=new NavDrawerItem();
//		NavDrawerItem rateusBtn=new NavDrawerItem();
//		NavDrawerItem aboutusBtn=new NavDrawerItem();
//		
//		homeBtn.setIcon(R.drawable.ic_home_black_24dp);
//		homeBtn.setTitle(getString(R.string.home));
//		
//		shareBtn.setIcon(R.drawable.ic_launcher);
//		shareBtn.setTitle(getString(R.string.share));
//		
//		rateusBtn.setIcon(R.drawable.ic_launcher);
//		rateusBtn.setTitle(getString(R.string.rate));
//		
//		aboutusBtn.setIcon(R.drawable.ic_launcher);
//		aboutusBtn.setTitle(getString(R.string.aboutus));
//		
//		items.add(homeBtn);
//		items.add(shareBtn);
//		items.add(rateusBtn);
//		items.add(aboutusBtn);
//		
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.naviationdrawerBtn:
			
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {			
				mDrawerLayout.closeDrawer(mDrawerList);
			}

			else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			
			break;
			
		case R.id.partner:
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new BusinessPartner()).addToBackStack("").commit();
			break;
			
		case R.id.aboutus:
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new Aboutus()).addToBackStack("").commit();
			break;
		case R.id.emergency:
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new Emergency()).addToBackStack("").commit();
			break;
			
		case R.id.profileIcon:
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new ProfilePage()).addToBackStack("").commit();
			break;
			
		case R.id.call1:
			emergencybtn.performClick();
			break;
		case R.id.call2:
			aboutus.performClick();
			break;

		case R.id.call3:
			sharebtn.performClick();
			break;
			
		case R.id.call4:
			partnerBtn.performClick();
			break;
			
		case R.id.sharebtn:
			sharetext();
			break;
			
		case R.id.sharerelay:
			sharetext();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		NavDrawerItem navigationrow=items.get(position);
		String serviceTitle=navigationrow.getTitle();
		
		if (position == 0) {
			mDrawerLayout.closeDrawer(mDrawerList);
			ClearStack();
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
		} 
		
		else if (serviceTitle.equalsIgnoreCase("Blogs")) {
			mDrawerLayout.closeDrawer(mDrawerList);
			ClearStack();
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Blog()).commit();
		}
		else if (serviceTitle.equalsIgnoreCase("Real Estate")) {
			mDrawerLayout.closeDrawer(mDrawerList);
			ClearStack();
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PropertyMain()).commit();
		}
		else if (serviceTitle.equalsIgnoreCase("Second Hand")) {
			mDrawerLayout.closeDrawer(mDrawerList);
			ClearStack();
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Secondhand()).commit();
		}
		else if (serviceTitle.equalsIgnoreCase("Leader & Politics")) {
			mDrawerLayout.closeDrawer(mDrawerList);
			ClearStack();
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Politian_list()).commit();
		}
		else {

			SharedPreferences preff = getSharedPreferences("DrawerClick", Context.MODE_PRIVATE);
			Editor editor = preff.edit();
			editor.putString("category", navigationrow.getTitle());
			editor.commit();

			mDrawerLayout.closeDrawer(mDrawerList);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ServiceFragment()).addToBackStack("").commit();
		}
//		switch (position) {
//		case 0:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
//			break;
//		case 1:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Blog()).commit();
//			break;
//		case 2:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Politian_list()).commit();
//			break;
//		case 3:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Media()).commit();
//			break;
//		case 4:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Secondhand()).commit();
//			break;
//		case 5:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Educa()).commit();
//			break;
//		case 6:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new BusinessPartner()).commit();
//			break;
//		case 7:
//			mDrawerLayout.closeDrawer(mDrawerList);
//			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PropertyMain()).commit();
//			break;
//		}
	}
	
	private class GetDrawerItems extends AsyncTask<Void, Boolean, Boolean>{
		ProgressDialog dialog;
		ArrayList<NameValuePair>parameters;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(MainActivity.this);
			dialog.setMessage("Processing...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				parameters=new ArrayList<NameValuePair>();
				
				String regionString=new Smartcitydatabase(MainActivity.this).getRegion();
				
				parameters.add(new BasicNameValuePair("region", regionString));
				
				ServiceHandler handler=new ServiceHandler();
				
				String response=handler.makeServiceCall(webservices.getDrawerItems, ServiceHandler.POST, parameters);
				
				JSONArray mainarray=new JSONArray(response);
				
				items=new ArrayList<NavDrawerItem>();
				
				//http://png-1.findicons.com/files/icons/1580/devine_icons_part_2/128/home.png
				NavDrawerItem item1=new NavDrawerItem();
				item1 =new NavDrawerItem();
				item1.setIcon("http://png-1.findicons.com/files/icons/1580/devine_icons_part_2/128/home.png");
				item1.setTitle("Home");
				items.add(item1);
				
				Smartcitydatabase db=new Smartcitydatabase(MainActivity.this);
				
				ArrayList<String>navigations=db.getDrawerCategory();
				
				for (int i = 0; i < mainarray.length(); i++) {
					NavDrawerItem item=new NavDrawerItem();
					JSONObject innerobj=mainarray.getJSONObject(i);
					
					String image=innerobj.getString("image").toString();
					item.setIcon(image);
					
					String cat=innerobj.getString("category").toString();
					item.setTitle(cat);
					
					if(!navigations.contains(cat)){
						if(db.InsertDrawerItems(image, cat)){
							Log.d("data insert","zala");
						}
						Log.d("data","nai ahe");
					}
					else {
						Log.d("data","ahe");
					}
					
					items.add(item);
					
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
					mDrawerList.setAdapter(new NavDrawerListAdapter(MainActivity.this, items));

				        
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
	    if (fm.getBackStackEntryCount() > 0) {
	        fm.popBackStack();
	    }
	    else {
	    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Are you sure you want to exit?");
			dialog.setPositiveButton(" Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}
	
	private void ClearStack() {
		try {
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void sharetext() {
		String shareBody = "Hey I am using Click MEE Android App";
	    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	        sharingIntent.setType("text/plain");
	        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
	        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	        startActivity(Intent.createChooser(sharingIntent,"Share"));
	}
	
	
}
