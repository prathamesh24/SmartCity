package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.smartcity.beans.ServiceItems;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceAdapter extends BaseAdapter{
	Context context;
	ArrayList<ServiceItems>services;
	
	public ServiceAdapter(Context context, ArrayList<ServiceItems> services) {
		this.context = context;
		this.services = services;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return services.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return services.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return services.indexOf(services.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater inflator=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=inflator.inflate(R.layout.custom_grid, null);
		}
		
		ImageView image=(ImageView) convertView.findViewById(R.id.imageView1);
		TextView title=(TextView) convertView.findViewById(R.id.textView1);
		
		ServiceItems row_pos=services.get(position);
		
		Picasso.with(context)
        .load(row_pos.getImage())
        .placeholder(R.drawable.stub) // optional
        .error(R.drawable.stub)         // optional
        .into(image);
		
		
		title.setText(row_pos.getCategory());
		
		return convertView;
	}

}
