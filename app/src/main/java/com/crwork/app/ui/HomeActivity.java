package com.crwork.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.debug.ClientActivity;
import com.crwork.app.debug.NFCActivity;
import com.crwork.app.debug.ServerActivity;
import com.crwork.app.util.ActivityController;
import com.crwork.app.util.NullController;
import com.crwork.app.util.PermissionHelper;

import java.util.List;

/**
 * s activity_home activity_home activity
 *
 * @author xiezhenlin
 */
public class HomeActivity extends Activity implements OnClickListener {
    private final static String TAG = "HomeActivity";
    private final static boolean LOGV_ENABLED = false;

    private Button mBtnWeigh, mBtnReport, mBtnUpload, mBtnJoin;
    private TextView login_userName, login_userType;

    private boolean mAllGranted;
    private ActivityController mController = NullController.INSTANCE;
    private PowerManager pm;
    private PowerManager.WakeLock wl;

    private String loginUserName = "";
    private int loginUserType = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAllGranted = false;
        // check all application permissions
        PermissionHelper.init(this);
        setContentView(R.layout.activity_home);
        initView();
        initLogindata();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        wl.acquire();
    }

    /**
     * 获取用户登录信息
     */
    private void initLogindata() {
        SharedPreferences sp = getSharedPreferences("user_login_data", Context.MODE_PRIVATE);
        loginUserName = sp.getString("userName", "");
        loginUserType = sp.getInt("userType", 2);
        login_userName.setText(loginUserName);
        login_userType.setText(loginUserType == 0 ? "超级管理员" : "管理员");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (LOGV_ENABLED) {
            MenuItem action_client = menu.findItem(R.id.action_client);
            action_client.setVisible(true);
            MenuItem action_server = menu.findItem(R.id.action_server);
            action_server.setVisible(true);
            MenuItem action_nfc = menu.findItem(R.id.action_nfc);
            action_nfc.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.action_client:
                Intent client = new Intent(this, ClientActivity.class);
                startActivity(client);
                break;
            case R.id.action_server:
                Intent server = new Intent(this, ServerActivity.class);
                startActivity(server);
                break;
            case R.id.action_nfc:
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
                if (nfcAdapter == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_nfc_notfound),
                            Toast.LENGTH_LONG).show();
                    break;
                }
                Intent nfc = new Intent(this, NFCActivity.class);
                startActivity(nfc);
                break;
            case R.id.action_analysis:// analysis litter data
                Intent analysis = new Intent(this, UploadDataFileActivity.class);
                startActivity(analysis);
                break;
            default:
                break;
        }
        return true;
    }

    private void initView() {
        login_userName = findViewById(R.id.login_userName);
        login_userType = findViewById(R.id.login_userType);
        mBtnWeigh = (Button) findViewById(R.id.btn_weighed);
        mBtnReport = (Button) findViewById(R.id.btn_report);
        mBtnUpload = (Button) findViewById(R.id.btn_upload);
        mBtnJoin = (Button) findViewById(R.id.btn_join);
        mBtnWeigh.setOnClickListener(this);
        mBtnReport.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
        mBtnJoin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAllGranted) {
            doResume();
        } else {
            List<String> permissionsRequestList = PermissionHelper.getInstance().getAllUngrantedPermissions();
            if (permissionsRequestList.size() > 0) {
                PermissionHelper.getInstance().requestPermissions(permissionsRequestList, mPermissionCallback);
            } else {
                doResume();
            }
        }
    }

    private void doResume() {
        if (LOGV_ENABLED) {
            Log.v(TAG, "BrowserActivity.onResume: this=" + this);
        }
        mController.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_weighed:
                Intent client = new Intent(this, LitterWeighActivity.class);
                startActivity(client);
                break;
            case R.id.btn_report:
                Intent report = new Intent(this, ExportReportActivity.class);
                startActivity(report);
                break;
            case R.id.btn_upload:
                Intent analysis = new Intent(this, UploadDataFileActivity.class);
                startActivity(analysis);
                break;
            case R.id.btn_join:
                Intent settings = new Intent(this, JoinCRActivity.class);
                startActivity(settings);
                break;
            default:
                break;
        }
    }

    private PermissionHelper.PermissionCallback mPermissionCallback = new PermissionHelper.PermissionCallback() {
        public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (grantResults != null && grantResults.length > 0) {
                mAllGranted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mAllGranted = false;
                        Log.d(TAG, permissions[i] + " is not granted !");
                        break;
                    }
                }
                if (!mAllGranted) {
                    String toastStr = getString(R.string.denied_required_permission);
                    Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_LONG).show();
                    finish();
                }
                doResume();
            }
        }
    };
}
