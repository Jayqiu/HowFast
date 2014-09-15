package com.threehalf.howfast.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.threehalf.howfast.R;

public class SimpleListDialogAdapter extends BaseAdapter {
	private List<String> datas;
	private Context context;
	protected LayoutInflater mInflater;

	public SimpleListDialogAdapter(Context context, List<String> datas) {
		mInflater = LayoutInflater.from(context);
		this.datas = datas;
		this.context = context;
	}

	public SimpleListDialogAdapter(Context context, String... datas) {
		mInflater = LayoutInflater.from(context);
		if (datas != null && datas.length > 0) {
			this.datas = Arrays.asList(datas);
		}
		this.context = context;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.listitem_dialog, null);
		}
		((TextView) arg1.findViewById(R.id.listitem_dialog_text))
				.setText((CharSequence) getItem(arg0));
		return arg1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
