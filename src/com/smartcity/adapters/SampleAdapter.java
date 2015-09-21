package com.smartcity.adapters;


import com.smartcity.PropertyBuy;
import com.smartcity.PropertyRent;
import com.smartcity.PropertySell;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SampleAdapter extends FragmentPagerAdapter {
	Context ctxt = null;

	public SampleAdapter(Context ctxt, FragmentManager mgr) {
		super(mgr);
		this.ctxt = ctxt;
	}

	@Override
	public int getCount() {
		return (3);
	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0) {
			return (PropertySell.newInstance(0));
		} else if (position == 1){
			return (PropertyBuy.newInstance(position));
		}
		else {
			return (PropertyRent.newInstance(position));
		}

	}

	@Override
	public String getPageTitle(int position) {
		if (position == 0) {
			return (PropertySell.getTitle(ctxt, position));
		}
		else if (position == 1) {
			return (PropertyBuy.getTitle(ctxt, position));
		}
		else {
			return (PropertyRent.getTitle(ctxt, position));
		}
	}
}