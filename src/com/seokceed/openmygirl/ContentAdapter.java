package com.seokceed.openmygirl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seokceed.openmygirl.contents.ContentsData;

public class ContentAdapter extends BaseAdapter {

	Context mContext;
	ContentsData[] mArrContents;
	String[] titles;

	public ContentAdapter(Context context, ContentsData[] arrContents) {
		mContext = context;
		mArrContents = arrContents;
		
		titles = new String[arrContents.length];
		
		int i = 0;
		for (ContentsData content : arrContents) {
			titles[i] = mContext.getResources().getStringArray(content.getMeta_id())[0];
			i++;
		}
	}

	@Override
	public int getCount() {
		return mArrContents.length;
	}

	@Override
	public Object getItem(int index) {
		return mArrContents[index];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.grid_content, null);
			convertView.setTag(R.id.iv_thumb, convertView.findViewById(R.id.iv_thumb));
			convertView.setTag(R.id.tv_title, convertView.findViewById(R.id.tv_title));
		}
		ContentsData contents = mArrContents[position];

		ImageView iv_thumb = (ImageView) convertView.getTag(R.id.iv_thumb);
		TextView tv_title = (TextView) convertView.getTag(R.id.tv_title);

		iv_thumb.setImageResource(contents.getThumb_id());
		tv_title.setText(titles[position]);

		return convertView;
	}

}
