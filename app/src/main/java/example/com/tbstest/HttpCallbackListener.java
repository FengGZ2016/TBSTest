package example.com.tbstest;

/**
 * Created by 国富小哥 on 2017/4/8.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
