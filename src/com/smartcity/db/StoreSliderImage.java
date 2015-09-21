package com.smartcity.db;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StoreSliderImage {
	 Context context;
	 SharedPreferences pref;
	 Editor editor;

	public StoreSliderImage(Context context) {
		this.context = context;
		pref=context.getSharedPreferences("SliderImage",Context.MODE_PRIVATE);
		editor=pref.edit();
	}
	
	public void saveImage(String key,String image) {
		editor.putString(key, image);
		editor.commit();
	}
	public String getImage(String key){
		return pref.getString(key, "");
	}
	
}
