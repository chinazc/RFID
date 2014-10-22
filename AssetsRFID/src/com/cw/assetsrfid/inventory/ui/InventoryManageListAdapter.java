package com.cw.assetsrfid.inventory.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cw.assetsrfid.R;
import com.cw.assetsrfid.entity.AssetsInfoEntity;

	public class InventoryManageListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<AssetsInfoEntity> list;

		public InventoryManageListAdapter(Context context, ArrayList<AssetsInfoEntity> list) {
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
						R.layout.inventory_manage_main_list_item, null, false);
				
				holder.inventoryCode = (TextView) convertView
						.findViewById(R.id.inventoryCode);
				holder.inventoryDes = (TextView) convertView
						.findViewById(R.id.inventoryDes);
				holder.inventoryPalce = (TextView) convertView
						.findViewById(R.id.inventoryPalce);
				holder.inventoryCount = (TextView) convertView
						.findViewById(R.id.inventoryCount);
				holder.inventoryUnit = (TextView) convertView
						.findViewById(R.id.inventoryUnit);
				holder.inventorySafeCount = (TextView) convertView
						.findViewById(R.id.inventorySafeCount);
				convertView.setTag(holder);
			} else {
				holder = (GViewHolder) convertView.getTag();
			}
			holder.inventoryCode.setText(list.get(position).getAssetID());
			holder.inventoryDes.setText(list.get(position).getAssetName());
			holder.inventoryPalce.setText(list.get(position).getEquipmentCategory());
			holder.inventoryCount.setText(list.get(position).getBelongDepartment());
			holder.inventoryUnit.setText(list.get(position).getImportance());
			holder.inventorySafeCount.setText(list.get(position).getStatus());
			return convertView;
		}

		private static class GViewHolder {
			TextView inventoryCode;
			TextView inventoryDes;
			TextView inventoryPalce;
			TextView inventoryCount;
			TextView inventoryUnit;
			TextView inventorySafeCount;
		}

	}