package com.study.gmall0513.mock;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 发送日志工具类
 * 通过http方法发送到采集系统的web端口
 */
public class LogUploader {
    public static void sendLogStream(String log){
        try{
            //不同的日志类型对应不同的URL

            /**
             * 可以在自己win环境应射服务url地址，在Windows\System32\drivers\etc\hosts
             *
             * 添加映射参数：
             * 192.168.126.199   logserver
             * 192.168.126.199   publisher
             */

            URL url  =new URL("http://logserver:8080/log");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为post
            conn.setRequestMethod("POST");

            //时间头用来供server进行时钟校对的
            conn.setRequestProperty("clientTime",System.currentTimeMillis() + "");
            //允许上传数据
            conn.setDoOutput(true);
            //设置请求的头信息,设置内容类型为JSON
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            System.out.println("upload" + log);

            //输出流
            OutputStream out = conn.getOutputStream();
            out.write(("logString="+log).getBytes());
            out.flush();
            out.close();
            int code = conn.getResponseCode();  //返回的状态码
            System.out.println(code);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}