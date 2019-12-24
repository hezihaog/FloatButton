package com.zh.android.floatbutton;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zh.android.floatbutton.weiget.FloatButtonLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatButtonLayout floatButton = findViewById(R.id.float_button_layout);
        //设置点击事件
        floatButton.setCallback(new FloatButtonLayout.Callback() {
            @Override
            public void onClickFloatButton() {
                Toast.makeText(MainActivity.this.getApplicationContext(),
                        "跳转到活动详情", Toast.LENGTH_SHORT).show();
            }
        });
    }
}