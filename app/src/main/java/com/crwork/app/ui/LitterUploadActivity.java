package com.crwork.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.net.NetUtil;
import com.crwork.app.util.FileUtil;

import java.io.File;

/**
 * LitterUploadActivity
 *
 * @author xiezhenlin
 */
public class LitterUploadActivity extends Activity implements OnClickListener {
    private final static String TAG = "LitterUploadActivity";
    private Context mContext;
    private final static int XN_LD_SELECTED = 1;
    private String filePath = "";

    private Button ld_loadfile_btn;
    private Button ld_upload_btn;
    private TextView ld_file_name;
    private TextView ld_dataload_tip;
    private TextView total_weight_r, total_weight_ur, total_weight_k;
    private TextView total_price_r, total_price_ur, total_price_k;
    private TextView total_price_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litter_data_upload);
        mContext = this;
        InitView();
    }

    private void InitView() {
        // TODO Auto-generated method stub

        ld_upload_btn = findViewById(R.id.ld_upload_btn);
        ld_upload_btn.setEnabled(false);
        ld_upload_btn.setTextColor(0xFFD0EFC6);
        ld_loadfile_btn = findViewById(R.id.ld_loadfile_btn);
        ld_upload_btn.setOnClickListener(this);
        ld_loadfile_btn.setOnClickListener(this);

        ld_file_name = findViewById(R.id.ld_file_name);
        ld_dataload_tip = findViewById(R.id.ld_dataload_tip);

        total_weight_r = findViewById(R.id.total_weight_r);
        total_weight_ur = findViewById(R.id.total_weight_ur);
        total_weight_k = findViewById(R.id.total_weight_k);

        total_price_r = findViewById(R.id.total_price_r);
        total_price_ur = findViewById(R.id.total_price_ur);
        total_price_k = findViewById(R.id.total_price_k);
        total_price_tip=findViewById(R.id.total_price_tip);
        total_price_r.setVisibility(View.GONE);
        total_price_ur.setVisibility(View.GONE);
        total_price_k.setVisibility(View.GONE);
        total_price_tip.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ld_loadfile_btn:
                openFileSelect();
                break;
            case R.id.ld_upload_btn:
                UploadDataSourceFileAsyncTask mUploadDataSourceFileAsyncTask = new UploadDataSourceFileAsyncTask();
                mUploadDataSourceFileAsyncTask.execute(filePath);
                break;
            default:
                break;

        }
    }

    /**
     * select litter data from phone storage
     */
    private void openFileSelect() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, LitterDataFileExplorerActivity.class);
        startActivityForResult(intent, XN_LD_SELECTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            filePath = data.getStringExtra("apk_path");
            ld_file_name.setText(new File(filePath).getName());
            ParseDataSourceFileAsyncTask mParseDataSourceFileAsyncTask = new ParseDataSourceFileAsyncTask();
            mParseDataSourceFileAsyncTask.execute(filePath);
        }
    }

    /**
     * 检查上传文件的是否符合要求
     *
     * @param isOk
     */
    private void SourceFileCheck(Boolean isOk, FileUtil mFileUtil) {
        ld_loadfile_btn.setEnabled(false);
        ld_loadfile_btn.setTextColor(0xFFD0EFC6);
        if (isOk) {
            ld_loadfile_btn.setEnabled(true);
            ld_loadfile_btn.setTextColor(0xFF000000);
            ld_upload_btn.setEnabled(true);
            ld_upload_btn.setTextColor(0xFF000000);
            total_weight_r.setText(String.valueOf(mFileUtil.getTotal_weight_r()));
            total_weight_ur.setText(String.valueOf(mFileUtil.getTotal_weight_ur()));
            total_weight_k.setText(String.valueOf(mFileUtil.getTotal_weight_k()));
            ld_dataload_tip.setText("数据文件正确，可以上传" + "\n\n" + "共" + String.valueOf(mFileUtil.getTotalCount() + "条"));
        } else {
            ld_loadfile_btn.setEnabled(true);
            ld_loadfile_btn.setTextColor(0xFF000000);
            ld_upload_btn.setEnabled(false);
            ld_upload_btn.setTextColor(0xFFD0EFC6);
            ld_dataload_tip.setText("数据文件错误，无法上传");
        }
    }

    /**
     * 上传数据源文件
     */
    private class UploadDataSourceFileAsyncTask extends AsyncTask<String, Object, String> {
        NetUtil mNetUtil;

        public UploadDataSourceFileAsyncTask() {
            super();
            mNetUtil = new NetUtil();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ld_upload_btn.setEnabled(false);
            ld_upload_btn.setTextColor(0xFFD0EFC6);
            ld_loadfile_btn.setEnabled(false);
            ld_loadfile_btn.setTextColor(0xFFD0EFC6);
            ld_dataload_tip.setText("数据上传中");
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            return mNetUtil.UploadSourceFile(params[0]);
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            if (!result_msg.equals("fail")) {
                ld_upload_btn.setEnabled(false);
                ld_upload_btn.setTextColor(0xFFD0EFC6);
                total_weight_r.setText("0.00");
                total_weight_ur.setText("0.00");
                total_weight_k.setText("0.00");
                ld_file_name.setText("");
                Toast.makeText(mContext, "~~~~~~~OK~~~~~~~", Toast.LENGTH_LONG).show();
                ld_dataload_tip.setText("数据上传成功");
            } else {
                ld_upload_btn.setEnabled(true);
                ld_upload_btn.setTextColor(0xFF000000);
                ld_dataload_tip.setText("数据上传失败");
            }
            ld_loadfile_btn.setEnabled(true);
            ld_loadfile_btn.setTextColor(0xFF000000);
        }
    }

    /**
     * 数据文件解析
     */
    private class ParseDataSourceFileAsyncTask extends AsyncTask<String, Object, Boolean> {
        FileUtil mFileUtil;

        public ParseDataSourceFileAsyncTask() {
            super();
            mFileUtil = new FileUtil();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ld_dataload_tip.setText("数据解析中...");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            SourceFileCheck(aBoolean, mFileUtil);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return mFileUtil.isFileValid(strings[0]);
        }

    }
}
