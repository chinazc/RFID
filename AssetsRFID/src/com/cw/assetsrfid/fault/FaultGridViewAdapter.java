package com.cw.assetsrfid.fault;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cw.assetsrfid.R;
import com.cw.assetsrfid.entity.FaultInfoEntity;

public class FaultGridViewAdapter extends BaseAdapter{

	private ArrayList<FaultInfoEntity> list = null;
	private Context context;
	
	public FaultGridViewAdapter(Context context,ArrayList<FaultInfoEntity> list){
		this.list = list;
		this.context = context;
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GViewHolder holder;
		if (convertView == null) {
			holder = new GViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.view_layout_fault_detail, null, false);
	        
			holder.textviewFaultID = (TextView)convertView.findViewById(R.id.textViewWorkRequestID);  
			holder.textviewFaultStatus = (TextView) convertView.findViewById(R.id.textViewStatus);  
			holder.textviewFaultDescription = (TextView) convertView.findViewById(R.id.textViewDescription);  
			holder.textViewReportTime=(TextView) convertView.findViewById(R.id.textViewReportTime);
			holder.textViewEquipment=(TextView) convertView.findViewById(R.id.textViewEquipmentNum);
			holder.textViewPosition=(TextView) convertView.findViewById(R.id.textViewPosition);
			holder.textViewDepartment=(TextView) convertView.findViewById(R.id.textViewDepartment);
			convertView.setTag(holder);
		} else {
			holder = (GViewHolder) convertView.getTag();
		}
		holder.textviewFaultID.setText(list.get(position).getAssetsId()+"#");
		holder.textviewFaultStatus.setText(list.get(position).getStatus()+"");
		holder.textviewFaultDescription.setText(list.get(position).getDescription()+"");
		holder.textViewReportTime.setText(list.get(position).getReportTime()+"");
		holder.textViewEquipment.setText(list.get(position).getReportorId()+"");
		holder.textViewPosition.setText(list.get(position).getPositions()+"");
		holder.textViewDepartment.setText(list.get(position).getDepartmentId()+"");
		
		
		return convertView;
	}

	private static class GViewHolder {

		TextView textviewFaultID;
		TextView textviewFaultStatus;
		TextView textviewFaultDescription;
		TextView textViewReportTime;
		TextView textViewEquipment;
		TextView textViewPosition;
		TextView textViewDepartment;
	}
}
