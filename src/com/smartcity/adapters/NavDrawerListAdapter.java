package com.smartcity.adapters;

import java.util.ArrayList;

import com.smartcity.R;
import com.smartcity.MyImageLoder.ImageLoader;
import com.smartcity.beans.NavDrawerItem;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class NavDrawerListAdapter extends BaseAdapter {
     
	public ImageLoader imageLoader;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
     
    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        imageLoader=new ImageLoader(context);
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        
        NavDrawerItem row_pos=navDrawerItems.get(position);
          
        //imageLoader.DisplayImage(row_pos.getIcon(), imgIcon); 
        
        String imageToLoad=row_pos.getIcon();
        Picasso.with(context)
        .load(imageToLoad)
        .placeholder(R.drawable.stub) // optional
        .error(R.drawable.stub)         // optional
        .into(imgIcon);
        
        txtTitle.setText(row_pos.getTitle());
         
        return convertView;
    }
 
}