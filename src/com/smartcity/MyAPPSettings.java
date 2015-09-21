package com.smartcity;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;

public class MyAPPSettings extends Application{
	
	private static String APP_ID="4pQgqKenieTUbSxL78fLxhZ063Jn56ZluKgL99EV";
    private static String CLIENT_KEY="pfAJeKa30FLhQ1PHkwc7RnZq7RlKqvtnsiEAJ7LV";
    
	 @Override
	    public void onCreate() {
	        super.onCreate();
	     // Initialize the Parse SDK.
	    	Parse.initialize(this,APP_ID, CLIENT_KEY); 

	    	// Specify an Activity to handle all pushes by default.
	    	PushService.setDefaultPushCallback(this, SplashScreen.class);
	    }
}
