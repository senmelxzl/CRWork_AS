package com.crwork.app.bt.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.adapter.LitterDataReportAdapter;
import com.crwork.app.dao.LitterDao;
import com.crwork.app.domain.LitterDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Report activity
 *
 * @author xiezhenlin
 */
public class LitterDataReportActivity extends Activity implements OnClickListener {
    private final static String TAG = "LitterDataReportActivity";
    private ListView lv_litter_data_list;
    private EditText userid_fetch;
    private TextView litter_data_fetch, litter_data_fetch_clean;
    private LitterDataReportAdapter mLitterReportAdapter;
    private LitterDao mLitterDao;
    ArrayList<LitterDomain> mLitterDomainList;
    private String userID_fetch = "0";
    List<Map<String, Object>> dataList;
    private TextView litter_data_filter_by_date, litter_data_filter_by_quarter, litter_data_filter_by_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.litter_data_report);
        mLitterDao = new LitterDao();
        initview();
    }

    private void initview() {
        // TODO Auto-generated method stub
        lv_litter_data_list = (ListView) findViewById(R.id.lv_litter_data_list);
        userid_fetch = (EditText) findViewById(R.id.userid_fetch);
        litter_data_fetch = (TextView) findViewById(R.id.litter_data_fetch);
        litter_data_fetch_clean = (TextView) findViewById(R.id.litter_data_fetch_clean);
        litter_data_filter_by_date = (TextView) findViewById(R.id.litter_data_filter_by_date);
        litter_data_filter_by_quarter = (TextView) findViewById(R.id.litter_data_filter_by_quarter);
        litter_data_filter_by_type = (TextView) findViewById(R.id.litter_data_filter_by_type);
        litter_data_filter_by_date.setOnClickListener(this);
        litter_data_filter_by_quarter.setOnClickListener(this);
        litter_data_filter_by_type.setOnClickListener(this);
        if (!userid_fetch.getText().toString().equals("")) {
            userID_fetch = userid_fetch.getText().toString();
        }
        litter_data_fetch.setOnClickListener(this);
        litter_data_fetch_clean.setOnClickListener(this);
        mLitterDomainList = mLitterDao.queryLitterDataByUserID(userID_fetch);
        int litterlistSize = mLitterDomainList.size();
        if (litterlistSize == 0) {
            Toast.makeText(this, getResources().getString(R.string.litter_is_empty), Toast.LENGTH_LONG).show();
        } else {
            mLitterReportAdapter = new LitterDataReportAdapter(mLitterDomainList, this);
            lv_litter_data_list.setAdapter(mLitterReportAdapter);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (!userid_fetch.getText().toString().equals("")) {
            userID_fetch = userid_fetch.getText().toString();
        }
        mLitterDomainList = mLitterDao.queryLitterDataByUserID(userID_fetch);
        if (mLitterDomainList.size() != 0) {
            mLitterReportAdapter.refresh(mLitterDomainList);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.litter_data_fetch:
                if (userid_fetch.getText().toString().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.userid_query_hint), Toast.LENGTH_LONG).show();
                    break;
                }
                mLitterDomainList = mLitterDao.queryLitterDataByUserID(userid_fetch.getText().toString());

                Toast.makeText(this, "data size:" + String.valueOf(mLitterDomainList.size()), Toast.LENGTH_LONG).show();
                for (int i = 0; i < mLitterDomainList.size(); i++) {
                    Log.i(TAG,
                            "detail:" + "userID:" + String.valueOf(mLitterDomainList.get(i).getUserId()) + " LittertypeID:"
                                    + String.valueOf(mLitterDomainList.get(i).getLittertypeID()) + " Weight:"
                                    + String.valueOf(mLitterDomainList.get(i).getWeight()) + " Litterdate:"
                                    + String.valueOf(mLitterDomainList.get(i).getLitterdate()));
                }
                mLitterReportAdapter.refresh(mLitterDomainList);
                break;
            case R.id.litter_data_fetch_clean:
                userid_fetch.setText("");
                userID_fetch = "0";
                mLitterDomainList = mLitterDao.queryLitterDataByUserID(userID_fetch);
                mLitterReportAdapter.refresh(mLitterDomainList);
                break;
            case R.id.litter_data_filter_by_date:
                break;
            case R.id.litter_data_filter_by_quarter:
                break;
            case R.id.litter_data_filter_by_type:
                break;
            default:
                break;
        }
    }

}
