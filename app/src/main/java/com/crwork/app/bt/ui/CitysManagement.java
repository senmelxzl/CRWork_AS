package com.crwork.app.bt.ui;

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
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.dao.CitysDao;
import com.crwork.app.domain.CitysDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitysManagement extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final static String TAG = "CitysManagement";
    private Context mContext;

    private int citys_add_status = 0;
    private String parentId = "1";
    private String currentcitys = "中国";
    private int city_level = 1;
    private ArrayList<CitysDomain> mCitysDomainList;

    private ListView citys_lv;
    private SimpleAdapter mCitysAdapter = null;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    private TextView citys_current_name_zh;
    private Button citys_add_bt, citys_parent_bt, citys_reload_bt;
    private EditText et_citys_name_zh;
    private CheckBox cb_citys_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citys_management);
        mContext = this;
        initView();
    }

    private void initView() {
        citys_current_name_zh = findViewById(R.id.citys_current_name_zh);

        citys_add_bt = findViewById(R.id.citys_add_bt);
        citys_parent_bt = findViewById(R.id.citys_parent_bt);
        citys_reload_bt = findViewById(R.id.citys_reload_bt);

        et_citys_name_zh = findViewById(R.id.et_citys_name_zh);

        cb_citys_status = findViewById(R.id.cb_citys_status);

        citys_lv = findViewById(R.id.citys_list);
        mCitysAdapter = new SimpleAdapter(this, list, R.layout.citys_data_list_item, new String[]{"citys_id", "citys_parents_id", "city_level", "citys_name_zh"},
                new int[]{R.id.citys_id, R.id.citys_parents_id, R.id.city_level, R.id.citys_name_zh});
        citys_lv.setAdapter(mCitysAdapter);
        citys_lv.setOnItemClickListener(this);

        cb_citys_status.setOnCheckedChangeListener(this);

        citys_add_bt.setOnClickListener(this);
        citys_parent_bt.setOnClickListener(this);
        citys_reload_bt.setOnClickListener(this);
    }

    private void refreshListItems(String mparentId) {
        parentId = mparentId;
        DownloadTask mDownloadTask = new DownloadTask();
        mDownloadTask.execute(mparentId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.citys_add_bt:
                AddCitysTask mAddCitysTask = new AddCitysTask();
                mAddCitysTask.execute();
                break;
            case R.id.citys_parent_bt:
                refreshListItems(parentId);
                break;
            case R.id.citys_reload_bt:
                refreshListItems(parentId);
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
            refreshListItems(parentId);
        }
    }

    /**
     * 获取服务器城市数据列表
     */
    private class DownloadTask extends AsyncTask<String, Object, ArrayList<CitysDomain>> {
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
        protected ArrayList<CitysDomain> doInBackground(String... params) {
            CitysDao mCitysDao = new CitysDao();
            return mCitysDao.getCitys(Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(ArrayList<CitysDomain> cityslist) {
            super.onPostExecute(cityslist);
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
                    map.put("city_level", mCitysDomain.getCity_level());
                    map.put("citys_name_zh", mCitysDomain.getCity_name_zh());
                    list.add(map);
                }
            } else {
                Toast.makeText(mContext, "没有下级菜单了！", Toast.LENGTH_LONG).show();
            }
            mCitysAdapter.notifyDataSetChanged();
            citys_current_name_zh.setText(currentcitys);
            dialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String citys_id = String.valueOf(list.get(position).get("citys_id"));
        currentcitys = String.valueOf(list.get(position).get("citys_name_zh"));
        parentId = String.valueOf(list.get(position).get("citys_parents_id"));
        city_level = (Integer) list.get(position).get("city_level");
        Log.i(TAG, "citys_id is:" + citys_id);
        Log.i(TAG, "city_level is:" + city_level);
        Log.i(TAG, "currentcitys is:" + currentcitys);
        refreshListItems(citys_id);
    }
}
