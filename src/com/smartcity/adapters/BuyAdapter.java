package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.smartcity.beans.BuyItems;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyAdapter extends BaseAdapter{

	Context context;
	ArrayList<BuyItems>items;
	public BuyAdapter(Context context, ArrayList<BuyItems> items) {
		super();
		this.context = context;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return items.indexOf(items.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.custom_buy, null);
		}
		
		ImageView image=(ImageView) convertView.findViewById(R.id.propertyImage);
		TextView title,amount,address,mobileno;
		title=(TextView) convertView.findViewById(R.id.tile);
		mobileno=(TextView) convertView.findViewById(R.id.mob);
		amount=(TextView) convertView.findViewById(R.id.ammount);
		address=(TextView) convertView.findViewById(R.id.address);
		
		BuyItems row_pos=items.get(position);
		
		Picasso.with(context)
        .load(row_pos.getImage())
        .placeholder(R.drawable.stub) // optional
        .error(R.drawable.stub)         // optional
        .into(image);
		
		title.setText(row_pos.getTitle());
		amount.setText("Cost: "+row_pos.getAmount());
		mobileno.setText("Mobile no: "+row_pos.getMobile());
		address.setText(row_pos.getLocation());
		
		return convertView;
		
	}

}
