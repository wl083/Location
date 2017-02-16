package com.example.l.location.bd.location;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.example.l.location.BaseActivity;
import com.example.l.location.R;


/**
 * Created by Administrator on 2017/2/15.
 */

public class BDLocationActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bdlocation);

        Log.i(TAG, "onReceive: " + "0");
        initFilt();
    }

    /**
     * 验证key
     */
    private void initFilt() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(  //网络错误
                SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        filter.addAction(  //key验证失败
                SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);;
        filter.addAction(  //key验证成功
                SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);

        registerReceiver(new MyReceiver(), filter);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                Intent intent = new Intent(this, BasicMapActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                Intent poiIntent = new Intent(this, PoiSearchActivity.class);
                startActivity(poiIntent);
                break;
            case R.id.btn3:
                Intent pIntent = new Intent(this,MapActivity.class);
                startActivity(pIntent);
                break;
        }
    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(action)){
                showToast("网络异常，请检查网络设置");
            } else if(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(action)){
                showToast("key验证失败，请在AndroidManifest.xml文件中注册密钥");
            } else if(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK.equals(action)){
                showToast("key验证成功， 功能可以正常使用");
            }
        }

    }


}
