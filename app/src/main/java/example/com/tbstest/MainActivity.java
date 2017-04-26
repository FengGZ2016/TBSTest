package example.com.tbstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private TextView mTextView;
    private Button btn_send_request_forHttpURLConnction;
    private Button btn_send_request_forOkhttp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_activity);
       // initView();
        init();

    }

    private void init() {
        mTextView= (TextView) findViewById(R.id.textview);
        btn_send_request_forHttpURLConnction= (Button) findViewById(R.id.btn_send_request_forHttpURLConnction);
        btn_send_request_forHttpURLConnction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address="https://www.baidu.com";
                HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        String responseData=response;
                        showResponse(responseData);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                //sendRequestWithHttpURLConnetion();
            }
        });

        btn_send_request_forOkhttp= (Button) findViewById(R.id.btn_send_request_forOkhttp);
        btn_send_request_forOkhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address="http://www.baidu.com";
                HttpUtil.sendOkHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                       //在这里处理具体的异常
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        String responseData=response.body().string();
                        showResponse(responseData);
                    }
                });
               // sendRequestWithOkhttp();
            }
        });
    }

    /**
     * 运用Okhttp发送网络请求的方法
     * */
    private void sendRequestWithOkhttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url("http://www.baidu.com").build();
                try {
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    //更新ui
                    showResponse(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 运用HttpURLConnetion发送网络请求的方法
     * */
    private void sendRequestWithHttpURLConnetion() {

        //开启子线程访问网络
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection Connection=null;
//                BufferedReader reader=null;
//                try {
//                    URL url=new URL("https://www.baidu.com");
//                    Connection= (HttpURLConnection) url.openConnection();
//                    //请求方式
//                    Connection.setRequestMethod("GET");
//                    //设置连接超时的毫秒数
//                    Connection.setConnectTimeout(8000);
//                    //设置读取超时的毫秒数
//                    Connection.setReadTimeout(8000);
//                    //获取输入流
//                    InputStream in=Connection.getInputStream();
//                    //根据获取到的输入流进行读取
//                    reader=new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response=new StringBuilder();
//                    String line;
//                    while ((line=reader.readLine())!=null){
//                        response.append(line);
//                    }
//                    //更新UI界面
//                    showResponse(response.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    //最后记得要回收！！！
//                    if (reader!=null){
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if(Connection!=null){
//                        Connection.disconnect();
//                    }
//                }
//            }
//        }).start();

    }

    /**
     * 在这里更新UI界面，把网络请求返回的消息显示在TEXTVIEW上
     * */
    private void showResponse(final String s) {
        //因为更新UI必须要在主线程上，所有将线程切换到主线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(s);
            }
        });

    }

    private void initView() {
        mWebView= (WebView) findViewById(R.id.tencent_vebview);
        //设置浏览器的属性，让webview支持javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.baidu.com");
        //让目标网页仍然在当前webview显示
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                if(i == 100){
                    Toast.makeText(MainActivity.this,"网页加载完了 可以显示评论区了",Toast.LENGTH_LONG).show();
                }
                super.onProgressChanged(webView, i);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack())
            {
                mWebView.goBack();//返回上一页面
                return true;
            }
            else
            {
                System.exit(0);//退出程序
            }

        }

        return super.onKeyDown(keyCode, event);
    }


}
