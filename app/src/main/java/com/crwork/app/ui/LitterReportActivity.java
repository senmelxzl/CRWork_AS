package com.crwork.app.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.domain.CitysDomain;
import com.crwork.app.net.NetUtil;
import com.crwork.app.util.Arith;
import com.crwork.app.util.DateUtil;
import com.crwork.app.util.ExcelUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LitterReportActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "LitterReportActivity";
    private Context mContext;
    String data_search = "";
    private String data_search_start;
    private String data_search_end;
    private String parentId = "1";
    private String ld_region = "";

    private Button explore_submit, export_submit, reset_submit;
    private TextView start_date_values, end_date_values;
    private Spinner mSpinnerCitys;
    private EditText ld_username_et;


    private SimpleAdapter mCitysAdapter = null;
    private List<Map<String, Object>> cityslistmap = new ArrayList<Map<String, Object>>();
    private ArrayList<String[]> mLitterModelList = new ArrayList<String[]>();

    private TextView total_weight_ur_tv, total_weight_r_tv, total_weight_k_tv;
    private TextView total_price_ur_tv, total_price_r_tv, total_price_k_tv;

    private TextView total_price_mng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litter_report);
        mContext = this;
        refreshListItems(parentId);
        initView();
    }

    /**
     * init view
     */
    private void initView() {
        explore_submit = findViewById(R.id.explore_submit);
        export_submit = findViewById(R.id.export_submit);
        explore_submit.setOnClickListener(this);
        export_submit.setOnClickListener(this);
        reset_submit = findViewById(R.id.reset_submit);
        reset_submit.setOnClickListener(this);

        start_date_values = findViewById(R.id.start_date_values);
        end_date_values = findViewById(R.id.end_date_values);

        start_date_values.setOnClickListener(this);
        start_date_values.setText(DateUtil.getCurrentDate().toString());
        end_date_values.setOnClickListener(this);
        end_date_values.setText(DateUtil.getCurrentDate().toString());

        mSpinnerCitys = findViewById(R.id.citys_list_sp);
        mCitysAdapter = new SimpleAdapter(this, cityslistmap, R.layout.citys_user_data_list_item, new String[]{"citys_id", "citys_parents_id", "city_level", "citys_name_zh"},
                new int[]{R.id.user_citys_id, R.id.user_citys_parents_id, R.id.user_city_level, R.id.user_citys_name_zh});
        mSpinnerCitys.setAdapter(mCitysAdapter);
        mSpinnerCitys.setOnItemSelectedListener(mcitysOnItemSelectedListener);

        ld_username_et = findViewById(R.id.ld_username);

        total_weight_ur_tv = findViewById(R.id.total_weight_ur);
        total_weight_r_tv = findViewById(R.id.total_weight_r);
        total_weight_k_tv = findViewById(R.id.total_weight_k);
        total_price_ur_tv = findViewById(R.id.total_price_ur);
        total_price_r_tv = findViewById(R.id.total_price_r);
        total_price_k_tv = findViewById(R.id.total_price_k);

        total_price_mng = findViewById(R.id.total_price_mng);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.explore_submit:
                /*if (DateUtil.compare_date(end_date_values.getText().toString(), start_date_values.getText().toString()) != 1) {
                    Toast.makeText(mContext, "请选择正确日期", Toast.LENGTH_SHORT).show();
                    break;
                }*/
                GetLittersTask mGetLittersTask = new GetLittersTask();
                mGetLittersTask.execute(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_LITTER, ld_username_et.getText().toString(),
                        ld_region, start_date_values.getText().toString(), end_date_values.getText().toString(), "ld_search", "");
                break;
            case R.id.export_submit:
                if (mLitterModelList == null || mLitterModelList.size() == 0) {
                    Toast.makeText(mContext, "数据源为空，请加载...", Toast.LENGTH_SHORT).show();
                    break;
                }
                String exportResult_msg = "导出失败...";
                if (new ExcelUtil().ExportLDToExcel("新村", start_date_values.getText().toString(), end_date_values.getText().toString(), mLitterModelList)) {
                    exportResult_msg = "导出成功...";
                }
                Toast.makeText(mContext, exportResult_msg, Toast.LENGTH_SHORT).show();
                break;
            case R.id.start_date_values:
                showDatePicker(start_date_values);
                break;
            case R.id.end_date_values:
                showDatePicker(end_date_values);
                break;
        }
    }

    private void showDatePicker(final TextView date_select) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePicker = new DatePickerDialog(mContext, null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setCanceledOnTouchOutside(true);
        datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定的逻辑代码在监听中实现
                        DatePicker picker = datePicker.getDatePicker();
                        int year = picker.getYear();
                        int monthOfYear = picker.getMonth();
                        int dayOfMonth = picker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        date_select.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).toString());
                    }
                });
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消什么也不用做
                    }
                });
        datePicker.show();
    }

    private AdapterView.OnItemSelectedListener mcitysOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                parentId = String.valueOf(cityslistmap.get(position).get("citys_id"));
                ld_region = String.valueOf(cityslistmap.get(position).get("citys_name_zh"));
                refreshListItems(parentId);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void refreshListItems(String mparentId) {
        GetCitysAsyncTask mCitysAsyncTask = new GetCitysAsyncTask();
        mCitysAsyncTask.execute(NetUtil.ACTION_URL_HEAD + NetUtil.ACTION_GETCITYS, mparentId, "0", "getcitylist");
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
            Map<String, String> map = new HashMap<>();
            map.put("parent_id", params[1]);
            map.put("id", params[2]);
            map.put("citys_action", params[3]);
            return new NetUtil().GetDataByPOST(params[0], map);
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            System.out.print(TAG + " result_msg:" + result_msg + "\n");
            if (!result_msg.equals("fail")) {
                Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                ArrayList<CitysDomain> cityslist = mGson.fromJson(result_msg, new TypeToken<List<CitysDomain>>() {
                }.getType());
                System.out.print(TAG + " data size:" + cityslist.size() + "\n");
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
                    Toast.makeText(mContext, "没有更多菜单啦！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "网络出错了！", Toast.LENGTH_SHORT).show();
            }
            mCitysAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取垃圾数据列表
     */
    private class GetLittersTask extends AsyncTask<String, Object, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("正在加载");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("垃圾数据加载中......");
            dialog.show();
            System.out.print(TAG + " Litter data query start!" + "\n");
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("ld_username", params[1]);
            map.put("ld_region", params[2]);
            map.put("ld_start_date", params[3]);
            map.put("ld_end_date", params[4]);
            map.put("ld_search", params[5]);
            map.put("ld_export", params[6]);
            for (int i = 0; i < params.length; i++) {
                System.out.print(TAG + " params:" + params[i] + "\n");
            }
            return new NetUtil().GetDataByPOST(params[0], map);
        }

        @Override
        protected void onPostExecute(String result_msg) {
            super.onPostExecute(result_msg);
            System.out.print(TAG + " result_msg:" + result_msg + "\n");
            if (!result_msg.equals("fail")) {
                mLitterModelList.clear();
                Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                mLitterModelList = mGson.fromJson(result_msg, new TypeToken<List<String[]>>() {
                }.getType());
                System.out.print(TAG + " litter data size:" + mLitterModelList.size() + "\n");
                if (mLitterModelList != null && mLitterModelList.size() > 0) {
                    double total_weight = 0.00;
                    double total_weight_ur = 0.00;
                    double total_weight_r = 0.00;
                    double total_weight_k = 0.00;
                    double total_price_ur = 0.00;
                    double total_price_r = 0.00;
                    double total_price_k = 0.00;
                    double total_price_io = 0.00;
                    for (int i = 0; i < mLitterModelList.size(); i++) {
                        String litter_type = mLitterModelList.get(i)[5];
                        double litter_weight = Double.parseDouble(mLitterModelList.get(i)[3]);
                        double litter_price = Double.parseDouble(mLitterModelList.get(i)[6]);
                        if (litter_type.equals("0")) {
                            total_weight_ur = Arith.add(total_weight_ur, litter_weight);
                            total_price_ur = Arith.add(total_price_ur, litter_price);
                        } else if (litter_type.equals("1")) {
                            total_weight_r = Arith.add(total_weight_r, litter_weight);
                            total_price_r = Arith.add(total_price_r, litter_price);
                        } else if (litter_type.equals("2")) {
                            total_weight_k = Arith.add(total_weight_k, litter_weight);
                            total_price_k = Arith.add(total_price_k, litter_price);
                        }
                        total_weight = Arith.add(total_weight, litter_weight);
                        for (int j = 0; j < mLitterModelList.get(i).length; j++) {
                            System.out.print(TAG + mLitterModelList.get(i)[j]);
                        }
                    }
                    //total weight
                    total_weight_ur_tv.setText(String.valueOf(total_weight_ur) + "kg");
                    total_weight_r_tv.setText(String.valueOf(total_weight_r) + "kg");
                    total_weight_k_tv.setText(String.valueOf(total_weight_k) + "kg");
                    //total price
                    total_price_ur_tv.setText("费用:" + String.valueOf(total_price_ur) + "元");
                    total_price_r_tv.setText("收入:" + String.valueOf(total_price_r) + "元");
                    total_price_k_tv.setText("价值:" + String.valueOf(total_price_k) + "元");

                    if (total_price_ur > total_price_r) {
                        total_price_mng.setText("总重量:" + total_weight + "kg" + "\n\n" + "支出:" + Arith.sub(total_price_ur, total_price_r) + "元");
                        //total_price_mng.setTextColor(getResources().getColor(R.color.dimgrey));
                        total_price_mng.setBackgroundColor(getResources().getColor(R.color.dimgrey));
                    } else {
                        total_price_mng.setText("总重量:" + total_weight + "kg" + "\n\n" + "盈利:" + Arith.sub(total_price_r, total_price_ur) + "元");
                        total_price_mng.setBackgroundColor(getResources().getColor(R.color.seagreen));
                    }
                    Toast.makeText(mContext, "共：" + mLitterModelList.size() + "条数据", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "没有下级菜单啦！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "网络出错啦！", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}
