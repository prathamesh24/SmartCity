package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.smartcity.beans.PoliticsItems;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Poliitianadapter extends BaseAdapter{
	Context context;
	ArrayList<PoliticsItems>items;

	public Poliitianadapter(Context context, ArrayList<PoliticsItems> items) {
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
			LayoutInflater inflator=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=inflator.inflate(R.layout.custom_party, null);
		}
		ImageView partyimg=(ImageView) convertView.findViewById(R.id.paryimage);
		TextView title=(TextView) convertView.findViewById(R.id.title);
		
		PoliticsItems row_pos=items.get(position);
		
		 Picasso.with(context)
	        .load(row_pos.getImage())
	        .placeholder(R.drawable.stub) // optional
	        .error(R.drawable.stub)         // optional
	        .into(partyimg);
		title.setText(row_pos.getParty());
		
		return convertView;
	}

}
