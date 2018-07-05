package com.crwork.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.domain.CitysDomain;
import com.crwork.app.net.NetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UsersActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "UsersActivity";
    private Context mContext;

    private final static String USER_LIST = "user_list";
    private final static String USER_ADD = "user_add";
    private final static String USER_MODIFY = "user_modify";
    private final static String USER_DELETE = "user_delete";

    private String parentId = "1";
    private int usr_ID = 0;
    private int regionID = 0;

    private Button user_add_bt, user_modify_bt, user_delete_bt, user_reset_bt, citys_parent_bt;
    private CheckBox is_cr;
    private Spinner mSpinner_city;
    private EditText userId_add_et, userName_add_et;


    private SimpleAdapter mCitysAdapter = null;
    private List<Map<String, Object>> city_list_map = new ArrayList<Map<String, Object>>();

    private ListView users_list_lv;
    private SimpleAdapter mUsersAdapter = null;
    private ArrayList<String[]> user_list = new ArrayList<String[]>();
    private List<Map<String, Object>> user_list_map = new ArrayList<Map<String, Object>>();
    private TextView iscr_percent;

    private Spinner usertype_sp;
    private ArrayAdapter usertype_adapter;
    private boolean isSpinnerFirst = true;

    private Button user_import;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mContext = this;
        InitView();
        initLogindata();
        refreshUserListItems();
        refreshCityListItems(parentId, CitysActivity.ACTION_GET_CITY_LIST);
    }

    private void refreshUserListItems() {
        UserAsyncTask GUserAsyncTask = new UserAsyncTask();
        GUserAsyncTask.execute(USER_LIST);
    }

    /**
     * 获取用户登录信息
     */
    private void initLogindata() {
        SharedPreferences sp = getSharedPreferences("user_login_data", Context.MODE_PRIVATE);
        regionID = sp.getInt("regionID", 0);
    }

    private void refreshCityListItems(String mparentId, String citys_action) {
        parentId = mparentId;
        GetCitysAsyncTask mCitysAsyncTask = new GetCitysAsyncTask();
        mCitysAsyncTask.execute(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_GETCITYS, mparentId, "0", citys_action);
    }

    private void InitView() {
        user_add_bt = findViewById(R.id.user_add_bt);
        user_modify_bt = findViewById(R.id.user_modify_bt);
        user_delete_bt = findViewById(R.id.user_delete_bt);
        user_reset_bt = findViewById(R.id.user_reset_bt);
        citys_parent_bt = findViewById(R.id.citys_parent_bt);

        userId_add_et = findViewById(R.id.userId_add_et);
        userName_add_et = findViewById(R.id.userName_add_et);
        is_cr = findViewById(R.id.is_cr);

        mSpinner_city = findViewById(R.id.citys_list_sp);
        mCitysAdapter = new SimpleAdapter(this, city_list_map, R.layout.citys_user_data_list_item, new String[]{"citys_id", "citys_parents_id", "city_level", "citys_name_zh"},
                new int[]{R.id.user_citys_id, R.id.user_citys_parents_id, R.id.user_city_level, R.id.user_citys_name_zh});
        mSpinner_city.setAdapter(mCitysAdapter);
        mSpinner_city.setOnItemSelectedListener(mcitysOnItemSelectedListener);

        users_list_lv = findViewById(R.id.users_list_lv);
        mUsersAdapter = new SimpleAdapter(this, user_list_map, R.layout.activity_users_data_list_item, new String[]{"user_id", "user_name", "user_type_str", "city_name_zh", "user_registerdate"},
                new int[]{R.id.user_id, R.id.user_name, R.id.user_type, R.id.user_region, R.id.user_registerdate});
        users_list_lv.setAdapter(mUsersAdapter);
        users_list_lv.setOnItemClickListener(userOnItemClickListener);

        usertype_sp = findViewById(R.id.usertype_sp);
        usertype_adapter = ArrayAdapter.createFromResource(this, R.array.usertype_item, android.R.layout.simple_spinner_item);
        usertype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usertype_sp.setAdapter(usertype_adapter);
        usertype_sp.setOnItemSelectedListener(usertype_spOnItemSelectedListener);

        iscr_percent = findViewById(R.id.iscr_percent);

        user_import = findViewById(R.id.user_import);

        user_add_bt.setOnClickListener(this);
        user_modify_bt.setOnClickListener(this);
        user_delete_bt.setOnClickListener(this);
        user_reset_bt.setOnClickListener(this);
        citys_parent_bt.setOnClickListener(this);
        user_import.setOnClickListener(this);
    }

    /**
     * 用户类型选择
     */
    private AdapterView.OnItemSelectedListener usertype_spOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isSpinnerFirst) {
                //第一次初始化spinner时，不显示默认被选择的第一项即可
                view.setVisibility(View.INVISIBLE);
            }
            isSpinnerFirst = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    /**
     * 区域列表选中
     */
    private AdapterView.OnItemSelectedListener mcitysOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                parentId = String.valueOf(city_list_map.get(position).get("citys_id"));
                refreshCityListItems(parentId, CitysActivity.ACTION_GET_CITY_LIST);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    /**
     * 用户列表点击
     */
    private AdapterView.OnItemClickListener userOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            isSpinnerFirst = false;
            usr_ID = Integer.parseInt(String.valueOf(user_list_map.get(position).get("ID")));
            userId_add_et.setText(String.valueOf(user_list_map.get(position).get("user_id")));
            userName_add_et.setText(String.valueOf(user_list_map.get(position).get("user_name")));
            is_cr.setChecked(String.valueOf(user_list_map.get(position).get("iscr")).equals("1"));
            usertype_sp.setSelection(Integer.parseInt(String.valueOf(user_list_map.get(position).get("user_type"))));
            user_delete_bt.setEnabled(true);
            user_modify_bt.setEnabled(true);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_add_bt:
                if (userId_add_et.getText().toString().equals("") || userName_add_et.getText().toString().equals("")) {
                    Toast.makeText(mContext, "不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                UserAsyncTask AUserAsyncTask = new UserAsyncTask();
                AUserAsyncTask.execute(USER_ADD);
                break;
            case R.id.user_modify_bt:
                if (userId_add_et.getText().toString().equals("") || userName_add_et.getText().toString().equals("")) {
                    Toast.makeText(mContext, "不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                UserAsyncTask MUserAsyncTask = new UserAsyncTask();
                MUserAsyncTask.execute(USER_MODIFY);
                break;
            case R.id.user_delete_bt:
                UserAsyncTask DUserAsyncTask = new UserAsyncTask();
                DUserAsyncTask.execute(USER_DELETE);
                break;
            case R.id.user_reset_bt:
                reset();
                break;
            case R.id.citys_parent_bt:
                refreshCityListItems(parentId, CitysActivity.ACTION_GET_PRE_CITY_LIST);
                break;
            case R.id.user_import:
                Intent intent = new Intent(this, LitterDataFileExplorerActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    /**
     * 重置
     */
    private void reset() {
        usr_ID = 0;
        userId_add_et.setText("");
        userName_add_et.setText("");
        is_cr.setChecked(false);
        usertype_sp.setSelection(2);
        user_delete_bt.setEnabled(false);
        user_modify_bt.setEnabled(false);
        usertype_sp.getSelectedView().setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("apk_path");
            ExcelUtil mExcelUtil = new ExcelUtil();
            try {
                ArrayList<String[]> list = (ArrayList<String[]>) mExcelUtil.readExcel(filePath);
                for (String[] sds : list) {
                    System.out.print(TAG + " xiezhenlin" + " 姓名：" + sds[0] + " 人口：" + sds[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用户操作
     */
    private class UserAsyncTask extends AsyncTask<String, Object, String> {

        public UserAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mContext, "操作提交！", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("useraction", params[0]);
            if (!params[0].equals(USER_LIST)) {
                map.put("userId", userId_add_et.getText().toString());
                if (!params[0].equals(USER_DELETE)) {
                    map.put("userName", userName_add_et.getText().toString());
                    map.put("regionID", String.valueOf(regionID));
                    map.put("userType", String.valueOf(usertype_sp.getSelectedItemPosition()));
                    map.put("iscr", is_cr.isChecked() ? "1" : "0");
                    if (params[0].equals(USER_MODIFY)) {
                        map.put("ID", String.valueOf(usr_ID));
                    }
                }
            }
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                System.out.println(TAG + "key=" + entry.getKey() + "valve=" + entry.getValue());
            }
            return new NetUtil().GetDataByPOST(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_USER, map);
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            System.out.print(TAG + result_msg);
            if (!result_msg.equals("fail")) {
                if (!result_msg.equals("success")) {
                    user_list_map.clear();
                    Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    user_list = mGson.fromJson(result_msg, new TypeToken<List<String[]>>() {
                    }.getType());
                    if (user_list != null || user_list.size() > 0) {
                        int iscr_total = 0;
                        for (String[] user : user_list) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("ID", user[0]);
                            map.put("user_id", user[1]);
                            map.put("user_name", user[2]);
                            map.put("city_name_zh", user[3]);
                            map.put("regionID", user[4]);
                            map.put("user_type", user[5]);
                            map.put("user_type_str", user[5].equals("0") || user[5].equals("1") ? "管理员" : "普通用户");
                            map.put("user_registerdate", user[6]);
                            map.put("iscr", user[7]);
                            if (user[7].equals("1")) {
                                iscr_total += 1;
                            }
                            user_list_map.add(map);
                            StringBuffer sb_user = new StringBuffer();
                            for (String s_user : user) {
                                sb_user.append(s_user + ";");
                            }
                            Log.i(TAG, sb_user.toString() + "\n");
                        }
                        iscr_percent.setText("分类占比：" + String.valueOf(iscr_total) + "/" + String.valueOf(user_list.size()));
                    }
                    mUsersAdapter.notifyDataSetChanged();
                } else {
                    reset();
                    refreshUserListItems();
                    Toast.makeText(mContext, "操作成功！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "操作失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取区域列表
     */
    private class GetCitysAsyncTask extends AsyncTask<String, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                System.out.print(TAG + " city data size:" + cityslist.size() + "\n");
                city_list_map.clear();
                if (cityslist != null && cityslist.size() > 0) {
                    for (CitysDomain mCitysDomain : cityslist) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("citys_id", mCitysDomain.getId());
                        map.put("citys_parents_id", mCitysDomain.getParent_id());
                        parentId = String.valueOf(mCitysDomain.getParent_id());
                        map.put("city_level", mCitysDomain.getCity_level());
                        map.put("citys_name_zh", mCitysDomain.getCity_name_zh());
                        city_list_map.add(map);
                    }
                } else {
                    Toast.makeText(mContext, "没有下级菜单了！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "网络出错了！", Toast.LENGTH_SHORT).show();
            }
            mCitysAdapter.notifyDataSetChanged();
        }
    }

}
