package com.example.l.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.l.location.bd.location.BDLocationActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this,BDLocationActivity.class));
                break;
            case R.id.btn2:
                showToast("还没想好呐~");
                break;
        }

    }
}
