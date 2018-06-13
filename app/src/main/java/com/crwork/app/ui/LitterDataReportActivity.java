package com.crwork.app.ui;

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
import com.crwork.app.adapter.LDPAdapter;
import com.crwork.app.mysql.dao.LitterDao;
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

    private String userID_fetch = "0";
    private LitterDao mLitterDao;
    private ArrayList<LitterDomain> mLitterDomainList =null;
    private List<Map<String, Object>> dataList;

    private TextView litter_data_filter_by_date, litter_data_filter_by_quarter, litter_data_filter_by_type;
    private TextView litter_data_fetch, litter_data_fetch_clean;
    private ListView lv_litter_data_list;
    private LDPAdapter mLitterReportAdapter;
    private EditText userid_fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.litter_data_report);
        mLitterDao = new LitterDao(this);
        initview();
    }

    private void initview() {
        // TODO Auto-generated method stub
        lv_litter_data_list = (ListView) findViewById(R.id.lv_litter_data_list);
        mLitterReportAdapter = new LDPAdapter(mLitterDomainList, this);
        lv_litter_data_list.setAdapter(mLitterReportAdapter);

        userid_fetch = (EditText) findViewById(R.id.userid_fetch);

        litter_data_fetch = (TextView) findViewById(R.id.litter_data_fetch);
        litter_data_fetch_clean = (TextView) findViewById(R.id.litter_data_fetch_clean);
        litter_data_filter_by_date = (TextView) findViewById(R.id.litter_data_filter_by_date);
        litter_data_filter_by_quarter = (TextView) findViewById(R.id.litter_data_filter_by_quarter);
        litter_data_filter_by_type = (TextView) findViewById(R.id.litter_data_filter_by_type);

        litter_data_filter_by_date.setOnClickListener(this);
        litter_data_filter_by_quarter.setOnClickListener(this);
        litter_data_filter_by_type.setOnClickListener(this);

        litter_data_fetch.setOnClickListener(this);
        litter_data_fetch_clean.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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
