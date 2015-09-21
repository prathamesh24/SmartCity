package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.smartcity.beans.ContactsItems;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter{

	Context context;
	ArrayList<ContactsItems>contacts;
	
	public ContactAdapter(Context context, ArrayList<ContactsItems> contacts) {
		this.context = context;
		this.contacts = contacts;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return contacts.indexOf(contacts.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater inflator=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=inflator.inflate(R.layout.custom_contact, null);
		}
		
		ImageView image=(ImageView) convertView.findViewById(R.id.imagview);
		TextView title,address,contactnumber,website;
		
		title=(TextView) convertView.findViewById(R.id.title);
		address=(TextView) convertView.findViewById(R.id.address);
		contactnumber=(TextView) convertView.findViewById(R.id.contactnumber);
		website=(TextView) convertView.findViewById(R.id.website);
		
		final ContactsItems row_pos=contacts.get(position);
		
		Picasso.with(context)
        .load(row_pos.getImage())
        .placeholder(R.drawable.stub) // optional
        .error(R.drawable.stub)         // optional
        .into(image);
		
		
		title.setText(Html.fromHtml("<u>"+row_pos.getName()+"</u>"));
		address.setText(row_pos.getAdress());
		contactnumber.setText(Html.fromHtml("<b>Con:</b> "+row_pos.getContactnumber()));
		website.setText(Html.fromHtml("<b>Website:</b> "+row_pos.getWebsite()));
		
		website.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(row_pos.getWebsite()));
				context.startActivity(browserIntent);
			}
		});
		
		return convertView;
	}

}
