package com.crwork.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.domain.CitysDomain;
import com.crwork.app.mysql.dao.CitysDao;
import com.crwork.app.net.NetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitysActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final static String TAG = "CitysActivity";
    private Context mContext;
    public static final String ACTION_GET_PRE_CITY_LIST = "getprecitylist";
    public static final String ACTION_GET_CITY_LIST = "getcitylist";
    public static final String ACTION_GET_CITY = "getcity";

    private int citys_add_status = 0;
    private String parentId = "1";
    private int city_level = 1;
    private ArrayList<CitysDomain> mCitysDomainList;

    private ListView citys_lv;
    private SimpleAdapter mCitysAdapter = null;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    private Button citys_add_bt, citys_parent_bt;
    private EditText et_citys_name_zh;
    private CheckBox cb_citys_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citys);
        mContext = this;
        initView();
        refreshListItems(parentId, ACTION_GET_CITY_LIST);
    }

    private void initView() {

        citys_add_bt = findViewById(R.id.citys_add_bt);
        citys_parent_bt = findViewById(R.id.citys_parent_bt);

        et_citys_name_zh = findViewById(R.id.et_citys_name_zh);

        cb_citys_status = findViewById(R.id.cb_citys_status);

        citys_lv = findViewById(R.id.citys_list);
        mCitysAdapter = new SimpleAdapter(this, list, R.layout.citys_data_list_item, new String[]{"citys_name_zh", "city_level", "city_status_cr"},
                new int[]{R.id.citys_name_zh, R.id.city_level, R.id.city_status_cr});
        citys_lv.setAdapter(mCitysAdapter);
        citys_lv.setOnItemClickListener(this);

        cb_citys_status.setOnCheckedChangeListener(this);

        citys_add_bt.setOnClickListener(this);
        citys_parent_bt.setOnClickListener(this);
    }

    private void refreshListItems(String mparentId, String citys_action) {
        parentId = mparentId;
        CitysTask mCitysTask = new CitysTask();
        mCitysTask.execute(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_GETCITYS, parentId, "0", citys_action);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.citys_add_bt:
                AddCitysTask mAddCitysTask = new AddCitysTask();
                mAddCitysTask.execute();
                break;
            case R.id.citys_parent_bt:
                refreshListItems(parentId, ACTION_GET_PRE_CITY_LIST);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            citys_add_status = 1;
        } else {
            citys_add_status = 0;
        }
    }

    private class AddCitysTask extends AsyncTask<String, Object, Boolean> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("正在新增");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("区域数据新增中，请稍候......");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            CitysDao mCitysDao = new CitysDao();
            CitysDomain mCitysDomain = new CitysDomain();
            mCitysDomain.setParent_id(Integer.valueOf(parentId));
            mCitysDomain.setCity_name_zh(et_citys_name_zh.getText().toString().trim());
            mCitysDomain.setCity_level(city_level + 1);
            mCitysDomain.setCity_code("445281121224");
            mCitysDomain.setCity_status_cr(citys_add_status);
            return mCitysDao.AddCitys(mCitysDomain);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b) {
                Toast.makeText(mContext, "添加成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "添加失败", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
            refreshListItems(parentId, ACTION_GET_CITY_LIST);
        }
    }

    /**
     * 获取服务器城市数据列表
     */
    private class CitysTask extends AsyncTask<String, Object, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("正在加载");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("区域数据加载中......");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("parent_id", params[1]);
            map.put("id", params[2]);
            map.put("citys_action", params[3]);
            return new NetUtil().GetDataByPOST(params[0], map);
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            if (!result_msg.equals("fail")) {
                Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                ArrayList<CitysDomain> cityslist = mGson.fromJson(result_msg, new TypeToken<List<CitysDomain>>() {
                }.getType());
                System.out.print(TAG + "data size:" + cityslist.size());
                list.clear();
                if (cityslist != null && cityslist.size() > 0) {
                    for (int i = 0; i < cityslist.size(); i++) {
                        Log.i(TAG, "城市：" + cityslist.get(i).getCity_name_zh());
                    }
                    for (CitysDomain mCitysDomain : cityslist) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("citys_id", mCitysDomain.getId());
                        map.put("citys_parents_id", mCitysDomain.getParent_id());
                        parentId = String.valueOf(mCitysDomain.getParent_id());
                        map.put("city_level", (mCitysDomain.getCity_level() - 1) + "级区域");
                        city_level = mCitysDomain.getCity_level();
                        map.put("citys_name_zh", mCitysDomain.getCity_name_zh());
                        map.put("city_status_cr", mCitysDomain.getCity_status_cr() == 0 ? "未分类" : "已分类");
                        list.add(map);
                    }
                } else {
                    Toast.makeText(mContext, "没有下级菜单了！", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, "网络出错了！", Toast.LENGTH_LONG).show();
            }
            mCitysAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String citys_id = String.valueOf(list.get(position).get("citys_id"));
        parentId = String.valueOf(list.get(position).get("citys_parents_id"));
        Log.i(TAG, "citys_id is:" + citys_id);
        refreshListItems(citys_id, ACTION_GET_CITY_LIST);
    }
}
