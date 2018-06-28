package com.crwork.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crwork.app.R;

public class SettingsActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "SettingsActivity";
    private TextView citys_management, users_management, types_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        citys_management = (TextView) findViewById(R.id.citys_management);
        users_management = (TextView) findViewById(R.id.users_management);
        types_management = (TextView) findViewById(R.id.types_management);
        citys_management.setOnClickListener(this);
        users_management.setOnClickListener(this);
        types_management.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.citys_management:
                Intent mCitysManagement = new Intent(this, CitysActivity.class);
                startActivity(mCitysManagement);
                break;
            case R.id.users_management:
                Intent mUsersManagement = new Intent(this, UsersActivity.class);
                startActivity(mUsersManagement);
                break;
            case R.id.types_management:
                Intent mLitterTypeManagement = new Intent(this, LitterTypeActivity.class);
                startActivity(mLitterTypeManagement);
                break;
        }
    }
}
