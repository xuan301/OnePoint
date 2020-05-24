package com.example.onepoint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
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

    private Knowledge[] knowledges = {
            new Knowledge("落霞与孤鹜齐飞，秋水共长天一色。——王勃《滕王阁序》", "https://wx2.sinaimg.cn/large/0067hdZuly1g7szjhsld7j30u01901kz.jpg"),
            new Knowledge("【徐志摩！你的落款又傲娇又风骚】徐志摩在写给陆小曼的书信集《爱曼小札》中的落款很肉麻风骚", "https://wx3.sinaimg.cn/large/0067hdZugy1g7ssvg877pj30j60j675i.jpg"),
            new Knowledge("晚安碎语【第十四话，私藏句子】我们常常不去想自己拥有的东西，却对得不到的东西念念不忘。——叔本华", "https://wx3.sinaimg.cn/large/0067hdZugy1g7ss38x252j30yi0yitau.jpg"),
            new Knowledge("人走茶凉伤感吗？不伤感！真正伤感的是：人走了，却把我的茶杯也骗走了。——王小波《王小波文集》", "https://wx1.sinaimg.cn/large/0067hdZuly1g71vmsi9jqj30uv0u0tea.jpg"),
            new Knowledge("所有感情都没有过分夸大的必要，因为没有什么只开始不结束。小时候觉得一辈子都不会弄丢的东西，最后也毫不心疼地丢弃了。亲情，爱情，友情，都会慢慢失去，有的自然而然，有的姿势难看。但感情的事，不就是这个样子吗。——姚瑶《生活上瘾指南》", "https://wx3.sinaimg.cn/large/0067hdZuly1g71efm9xpgj30u01hegy6.jpg")
    };

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
