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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.crwork.app.R;
import com.crwork.app.domain.LitterTypeDomain;
import com.crwork.app.mysql.dao.LitterTypeDao;
import com.crwork.app.sqlite.dao.LitterTypeDaoS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LitterTypeManagement extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = "LitterTypeManagement";

    private int littertypeID = 0;
    private int typemark;
    private int ID = 0;

    private Context mContext;
    private EditText littertype_ID, littertype_name, littertype_price;
    private Button littertype_add, littertype_modify, littertype_reset;
    private CheckBox littertype_mark;
    private ListView littertype_list_lv;


    private ArrayList<LitterTypeDomain> mLitterTypeDomainList;
    private SimpleAdapter mLitterTypeAdapter = null;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_littertype_management);
        mContext = this;
        initView();
        GetLitterTypeListTask mGetLitterTypeListTask = new GetLitterTypeListTask();
        mGetLitterTypeListTask.execute();
    }

    private void initView() {
        littertype_ID = findViewById(R.id.littertype_ID);
        littertype_name = findViewById(R.id.littertype_name);
        littertype_price = findViewById(R.id.littertype_price);

        littertype_add = findViewById(R.id.littertype_add);
        littertype_modify = findViewById(R.id.littertype_modify);
        littertype_reset = findViewById(R.id.littertype_reset);

        littertype_mark = findViewById(R.id.littertype_mark);

        littertype_list_lv = (ListView) findViewById(R.id.littertype_list_lv);
        mLitterTypeAdapter = new SimpleAdapter(mContext, list, R.layout.littertype_data_list_item, new String[]{"ID", "littertypeID", "typeName", "typemark", "price"},
                new int[]{R.id.ID, R.id.littertypeID, R.id.litterTypeName, R.id.litterTypeMark, R.id.litterTypePrice});
        littertype_list_lv.setAdapter(mLitterTypeAdapter);
        littertype_list_lv.setOnItemClickListener(this);

        littertype_add.setOnClickListener(this);
        littertype_modify.setOnClickListener(this);
        littertype_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.littertype_add:
                LitterTypeTask aLitterTypeTask = new LitterTypeTask();
                aLitterTypeTask.execute("add");
                break;
            case R.id.littertype_modify:
                LitterTypeTask mLitterTypeTask = new LitterTypeTask();
                mLitterTypeTask.execute("modify");
                break;
            case R.id.littertype_reset:
                littertype_ID.setText("");
                littertype_name.setText("");
                littertype_price.setText("");
                littertype_mark.setChecked(false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ID = (Integer) list.get(position).get("ID");

        littertypeID = (Integer) list.get(position).get("littertypeID");
        littertype_ID.setText(String.valueOf(littertypeID));

        littertype_name.setText(String.valueOf(list.get(position).get("typeName")));

        littertype_price.setText(String.valueOf(list.get(position).get("price")));

        typemark = (Integer) list.get(position).get("typemark");
        littertype_mark.setChecked(typemark == 1);
    }

    /**
     * 获取所有垃圾类型
     */
    private class GetLitterTypeListTask extends AsyncTask<String, Object, ArrayList<LitterTypeDomain>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ArrayList<LitterTypeDomain> doInBackground(String... params) {
            LitterTypeDao mLitterTypeDao = new LitterTypeDao();
            return mLitterTypeDao.getLitterTypes();
        }

        @Override
        protected void onPostExecute(ArrayList<LitterTypeDomain> litterTypeDomains) {
            super.onPostExecute(litterTypeDomains);
            list.clear();
            if (litterTypeDomains != null && litterTypeDomains.size() > 0) {
                for (LitterTypeDomain litterTypeDomain : litterTypeDomains) {
                    Log.i(TAG, "ID:" + litterTypeDomain.getID()
                            + " LitterTypeID:" + litterTypeDomain.getLittertypeID()
                            + " TypeName:" + litterTypeDomain.getTypeName()
                            + " TypeMark:" + litterTypeDomain.getTypemark()
                            + " Price:" + litterTypeDomain.getPrice() + "\n");
                    LitterTypeDaoS mLitterTypeDaoS =new LitterTypeDaoS(mContext);
                    mLitterTypeDaoS.insertLitterType(litterTypeDomain);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ID", litterTypeDomain.getID());
                    map.put("littertypeID", litterTypeDomain.getLittertypeID());
                    map.put("typeName", litterTypeDomain.getTypeName());
                    map.put("typemark", litterTypeDomain.getTypemark());
                    map.put("price", litterTypeDomain.getPrice());
                    list.add(map);
                }
                Toast.makeText(mContext, "数据查询成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "数据查询为空", Toast.LENGTH_LONG).show();
            }
            mLitterTypeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 类型操作
     */
    private class LitterTypeTask extends AsyncTask<String, Object, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            LitterTypeDao mLitterTypeDao = new LitterTypeDao();
            LitterTypeDomain litterTypeDomain = new LitterTypeDomain();
            litterTypeDomain.setID(ID);
            litterTypeDomain.setLittertypeID(Integer.parseInt(littertype_ID.getText().toString()));
            litterTypeDomain.setTypeName(littertype_name.getText().toString());
            litterTypeDomain.setTypemark(littertype_mark.isChecked() ? 1 : 0);
            litterTypeDomain.setPrice(Double.parseDouble(littertype_price.getText().toString()));
            Log.i(TAG, params[0]);
            Log.i(TAG, "ID:" + litterTypeDomain.getID()
                    + " LitterTypeID:" + litterTypeDomain.getLittertypeID()
                    + " TypeName:" + litterTypeDomain.getTypeName()
                    + " TypeMark:" + litterTypeDomain.getTypemark()
                    + " Price:" + litterTypeDomain.getPrice() + "\n");
            return params[0].equals("add") ? mLitterTypeDao.AddLitterType(litterTypeDomain) : mLitterTypeDao.ModifyLitterType(litterTypeDomain);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                GetLitterTypeListTask mGetLitterTypeListTask = new GetLitterTypeListTask();
                mGetLitterTypeListTask.execute();
                Toast.makeText(mContext, "操作成功！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "操作失败！", Toast.LENGTH_LONG).show();
            }
        }
    }
}
