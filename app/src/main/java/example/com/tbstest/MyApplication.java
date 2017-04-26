package example.com.tbstest;

import android.app.Application;
import android.content.Context;

/**
 * Created by 国富小哥 on 2017/4/7.
 * 定义一个自己的Application类，以便管理程序内一些全局的状态信息
 */

public class MyApplication  extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
