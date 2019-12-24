package com.zh.android.floatbutton;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zh.android.floatbutton.weiget.FloatButtonLayout;

/**
 * <b>Package:</b> com.zh.android.floatbutton <br>
 * <b>Create Date:</b> 2019-12-24  15:03 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b>  <br>
 */
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
                startActivity(new Intent(MainActivity.this, NewYearActivity.class));
            }
        });
    }
}