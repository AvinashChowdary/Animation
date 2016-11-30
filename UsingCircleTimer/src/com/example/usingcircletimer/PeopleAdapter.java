package com.example.usingcircletimer;

import java.util.ArrayList;

import com.example.foursquare.People;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeopleAdapter extends BaseAdapter{

	private ArrayList<People> names;
	private LayoutInflater mInflater;

	public PeopleAdapter(Context applicationContext, ArrayList<People> names) {
		mInflater = LayoutInflater.from(applicationContext);
		this.names = names;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return names.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return names.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.people_list, parent, false);

			holder = new ViewHolder();

			holder.firstname = (TextView) convertView.findViewById(R.id.first_name);
			holder.lastName = (TextView) convertView
					.findViewById(R.id.last_name);
			holder.gender = (TextView) convertView
					.findViewById(R.id.gender);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		People name = names.get(position);

		holder.firstname.setText(name.firstName);
		holder.lastName.setText(name.lastName);
		holder.gender.setText(name.gender);

		return convertView;
	}
	
	static class ViewHolder {
		TextView firstname;
		TextView lastName;
		TextView gender;
	}

}
