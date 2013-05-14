package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
 
public class ListAdapter extends BaseExpandableListAdapter {
	
	 
	private Context context;
	private ArrayList<HeaderInfo> websitesList;
	  
	public ListAdapter(Context context, ArrayList<HeaderInfo> websiteList) {
		 this.context = context;
		 this.websitesList = websiteList;
	}
	  
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<DetailInfo> eventsList = websitesList.get(groupPosition).getEventsList();
		return eventsList.get(childPosition);
	}
	 
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	 
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
	   
		DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.child_row, null);
		}
	   
		TextView sequence = (TextView) view.findViewById(R.id.sequence);
		sequence.setText(detailInfo.getSequence().trim() + ") ");
		TextView childItem = (TextView) view.findViewById(R.id.childItem);
		childItem.setText(detailInfo.getName().trim());
	   
		return view;
	}
	 
	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<DetailInfo> eventsList = websitesList.get(groupPosition).getEventsList();
		return eventsList.size();
	 
	}
	 
	@Override
	public Object getGroup(int groupPosition) {
		return websitesList.get(groupPosition);
	}
	 
	@Override
	public int getGroupCount() {
		return websitesList.size();
	}
	 
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	 
	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
	   
		HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.group_heading, null);
		}
	   
		TextView heading = (TextView) view.findViewById(R.id.heading);
		heading.setText(headerInfo.getName().trim());
	   
		return view;
	}
	 
	@Override
	public boolean hasStableIds() {
		return true;
	}
	 
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
