package com.crwork.app.util;

import android.icu.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final int DEF_DIV_SCALE = 10;
    private Double total_weight_ur = 0.00;
    private Double total_weight_r = 0.00;
    private Double total_weight_k = 0.00;
    private Double total_price_ur = 0.00;
    private Double total_price_r = 0.00;
    private Double total_price_k = 0.00;
    private int total_count = 0;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * 解析文件格式是否符合要求
     *
     * @param filePath
     * @return
     */
    public ArrayList<String[]> ParseLitterSourceDataFile(String filePath) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        BufferedReader reader = null;
        String temp;
        int line = 1;
        try {
            reader = new BufferedReader(new FileReader(new File(filePath)));
            while ((temp = reader.readLine()) != null) {
                System.out.println(TAG + line + ":" + temp + "\n");
                String[] list_temp = temp.split(" ");
                System.out.print(TAG + " weight:" + list_temp[2] + "\n");
                if (list_temp[1].equals("0")) {
                    total_weight_ur = Arith.add(total_weight_ur, Double.parseDouble(list_temp[2]));
                } else if (list_temp[1].equals("1")) {
                    total_weight_r = Arith.add(total_weight_r, Double.parseDouble(list_temp[2]));
                } else if (list_temp[1].equals("2")) {
                    total_weight_k = Arith.add(total_weight_k, Double.parseDouble(list_temp[2]));
                }
                total_count += 1;
                list.add(list_temp);
                line++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 检验是否是TXT文件
     *
     * @return
     */
    public boolean isFileValid(String filePath) {
        File mfile = new File(filePath);
        String mfileName = mfile.getName();
        if (mfileName.substring(mfileName.lastIndexOf(".") + 1).equals("txt")) {
            ArrayList<String[]> list = ParseLitterSourceDataFile(filePath);
            if (list != null && list.size() > 0) {
                return true;
            }
        }
        return false;

    }

    /**
     * 随机重命名上传文件
     *
     * @return
     */
    public String getUploadNewName() {
        Date date = new Date();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        return "ld_" + myFmt.format(date) + ".txt";
    }

    /**
     * 综合垃圾总重量
     *
     * @return
     */
    public Double getTotal_weight_ur() {
        return total_weight_ur;
    }

    /**
     * 可回收总重量
     *
     * @return
     */
    public Double getTotal_weight_r() {
        return total_weight_r;
    }

    /**
     * 厨余总重量
     *
     * @return
     */
    public Double getTotal_weight_k() {
        return total_weight_k;
    }

    /**
     * 获取上传数据条数
     *
     * @return
     */
    public int getTotalCount() {
        return total_count;
    }
}
