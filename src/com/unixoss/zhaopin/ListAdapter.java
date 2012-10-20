package com.unixoss.zhaopin;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	Context context;
	List<Info> list = null;
	LayoutInflater inflater;
	
	public ListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void notifyDataSetChanged(List<Info> list) {
		this.list = list;
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(list == null)
			return 0;
		else
			return list.size();
	}

	@Override
	public Info getItem(int arg0) {
		if(list == null)
			return null;
		else
			return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if(arg1 == null || arg1.getTag() == null) {
			arg1 = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.date = (TextView)arg1.findViewById(R.id.date);
			holder.title = (TextView)arg1.findViewById(R.id.title);
			holder.time = (TextView)arg1.findViewById(R.id.time);
			holder.location = (TextView)arg1.findViewById(R.id.location);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder)arg1.getTag();
		}
		holder.info = getItem(arg0);
		//if(holder.info.first) {
			holder.date.setVisibility(View.VISIBLE);
			holder.date.setText(getDate(holder.info.start));
		//}
		holder.title.setText(String.format(context.getString(
				R.string.title_format),
				holder.info.type, holder.info.title));
		holder.time.setText(String.format(context.getString(
				R.string.time_format),
				getTime(holder.info.start), getTime(holder.info.end)));
		holder.location.setText(String.format(context.getString(
				R.string.location_format), holder.info.location));
		arg1.setTag(holder);
		return arg1;
	}
	
	@TargetApi(9)
	String getDate(long timeMillis) {
		String disp = "";
		Calendar cal = Calendar.getInstance();
		long today = cal.getTimeInMillis() / 86400000;
		long curDay = timeMillis / 86400000;
		if(today == curDay) {
			disp = context.getString(R.string.today);
		} else if(curDay - today == 1) {
			disp = context.getString(R.string.tomorrow);
		} else {
			cal.setTimeInMillis(timeMillis);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			disp = month + "ÔÂ" + day + "ÈÕ " + 
						cal.getDisplayName(Calendar.DAY_OF_WEEK,
								Calendar.ALL_STYLES, Locale.CHINA);
		}
		return disp;
	}
	
	String getTime(long timeMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMillis);
		int time = cal.get(Calendar.HOUR_OF_DAY);
		StringBuilder builder = new StringBuilder();
		builder.append(time < 10 ? "0" : "");
		builder.append(time);
		builder.append(':');
		time = cal.get(Calendar.MINUTE);
		builder.append(time < 10 ? "0" : "");
		builder.append(time);
		return builder.toString();
	}
	
	class ViewHolder {
		Info info;
		TextView date;
		TextView title;
		TextView time;
		TextView location;
	}

}
