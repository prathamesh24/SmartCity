package com.smartcity;

import com.smartcity.db.Smartcitydatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfilePage extends Fragment{

	TextView name,email,phn,region;
	Button freeads;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.layout_profile,container,false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		name=(TextView) view.findViewById(R.id.name);
		email=(TextView) view.findViewById(R.id.email);
		phn=(TextView) view.findViewById(R.id.phn);
		region=(TextView) view.findViewById(R.id.region);
		freeads=(Button) view.findViewById(R.id.freeads);
		
		Smartcitydatabase database=new Smartcitydatabase(getActivity());
		name.setText(database.getName());
		email.setText(database.getEmail());
		phn.setText(database.getNumber());
		region.setText(database.getRegion());
		
		
		freeads.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coredevelopers.co.in/admin/user/"));
				getActivity().startActivity(browserIntent);
			}
		});
	}
}
