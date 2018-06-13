package com.crwork.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crwork.app.R;
import com.crwork.app.domain.LitterDomain;

import java.util.ArrayList;

public class LDPAdapter extends BaseAdapter {
    private ArrayList<LitterDomain> mLitterDomainList;
    private Context mContext;

    public LDPAdapter(ArrayList<LitterDomain> list, Context context) {
        mLitterDomainList = list;
        mContext = context;
    }

    public void refresh(ArrayList<LitterDomain> list) {
        mLitterDomainList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mLitterDomainList == null ? 0 : mLitterDomainList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLitterDomainList.get(position);
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
            convertView = inflater.inflate(R.layout.litter_data_list_item, null);
            holder = new Holder();
            holder.userID = convertView.findViewById(R.id.userID);
            holder.littertypeID = convertView.findViewById(R.id.littertypeID);
            holder.weight = convertView.findViewById(R.id.weight);
            holder.litterdate = convertView.findViewById(R.id.litterdate);
            holder.littertprice = convertView.findViewById(R.id.littertprice);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.userID.setText(String.valueOf(mLitterDomainList.get(position).getUserId()));
        holder.littertypeID.setText(mLitterDomainList.get(position).getLittertypeID() == 0
                ? mContext.getResources().getString(R.string.litter_union_type)
                : mContext.getResources().getString(R.string.litter_recyclable_type));
        holder.weight.setText(String.valueOf(mLitterDomainList.get(position).getWeight())
                + mContext.getResources().getString(R.string.tv_weight_count_tip));
        holder.littertprice.setText(String.valueOf(mLitterDomainList.get(position).gettPrice()));
        holder.litterdate.setText(String.valueOf(mLitterDomainList.get(position).getLitterdate()));
        return convertView;
    }

    class Holder {
        private TextView userID, littertypeID, weight, litterdate, littertprice;
    }
}
