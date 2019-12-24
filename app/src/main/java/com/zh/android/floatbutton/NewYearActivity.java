package com.zh.android.floatbutton;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <b>Package:</b> com.zh.android.floatbutton <br>
 * <b>Create Date:</b> 2019-12-24  17:21 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 新年活动页面 <br>
 */
public class NewYearActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_year);
        ActionBar actionBar = getSupportActionBar();
        //显示返回键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //隐藏图标
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}