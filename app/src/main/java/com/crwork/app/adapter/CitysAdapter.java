package com.crwork.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crwork.app.R;
import com.crwork.app.domain.CitysDomain;

import java.util.ArrayList;

/**
 * city items adatper
 */
public class CitysAdapter extends BaseAdapter {
    private ArrayList<CitysDomain> mCitysList;
    private Context mContext;

    public CitysAdapter(ArrayList<CitysDomain> list, Context context) {
        mCitysList = list;
        mContext = context;
    }

    public void refresh(ArrayList<CitysDomain> list) {
        mCitysList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCitysList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCitysList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.citys_data_list_item, null);
            holder = new Holder();
            holder.citys_id = (TextView) convertView.findViewById(R.id.citys_id);
            holder.citys_parents_id = (TextView) convertView.findViewById(R.id.citys_parents_id);
            holder.citys_name_zh = (TextView) convertView.findViewById(R.id.citys_name_zh);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.citys_id.setText(String.valueOf(mCitysList.get(position).getId()));
        holder.citys_parents_id.setText(String.valueOf(mCitysList.get(position).getParent_id()));
        holder.citys_name_zh.setText(String.valueOf(mCitysList.get(position).getCity_name_zh()));
        return convertView;
    }

    class Holder {
        private TextView citys_id, citys_parents_id, citys_name_zh;
    }
}
