package com.cw.assetsrfid.assets.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.entity.AssetsInfoEntity;

public class AssetsListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<AssetsInfoEntity> list;

	public AssetsListAdapter(Context context, ArrayList<AssetsInfoEntity> list) {
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
		// TODO Auto-generated method stub
		GViewHolder holder;
		if (convertView == null) {
			holder = new GViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.assets_query_main_list_item, null, false);
			
			holder.textviewAssetsIdID = (TextView) convertView
					.findViewById(R.id.assetsId);
			holder.textviewaAssetsName = (TextView) convertView
					.findViewById(R.id.assetsName);
			holder.textviewDeviceType = (TextView) convertView
					.findViewById(R.id.deviceType);
			holder.textViewBelongDept = (TextView) convertView
					.findViewById(R.id.belongDept);
			holder.textViewImportance = (TextView) convertView
					.findViewById(R.id.importance);
			holder.textViewStatus = (TextView) convertView
					.findViewById(R.id.status);
			convertView.setTag(holder);
		} else {
			holder = (GViewHolder) convertView.getTag();
		}
		holder.textviewAssetsIdID.setText(list.get(position).getAssetID());
		holder.textviewaAssetsName.setText(list.get(position).getAssetName());
		holder.textviewDeviceType.setText(list.get(position).getEquipmentCategory());
		holder.textViewBelongDept.setText(list.get(position).getBelongDepartment());
		holder.textViewImportance.setText(list.get(position).getImportance());
		holder.textViewStatus.setText(list.get(position).getStatus());
		return convertView;
	}

	private static class GViewHolder {
		TextView textviewAssetsIdID;
		TextView textviewaAssetsName;
		TextView textviewDeviceType;
		TextView textViewBelongDept;
		TextView textViewImportance;
		TextView textViewStatus;
	}

}