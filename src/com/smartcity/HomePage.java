package com.smartcity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.smartcity.R.layout;
import com.smartcity.adapters.NewsListAdpter;
import com.smartcity.adapters.SmallBanneradapter;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;
import com.smartcity.db.StoreSliderImage;
import com.smartcity.widgets.HorizontalListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class HomePage extends Fragment implements OnItemClickListener,OnClickListener{

	ArrayList<Bitmap> sliderimages;
	ArrayList<String>news,videos;
	View rootview;
	ViewFlipper fliper;
	ProgressBar progressbar;
	TextView marquee;
	HorizontalListView newslist,bannerlist;
	BroadcastReceiver broadcastReceiver;
	EditText searchbox;
	Button sendSearch;
	ArrayList<Bitmap>bitmapImages;
	LinearLayout myGallery;
	OnClickListener imgCick;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.layout_homepage, container, false);
		fliper = (ViewFlipper) rootview.findViewById(R.id.flipper);
		progressbar = (ProgressBar) rootview.findViewById(R.id.progressbar);
		marquee=(TextView) rootview.findViewById(R.id.marquee);
		newslist=(HorizontalListView) rootview.findViewById(R.id.newslist);
		bannerlist=(HorizontalListView) rootview.findViewById(R.id.bannerlist);
		searchbox=(EditText) rootview.findViewById(R.id.searchbox);
		sendSearch=(Button) rootview.findViewById(R.id.sendSearch);
		myGallery = (LinearLayout)rootview.findViewById(R.id.mygallery);
		
		newslist.setOnItemClickListener(this);
		
		sendSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(searchbox.getText().toString().length()!=0){
					SharedPreferences pre=getActivity().getSharedPreferences("searchdata", 0);
					Editor edit=pre.edit();
					edit.putString("keyword",searchbox.getText().toString());
					edit.commit();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new SerachResult()).commit();
				}
			}
		});
		
		marquee.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new Newpage()).commit();
			}
		});
		
//		myGallery.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ImageView imge=(ImageView) rootview.findViewById(v.getId());
//				String image=(String) imge.getTag();
//				Log.d("Plz yar", image);
//				
////				Intent i=new Intent(getActivity(),VideoActivity.class);
////				i.putExtra("youtubeID", videos.get(position));
////				getActivity().startActivity(i);
//			}
//		});
		
		
		imgCick=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position=v.getId();
				Intent i=new Intent(getActivity(),VideoActivity.class);
				
				i.putExtra("youtubeID", videos.get(position-1));
				getActivity().startActivity(i);
			}
		};
		
		return rootview;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final ArrayList<String>arr;
		if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
			new GetSliderImages().execute();
		}
		else {
			
			//offline
			
			ImageView image = new ImageView(getActivity());
			image.setAdjustViewBounds(true);
			image.setImageResource(R.drawable.offlinedefault);
			fliper.addView(image);
			
			fliper.setAutoStart(true);
			fliper.setFlipInterval(3000);
			fliper.startFlipping();
			
			ArrayList<String>news=new Smartcitydatabase(getActivity()).getNews();
				
			String txt="";
			for(int j=0;j<news.size();j++){
				txt=txt+news.get(j);
			}
			marquee.setText(txt);
			marquee.setSelected(true); 
			
			videos=new Smartcitydatabase(getActivity()).getAllVideos();
			arr=new Smartcitydatabase(getActivity()).getSmallSliderImages();
			
			newslist.setAdapter(new NewsListAdpter(getActivity(), videos));
			bannerlist.setAdapter(new SmallBanneradapter(getActivity(), arr));
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					newslist.setAdapter(new NewsListAdpter(getActivity(), videos));
					bannerlist.setAdapter(new SmallBanneradapter(getActivity(), arr));
				}
			}, 3000);
			
			progressbar.setVisibility(View.GONE);
		}	
	}

	private class GetSliderImages extends AsyncTask<Void, Void, Void> {
		ArrayList<NameValuePair> parameters;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				parameters = new ArrayList<NameValuePair>();

				String regionString = new Smartcitydatabase(getActivity()).getRegion();
				parameters.add(new BasicNameValuePair("region", regionString));

				String response = new ServiceHandler().makeServiceCall(webservices.GetSliderImages, ServiceHandler.POST,
						parameters);
				sliderimages = new ArrayList<Bitmap>();
				JSONArray mainArray = new JSONArray(response);
				for (int i = 0; i < mainArray.length(); i++) {

					String image = mainArray.getJSONObject(i).getString("Image");
					Bitmap covertedBitmap=getBitmapFromURL(image);
					sliderimages.add(covertedBitmap);
					
//					StoreSliderImage imagess=new StoreSliderImage(getActivity());
//					ArrayList<String>storedImages=new Smartcitydatabase(getActivity()).getSliderImages();
//					
//					if(!storedImages.contains(image)){
//						imagess.saveImage(image,BitMapToString(covertedBitmap));
//						new Smartcitydatabase(getActivity()).InsertSliderImages(image);
//					}
//					else {
//						Log.d("image","ahe re");
//					}
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
				for (int j = 0; j < sliderimages.size(); j++) {
					setFlipperImage(sliderimages.get(j));
				}
				fliper.setAutoStart(true);
				fliper.setFlipInterval(3000);
				fliper.startFlipping();
				
				new GetNews().execute();
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				progressbar.setVisibility(View.GONE);
			}
		}

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}

	private void setFlipperImage(Bitmap res) {
		Log.i("Set Filpper Called", res + "");
		ImageView image = new ImageView(getActivity());
		image.setAdjustViewBounds(true);
		image.setImageBitmap(res);
		fliper.addView(image);
	}

	private class GetNews extends AsyncTask<Void, Void, Void> {
		ArrayList<NameValuePair> parameters;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
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
				news=new ArrayList<String>();
				JSONArray mainArray = new JSONArray(response);
				for (int i = 0; i < mainArray.length(); i++) {

					String newstxt = mainArray.getJSONObject(i).getString("description");
					
					if(new Smartcitydatabase(getActivity()).CheckNews(newstxt)){
						if(new Smartcitydatabase(getActivity()).InsertNews(newstxt))
							Log.d("data","insert zala");
						else {
							Log.d("data","insert nai zala");
						}
					}
					else {
						Log.d("Data","ahe re");
					}
					
					news.add(newstxt);
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
				String txt="";
				for(int j=0;j<news.size();j++){
					txt=txt+news.get(j);
				}
				marquee.setText(txt);
				marquee.setSelected(true); 
				
				new GetVideos().execute();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	
	private class GetVideos extends AsyncTask<Void, Void, Void> {
		ArrayList<NameValuePair> parameters;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				parameters = new ArrayList<NameValuePair>();

				String regionString = new Smartcitydatabase(getActivity()).getRegion();
				parameters.add(new BasicNameValuePair("region", regionString));
				
				bitmapImages=new ArrayList<Bitmap>();

				String response = new ServiceHandler().makeServiceCall(webservices.GetVideos, ServiceHandler.POST,
						parameters);
				videos=new ArrayList<String>();
				JSONArray mainArray = new JSONArray(response);
				for (int i = 0; i < mainArray.length(); i++) {

					String newstxt = mainArray.getJSONObject(i).getString("videoid");
					
					String VideoIds[]=newstxt.split("https://www.youtube.com/embed/");
					
					Bitmap bits=getBitmapFromURL("http://img.youtube.com/vi/"+VideoIds[1]+"/hqdefault.jpg");
					
					bitmapImages.add(bits);
					
					if(new Smartcitydatabase(getActivity()).CheckVideoID(VideoIds[1])){
						if(new Smartcitydatabase(getActivity()).InsertVideos(VideoIds[1])){
							Log.d("Video", "Insert Zala");
						}
					}
					else {
						Log.d("Video","Ahe re");
					}
					videos.add(VideoIds[1]);
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

				 //newslist.setAdapter(new NewsListAdpter(getActivity(), videos));
				
				for(int i=0;i<bitmapImages.size();i++){
					myGallery.addView(insertPhoto(bitmapImages.get(i),i,videos.get(i)));
				}
				
				 new getsmallbanner().execute();
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally {
				progressbar.setVisibility(View.GONE);
			}
		}

	}
	
	View insertPhoto(Bitmap bm,int id,String video){
	     
	     LinearLayout layout = new LinearLayout(getActivity());
	     layout.setLayoutParams(new LayoutParams(500, 320));
	     layout.setGravity(Gravity.CENTER);
	     
	     ImageView imageView = new ImageView(getActivity());
	     imageView.setLayoutParams(new LayoutParams(500, 320));
	     imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	     imageView.setId(id+1);
	     imageView.setOnClickListener(imgCick);
	     imageView.setTag(video);
	     //imageView.setTag(imageView.getId(),video);
	     imageView.setImageBitmap(bm);
	     
	     layout.addView(imageView);
	     return layout;
	    }
	    
	
	private class getsmallbanner extends AsyncTask<Void, Void , Void>{
		ArrayList<String>arr;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			try {
				String regionString = new Smartcitydatabase(getActivity()).getRegion();
				ArrayList<NameValuePair>parameters=new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("region", regionString));
				
				String response=new ServiceHandler().makeServiceCall(webservices.GetSmallBanner, ServiceHandler.POST, parameters);
				JSONArray mainarray=new JSONArray(response);
				
				arr=new ArrayList<String>();
				ArrayList<String>temparr=new Smartcitydatabase(getActivity()).getSmallSliderImages();
				for (int i = 0; i < mainarray.length(); i++) {
					String image=mainarray.getJSONObject(i).getString("Image");
					arr.add(image);
					
					if(!temparr.contains(image)){
						if(new Smartcitydatabase(getActivity()).InsertSmallSliderImages(image)){
							Log.d("data save","zala");
						}
					}
					else {
						Log.d("data", "ahe re");
					}
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
				progressbar.setVisibility(View.GONE);
				 
				//newslist.setAdapter(new NewsListAdpter(getActivity(), videos));
				bannerlist.setAdapter(new SmallBanneradapter(getActivity(), arr));
				
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//newslist.setAdapter(new NewsListAdpter(getActivity(), videos));
						bannerlist.setAdapter(new SmallBanneradapter(getActivity(), arr));
					}
				}, 3000);
				
				 SharedPreferences prefff=getActivity().getSharedPreferences("hints", 0);
		        if(prefff.getString("bolflag", "false").equalsIgnoreCase("false")){
		        	Editor edit=prefff.edit();
		            edit.putString("bolflag","true");
		            edit.commit();
		            
		            ((MainActivity)getActivity()).showInfo();
		        }
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub youtubeID
		Intent i=new Intent(getActivity(),VideoActivity.class);
		i.putExtra("youtubeID", videos.get(position));
		getActivity().startActivity(i);
	}

	public String BitMapToString(Bitmap bitmap){
	     ByteArrayOutputStream baos=new  ByteArrayOutputStream();
	     bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
	     byte [] b=baos.toByteArray();
	     String temp=Base64.encodeToString(b, Base64.DEFAULT);
	     return temp;
	}
	
	/**
	 * @param encodedString
	 * @return bitmap (from given string)
	 */
	public Bitmap StringToBitMap(String encodedString){
	   try {
	      byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
	      Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	      return bitmap;
	   } catch(Exception e) {
	      e.getMessage();
	      return null;
	   }
	}
	
	
	public  Bitmap getBitmapImage(String src) {
	    try {
	    	
	    	// First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input, null, options);
	        
	        options.inSampleSize = calculateInSampleSize(options, 200, 200);
	        
	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        
	        myBitmap = BitmapFactory.decodeStream(input, null, options);
	        
	        return myBitmap;
	    } catch (IOException e) {
	        // Log exception
	        return null;
	    }
	}
	
	
	public int calculateInSampleSize(
		      
		     BitmapFactory.Options options, int reqWidth, int reqHeight) {
		     // Raw height and width of image
		     final int height = options.outHeight;
		     final int width = options.outWidth;
		     int inSampleSize = 1;
		        
		     if (height > reqHeight || width > reqWidth) {
		      if (width > height) {
		       inSampleSize = Math.round((float)height / (float)reqHeight);   
		      } else {
		       inSampleSize = Math.round((float)width / (float)reqWidth);   
		      }   
		     }
		     
		     return inSampleSize;   
		    }
//	private void installListener() {
//
//        if (broadcastReceiver == null) {
//
//            broadcastReceiver = new BroadcastReceiver() {
//
//                @Override
//                public void onReceive(Context context, Intent intent) {
//
//                    Bundle extras = intent.getExtras();
//
//                    NetworkInfo info = (NetworkInfo) extras
//                            .getParcelable("networkInfo");
//
//                    State state = info.getState();
//                    Log.d("InternalBroadcastReceiver", info.toString() + " "
//                            + state.toString());
//
//                    if (state == State.CONNECTED) {
//
//                         Toast.makeText(getActivity(), "Internet connected", Toast.LENGTH_LONG).show();
//
//                    } else {
//
//                    	 Toast.makeText(getActivity(), "Internet Disconnected", Toast.LENGTH_LONG).show();
//
//                    }
//
//                }
//            };
//
//            final IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            getActivity().registerReceiver(broadcastReceiver, intentFilter);
//        }
//    }
//	
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		installListener();
//	}
//	
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		getActivity().unregisterReceiver(broadcastReceiver);
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
