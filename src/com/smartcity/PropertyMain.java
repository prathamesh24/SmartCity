package com.smartcity;

import com.smartcity.adapters.SampleAdapter;
import com.smartcity.widgets.SlidingTabLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PropertyMain extends Fragment {
	View rootview;
	SlidingTabLayout mSlidingTabLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.layout_container, container, false);
		ViewPager pager = (ViewPager) rootview.findViewById(R.id.pager);

		pager.setAdapter(buildAdapter());

		mSlidingTabLayout = (SlidingTabLayout) rootview.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(pager);

		// pager.setCurrentItem(1);
		return rootview;
	}

	private PagerAdapter buildAdapter() {
		return (new SampleAdapter(getActivity(), getChildFragmentManager()));
	}
}
