package com.crwork.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.domain.UserDomain;
import com.crwork.app.net.NetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "LoginActivity";
    private Context mContext;

    private String userId = "";
    private String psw = "";

    private EditText et_userId_login, et_psw_login;
    private Button login_btn;
    private TextView tv_login_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        et_userId_login = findViewById(R.id.userId_login);
        et_psw_login = findViewById(R.id.psw_login);

        tv_login_result = findViewById(R.id.login_result);

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        UserLoginAsyncTask mUserLoginAsyncTask = new UserLoginAsyncTask();
        mUserLoginAsyncTask.execute(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_USER_LOGIN, et_userId_login.getText().toString(), et_psw_login.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                userId = et_userId_login.getText().toString();
                psw = et_psw_login.getText().toString();
                if (userId.equals("") || psw.equals("")) {
                    Toast.makeText(mContext, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                    break;
                }
                UserLoginAsyncTask mUserLoginAsyncTask = new UserLoginAsyncTask();
                mUserLoginAsyncTask.execute(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_USER_LOGIN, userId, psw);
                break;
        }
    }

    /**
     * 登录 AsyncTask
     */
    private class UserLoginAsyncTask extends AsyncTask<String, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_login_result.setText("正在登录...");
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("userId", params[1]);
            map.put("psw", params[2]);
            return new NetUtil().GetDataByPOST(params[0], map);
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            System.out.print(TAG + result_msg);
            String result_tip = "";
            if (!result_msg.equals("fail")) {
                result_tip = "登录成功";
                Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                UserDomain mUserDomain = mGson.fromJson(result_msg, UserDomain.class);
                SharedPreferences sp = getSharedPreferences("user_login_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userId", mUserDomain.getUserId());
                editor.putString("userName", mUserDomain.getUserName());
                editor.putInt("regionID", mUserDomain.getRegionID());
                editor.putInt("userType", mUserDomain.getUserType());
                editor.putString("registeredDate", mUserDomain.getRegisteredDate().toString());
                editor.commit();
                Intent mHomeActivity = new Intent(mContext, HomeActivity.class);
                startActivity(mHomeActivity);
                finish();
            } else {
                result_tip = "登录失败";
            }
            Toast.makeText(mContext, result_tip, Toast.LENGTH_SHORT).show();
        }
    }
}
