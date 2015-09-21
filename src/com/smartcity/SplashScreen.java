package com.smartcity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;
import com.smartcity.config.webservices;
import com.smartcity.db.Smartcitydatabase;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends FragmentActivity {

	String image;
	public static final String GENDER_MALE = "male";
	public static final String GENDER_FEMALE = "female";
	SharedPreferences preference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);

		//sandbox();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (new Smartcitydatabase(SplashScreen.this).getUserData() == 0) {
					//startActivity(new Intent(SplashScreen.this, Registration.class));
					//finish();
					Registerdevice();
				}

				else {
					
					if (new ConnectionDetector(SplashScreen.this).isConnectingToInternet())
						new Blocker().execute();
					else
						startActivity(new Intent(SplashScreen.this, MainActivity.class));
				}
				//finish();
			}
		}, 3000);
		
		
		
	}
	
	private void Registerdevice() {
		if(new ConnectionDetector(SplashScreen.this).isConnectingToInternet()){
			 ParseAnalytics.trackAppOpened(getIntent());

		        ParseInstallation.getCurrentInstallation().put("age", 22);

		        ParseInstallation.getCurrentInstallation().put("gender",GENDER_MALE);

		        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
		            @Override
		            public void done(ParseException e) {
		                if (e == null) {
		                    Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_success, Toast.LENGTH_SHORT);
		                    toast.show();
		                    
		                    startActivity(new Intent(SplashScreen.this,Registration.class));
							finish();
		                    
		                } else {
		                    e.printStackTrace();

		                    Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_failed, Toast.LENGTH_SHORT);
		                    toast.show();
		                    
		                }
		            }
		        });
			
			
		}
	}

	private void sandbox() {
		new Smartcitydatabase(SplashScreen.this).insertUserData("Prathamesh", "9930760257", "pprathamesh248@gmail.com",
				"shahapur");
	}

	private class Blocker extends AsyncTask<Void, Void, Void> {
		ArrayList<NameValuePair> paramters;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				paramters = new ArrayList<NameValuePair>();

				String region = new Smartcitydatabase(SplashScreen.this).getRegion();

				paramters.add(new BasicNameValuePair("region", region));

				String jsonresponse = new ServiceHandler().makeServiceCall(webservices.blocker, ServiceHandler.POST,
						paramters);

				image = new JSONArray(jsonresponse).getJSONObject(0).getString("image");

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
				FragmentManager fm = getSupportFragmentManager();
				MyDialogFragment dialogFragment = new MyDialogFragment ();
				dialogFragment.show(fm, "Sample Fragment");
				

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (image.length() == 0) {
					startActivity(new Intent(SplashScreen.this, MainActivity.class));
				}
			}
		}

	}

	public class MyDialogFragment extends DialogFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.custom_dialogbox, container, false);
		    getDialog().setTitle(getString(R.string.app_name));
			ImageView imageview = (ImageView) rootView.findViewById(R.id.imageview);
			Button close = (Button) rootView.findViewById(R.id.closebtn);
			Picasso.with(SplashScreen.this).load(image).placeholder(R.drawable.stub) // optional
					.error(R.drawable.stub) // optional
					.into(imageview);
			close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
					startActivity(new Intent(SplashScreen.this,MainActivity.class));
					finish();
				}
			});
			return rootView;
		}
	}
	
	@Override
    public void onStart() {
        super.onStart();

        // Display the current values for this user, such as their age and gender.
        displayUserProfile();
        refreshUserProfile();
    }

    // Get the latest values from the ParseInstallation object.
    private void refreshUserProfile() {
        ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {

            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    displayUserProfile();
                }
            }
        });
    }

    // Refresh the UI with the data obtained from the current ParseInstallation object.
    private void displayUserProfile() {
        String gender = ParseInstallation.getCurrentInstallation().getString("gender");
        int age = ParseInstallation.getCurrentInstallation().getInt("age");
//        Log.d("gender",gender);
//        Log.d("age",String.valueOf(age));

    }
}
