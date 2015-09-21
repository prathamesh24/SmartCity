package com.smartcity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Aboutus extends Fragment{

	View rootview;
	TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.layout_aboutus, container,false);
		tv=(TextView) rootview.findViewById(R.id.tv);
		tv.setText(Html.fromHtml("Started in Jan 2015 Core Developers came up with revolutionary Software training programs. Advance technology, effective forecasting of upcoming changes in industry and quality deliverable are the important KPI.Core Developers is basically social enterprise, started with an idea of bringing global high level e learning courses and trends with application base training. With e learning as a core competency Core Developers has spread its wings in various dimensions such as software development, web developments, advance software training, interactive education, mobile applications and so on.There is huge evolution and technological changes expected in future to make the operations more efficient and effective. Feel free to convey your feedback and suggestions to make it more Lean."));
		return rootview;
	}
}
