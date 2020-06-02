package com.example.onepoint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private List<Knowledge> knowledgeList = new ArrayList<>();

    private KnowledgeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        Button button_back = (Button) findViewById(R.id.home);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        Button button_setting = (Button) findViewById(R.id.setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        setHalfTransparent();
        initKnowledges();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeAdapter(knowledgeList);
        recyclerView.setAdapter(adapter);
    }

    private void initKnowledges() {
        knowledgeList.clear();
        Knowledge[] knowledges = {//不要把这个数组放在函数外面
                new Knowledge(getString(R.string.xigua_title), getString(R.string.xigua_img), getString(R.string.xigua_content)),
                new Knowledge(getString(R.string.famei_title), getString(R.string.famei_img), getString(R.string.famei_content)),
                new Knowledge(getString(R.string.chanbu_title), getString(R.string.snow_img), getString(R.string.chanbu_content)),
                new Knowledge(getString(R.string.cola_title), getString(R.string.cola_img), getString(R.string.cola_content)),
                new Knowledge(getString(R.string.HCL_title), getString(R.string.HCL_img), getString(R.string.HCL_content)),
                new Knowledge(getString(R.string.hug_title), getString(R.string.hug_img), getString(R.string.hug_content)),
                new Knowledge(getString(R.string.seli_title), getString(R.string.seli_img), getString(R.string.seli_content)),
                new Knowledge(getString(R.string.toubal_title), getString(R.string.toubal_img), getString(R.string.toubal_content)),
                new Knowledge(getString(R.string.sanmiao_title), getString(R.string.sanmiao_img), getString(R.string.sanmiao_content))
        };
        for(int i = 0; i < knowledges.length; i++) {
            knowledgeList.add(knowledges[i]);
        }
    }
    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

}
