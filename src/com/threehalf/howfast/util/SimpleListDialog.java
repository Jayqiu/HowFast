package com.threehalf.howfast.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.threehalf.howfast.R;

public class SimpleListDialog extends BaseDialog implements OnItemClickListener {
	private ListView mLvDisplay;
	private BaseAdapter mAdapter;
	private onSimpleListItemClickListener mOnSimpleListItemClickListener;

	public SimpleListDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.include_dialog_simplelist);
		mLvDisplay = (ListView) findViewById(R.id.dialog_simplelist_list);
		mLvDisplay.setOnItemClickListener(this);
	}

	public void setAdapter(BaseAdapter adapter) {
		mAdapter = adapter;
		if (mAdapter != null) {
			mLvDisplay.setAdapter(mAdapter);
		}
	}

	public void notifyDataSetChanged() {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void setOnSimpleListItemClickListener(
			onSimpleListItemClickListener listener) {
		mOnSimpleListItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		if (mOnSimpleListItemClickListener != null) {
			mOnSimpleListItemClickListener.onItemClick(arg2, view);
			dismiss();
		}
	}

	public interface onSimpleListItemClickListener {
		public void onItemClick(int position, View view);
	}
}
