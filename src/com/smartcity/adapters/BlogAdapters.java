package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.smartcity.beans.BlogItems;
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

public class BlogAdapters extends BaseAdapter{

	Context context;
	ArrayList<BlogItems>items;
	public BlogAdapters(Context context, ArrayList<BlogItems> items) {
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
			convertView=inflater.inflate(R.layout.custom_blog, null);
		}
		
		TextView name,blog,place;
		name=(TextView) convertView.findViewById(R.id.name);
		blog=(TextView) convertView.findViewById(R.id.blog);
		place=(TextView) convertView.findViewById(R.id.place);
		
		BlogItems row_pos=items.get(position);
		
//		Picasso.with(context)
//        .load(row_pos.getImage())
//        .placeholder(R.drawable.stub) // optional
//        .error(R.drawable.stub)         // optional
//        .into(image);
		
		name.setText(row_pos.getName());
		blog.setText(row_pos.getBlog());
		place.setText(row_pos.getRegion()+" - "+row_pos.getDate());
		
		return convertView;
		
	}

}
