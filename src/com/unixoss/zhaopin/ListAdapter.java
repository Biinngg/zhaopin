package com.unixoss.zhaopin;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	List<Info> list = null;
	LayoutInflater inflater;
	
	public ListAdapter(Context context) {
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
			holder.title = (TextView)arg1.findViewById(R.id.title);
			holder.timeLocal = (TextView)arg1.findViewById(R.id.time_local);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder)arg1.getTag();
		}
		holder.info = getItem(arg0);
		holder.title.setText(holder.info.type + "  " + holder.info.title);
		holder.timeLocal.setText(holder.info.start + holder.info.location);
		arg1.setTag(holder);
		return arg1;
	}
	
	class ViewHolder {
		Info info;
		TextView title;
		TextView timeLocal;
	}

}
