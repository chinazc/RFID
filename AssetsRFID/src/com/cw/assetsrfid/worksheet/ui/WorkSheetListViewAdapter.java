package com.cw.assetsrfid.worksheet.ui;

import java.util.ArrayList;

import com.cw.assetsrfid.R;
import com.cw.assetsrfid.entity.FaultInfoEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkSheetListViewAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<FaultInfoEntity> list;
	public WorkSheetListViewAdapter(Context context,ArrayList<FaultInfoEntity> list){
		this.context = context;
		this.list = list;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GViewHolder holder ;
		if (convertView == null) {
			holder = new GViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.work_sheet_list_item, null,false);
			holder.workNum = (TextView)convertView.findViewById(R.id.workSheetNum);
			holder.workStatus = (TextView) convertView.findViewById(R.id.workSheetStatus);  
			holder.workDescription = (TextView) convertView.findViewById(R.id.workSheetDes);  
			holder.workReportTime=(TextView) convertView.findViewById(R.id.assetsCode);
			holder.workEquipmentNum=(TextView) convertView.findViewById(R.id.assetsDescription);
			holder.workPosition=(TextView) convertView.findViewById(R.id.dept);
			holder.workDepartmentNum=(TextView) convertView.findViewById(R.id.planDate);
			convertView.setTag(holder);
		}else {
			holder = (GViewHolder) convertView.getTag();
		}
		holder.workNum.setText(list.get(position).getAssetsId()+"#");
		holder.workStatus.setText(list.get(position).getStatus()+"");
		holder.workDescription.setText(list.get(position).getDescription()+"");
		holder.workReportTime.setText(list.get(position).getReportTime()+"");
		holder.workEquipmentNum.setText(list.get(position).getReportorId()+"");
		holder.workPosition.setText(list.get(position).getPositions()+"");
		holder.workDepartmentNum.setText(list.get(position).getDepartmentId()+"");
		return convertView;
	}

	private static class GViewHolder {

		TextView workNum;
		TextView workStatus;
		TextView workDescription;
		TextView workReportTime;
		TextView workEquipmentNum;
		TextView workPosition;
		TextView workDepartmentNum;
	}
}
