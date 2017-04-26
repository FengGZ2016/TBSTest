package example.com.tbstest;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 国富小哥 on 2017/4/8.
 */

public class HttpUtil {
    //利用HttpURLConnection发送网络请求的方法
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        //开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL(address);
                    connection= (HttpURLConnection) url.openConnection();
                    //请求方式
                    connection.setRequestMethod("GET");
                    //设置连接超时的毫秒数
                    connection.setConnectTimeout(8000);
                    //设置读取超时的毫秒数
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //获取输入流
                    InputStream in=connection.getInputStream();
                    //根据获取到的输入流进行读取
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }if(listener!=null){
                        //回调onFinish()方法
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                   if(listener!=null){
                       //回调onError()方法
                       listener.onError(e);
                       //调用自定义的Myapplication获取Context对象
                       Toast.makeText(MyApplication.getContext(), "发生异常啦！！！", Toast.LENGTH_SHORT).show();
                   }
                }finally {
                    if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }

    /**
     * 利用OKHTTP发送网络请求的方法
     * */
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
