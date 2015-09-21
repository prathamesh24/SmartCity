package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SmallBanneradapter extends BaseAdapter{
	Context context;
	ArrayList<String> images;

	public SmallBanneradapter(Context context, ArrayList<String> images) {
		super();
		this.context = context;
		this.images = images;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return images.indexOf(images.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflator=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=inflator.inflate(R.layout.customsmallbanner,null);
		}
		ImageView image=(ImageView) convertView.findViewById(R.id.video);
		Picasso.with(context)
	        .load(images.get(position))
	        .placeholder(R.drawable.stub) // optional
	        .error(R.drawable.stub)         // optional
	        .into(image);
		
		return convertView;
	}

}
