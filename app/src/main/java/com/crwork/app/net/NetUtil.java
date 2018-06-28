package com.crwork.app.net;

import android.util.Log;

import com.crwork.app.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class NetUtil {
    private final static String TAG = "NetUtil";
    public final static String ACTION_URL_HEAD = "http://66.98.126.237:8080/CRWork_Web/servlet/";
    public final static String ACTION_UPLOAD_FILE = "UploadFileServlet";
    public final static String ACTION_GETCITYS = "MCitysServlet";
    public final static String ACTION_USER_LOGIN="MUserLoginServlet";
    public final static String ACTION_USER="MUserServlet";
    public final static String ACTION_USER_ADD="MUserAddServlet";
    public final static String ACTION_USER_MODIFY="MUserModifyServlet";
    public final static String ACTION_LITTER = "MLitterServlet";

    public NetUtil() {
        System.out.print(TAG + " init !");
    }

    /**
     * 上传文件至Server的方法
     *
     * @param uploadFilePath
     */
    public String UploadSourceFile(String uploadFilePath) {
        String result_msg = "fail";
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String newName = new FileUtil().getUploadNewName();
        String actionUrl = ACTION_URL_HEAD + ACTION_UPLOAD_FILE;
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // 设置http连接属性
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);

            // 取得文件的FileInputStream
            FileInputStream fStream = new FileInputStream(uploadFilePath);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            if (con.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = con.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffermsg[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffermsg)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffermsg, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                System.out.print(TAG + " file upload success!");
                result_msg = new String(message.toByteArray());
            }
            /* 关闭DataOutputStream */
            ds.close();
            System.out.print(TAG + " result: " + result_msg);
            return result_msg;
        } catch (Exception e) {
            System.out.print(TAG + " file upload failed!");
            System.out.print(TAG + " result: " + result_msg);
            return result_msg;
        }
    }

    /**
     * Do POST
     *
     * @param url_str
     * @param params_origin
     * @return
     */
    public String GetDataByPOST(String url_str, Map<String, String> params_origin) {
        String result_msg = "fail";
        StringBuffer params = new StringBuffer();
        try {
            if (params != null && !params_origin.isEmpty()) {
                //迭代器
                for (Map.Entry<String, String> entry : params_origin.entrySet()) {
                    params.append(entry.getKey()).append("=").
                            append(URLEncoder.encode(entry.getValue(), "UTF-8")).
                            append("&");
                }
                params.deleteCharAt(params.length() - 1);
            }
            //初始化URL
            URL url = new URL(url_str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, " start!");
            //设置请求方式
            conn.setRequestMethod("POST");

            //设置超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            //设置允许输入
            conn.setDoInput(true);
            //设置允许输出
            conn.setDoOutput(true);

            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);

            //我们请求的数据

            //
            OutputStream out = conn.getOutputStream();

            out.write(params.toString().getBytes());
            out.flush();
            out.close();
            conn.connect();

            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                result_msg = new String(message.toByteArray());
                return result_msg;
            }
        } catch (MalformedURLException e) {
            System.out.print(TAG + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print(TAG + e);
            e.printStackTrace();
        }
        Log.i(TAG, " exit!");
        return result_msg;
    }

    /**
     * 格式化参数
     *
     * @param params_origin
     * @return
     */
    public static String ParamsEncoder(String params_origin) {
        String params = "";
        try {
            return URLEncoder.encode(params_origin, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }
}
