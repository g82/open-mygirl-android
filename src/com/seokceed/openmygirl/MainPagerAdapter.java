package com.seokceed.openmygirl;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainPagerAdapter extends PagerAdapter {
	
	private RelativeLayout rl_create;
	private LinearLayout ll_whatnext;
	
	public MainPagerAdapter(View ... args) {
		rl_create = (RelativeLayout) args[0];
		ll_whatnext = (LinearLayout) args[1];
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = null;
		switch (position) {
		case 0:
			v = rl_create;
			break;

		case 1:
			v = ll_whatnext;
			break;
		}
		
		container.addView(v, position);
		
		return v;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
