package com.example.l.location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2017/2/15.
 */

public class BaseActivity extends AppCompatActivity{
    public String TAG = "lei";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
    }
    protected void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
