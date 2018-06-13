package com.crwork.app.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.domain.CitysDomain;
import com.crwork.app.domain.UserDomain;
import com.crwork.app.mysql.dao.UserDao;
import com.crwork.app.net.NetUtil;
import com.crwork.app.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersManagementActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "UsersManagementActivity";
    private Context mContext;
    private String parentId = "1";

    private Button user_add_bt;
    private CheckBox is_cr;
    private Spinner mSpinnerCitys;


    private SimpleAdapter mCitysAdapter = null;
    private List<Map<String, Object>> cityslistmap = new ArrayList<Map<String, Object>>();

    private ListView users_list_lv;
    private SimpleAdapter mUsersAdapter = null;
    private List<Map<String, Object>> userslist = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_management);
        mContext = this;
        InitView();
        GetUserListAsyncTask mGetUserListAsyncTask = new GetUserListAsyncTask();
        mGetUserListAsyncTask.execute();
        refreshListItems(parentId);
    }

    private void refreshListItems(String mparentId) {
        parentId = mparentId;
        GetCitysAsyncTask mCitysAsyncTask = new GetCitysAsyncTask();
        mCitysAsyncTask.execute(mparentId);
    }

    private void InitView() {
        user_add_bt = findViewById(R.id.user_add_bt);
        is_cr=findViewById(R.id.is_cr);

        mSpinnerCitys = findViewById(R.id.citys_list_sp);
        mCitysAdapter = new SimpleAdapter(this, cityslistmap, R.layout.citys_user_data_list_item, new String[]{"citys_id", "citys_parents_id", "city_level", "citys_name_zh"},
                new int[]{R.id.user_citys_id, R.id.user_citys_parents_id, R.id.user_city_level, R.id.user_citys_name_zh});
        mSpinnerCitys.setAdapter(mCitysAdapter);
        mSpinnerCitys.setOnItemSelectedListener(mcitysOnItemSelectedListener);

        users_list_lv = findViewById(R.id.users_list_lv);
        mUsersAdapter = new SimpleAdapter(this, userslist, R.layout.user_data_list_item, new String[]{"user_id", "user_name", "user_type", "city_name_zh", "user_registerdate"},
                new int[]{R.id.user_id, R.id.user_name, R.id.user_type, R.id.user_region, R.id.user_registerdate});
        users_list_lv.setAdapter(mUsersAdapter);

        user_add_bt.setOnClickListener(this);
    }

    private AdapterView.OnItemSelectedListener mcitysOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                parentId = String.valueOf(cityslistmap.get(position).get("citys_id"));
                refreshListItems(parentId);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_add_bt:
                AddUserAsyncTask mAddUserAsyncTask = new AddUserAsyncTask();
                mAddUserAsyncTask.execute("add");
                break;
        }
    }

    /**
     * get user list
     */
    private class GetUserListAsyncTask extends AsyncTask<String, Object, ArrayList<String[]>> {
        UserDao mUserDao = null;

        public GetUserListAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String[]> doInBackground(String... strings) {
            mUserDao = new UserDao();
            return mUserDao.getUserList();
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> userDomains) {
            super.onPostExecute(userDomains);
            userslist.clear();
            if (userDomains != null || userDomains.size() > 0) {
                for (String[] userDomain : userDomains) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("user_id", userDomain[1]);
                    map.put("user_name", userDomain[2]);
                    map.put("user_type", userDomain[4]);
                    map.put("city_name_zh", userDomain[9]);
                    map.put("user_registerdate", userDomain[5]);
                    userslist.add(map);
                    Log.i(TAG, "userId: " + userDomain[1] + " UserName:" + userDomain[2] + " city_name_zh:" + userDomain[9] + "\n");
                }
            }
            mUsersAdapter.notifyDataSetChanged();
        }
    }

    /**
     * add user
     */
    private class AddUserAsyncTask extends AsyncTask<String, Object, Boolean> {
        UserDao mUserDao = null;

        public AddUserAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mContext, "操作提交！", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            mUserDao = new UserDao();
            UserDao mUserDao = new UserDao();
            UserDomain mUserDomain = new UserDomain();
            mUserDomain.setUserId("198910");
            mUserDomain.setUserName("Tester2");
            mUserDomain.setRegionID(742037);
            mUserDomain.setUserType(2);
            mUserDomain.setRegisteredDate(DateUtil.getCurrentDate());
            mUserDomain.setPsw("xzl198819");
            return strings[0].equals("add") ? mUserDao.AddUser(mUserDomain) : mUserDao.AddUser(mUserDomain);
        }

        @Override
        protected void onPostExecute(Boolean successed) {
            super.onPostExecute(successed);
            if (successed) {

                Toast.makeText(mContext, "操作成功！", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(mContext, "操作失败！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * get city list
     */
    private class GetCitysAsyncTask extends AsyncTask<String, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return new NetUtil().GetCitysList(params[0], "0", "getcitylist");
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            if (!result_msg.equals("fail")) {
                Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                ArrayList<CitysDomain> cityslist = mGson.fromJson(result_msg, new TypeToken<List<CitysDomain>>() {
                }.getType());
                System.out.print(TAG + "data size:" + cityslist.size());
                cityslistmap.clear();
                if (cityslist != null && cityslist.size() > 0) {
                    for (CitysDomain mCitysDomain : cityslist) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("citys_id", mCitysDomain.getId());
                        map.put("citys_parents_id", mCitysDomain.getParent_id());
                        map.put("city_level", mCitysDomain.getCity_level());
                        map.put("citys_name_zh", mCitysDomain.getCity_name_zh());
                        cityslistmap.add(map);
                    }
                } else {
                    Toast.makeText(mContext, "没有下级菜单了！", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, "网络出错了！", Toast.LENGTH_LONG).show();
            }
            mCitysAdapter.notifyDataSetChanged();
        }
    }

}
