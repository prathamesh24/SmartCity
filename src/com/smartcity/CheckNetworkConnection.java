package com.smartcity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetworkConnection {
	
	Context context;

	public CheckNetworkConnection(Context context) {
		this.context = context;
	}

	public boolean haveInternet(){
        NetworkInfo info=(NetworkInfo)((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if(info==null || !info.isConnected()){
            return false;
        }
        if(info.isRoaming()){
            //here is the roaming option you can change it if you want to disable internet while roaming, just return false
            return true;
        }
        return true;
    }
}
