package com.example.onepoint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommentActivity extends AppCompatActivity {

    //private DrawerLayout mDrawerLayout;

    private Comment[] comments = {
            new Comment("落霞与孤鹜齐飞，秋水共长天一色。——王勃《滕王阁序》",
                    "https://wx2.sinaimg.cn/large/0067hdZuly1g7szjhsld7j30u01901kz.jpg",R.drawable.fig1,"wulala2580","啊这"),
            new Comment("【徐志摩！你的落款又傲娇又风骚】徐志摩在写给陆小曼的书信集《爱曼小札》中的落款很肉麻风骚",
                    "https://wx3.sinaimg.cn/large/0067hdZugy1g7ssvg877pj30j60j675i.jpg",R.drawable.fig2,"jlsnb","啊吧啊吧啊吧"),
            new Comment("晚安碎语【第十四话，私藏句子】我们常常不去想自己拥有的东西，却对得不到的东西念念不忘。——叔本华",
                    "https://wx3.sinaimg.cn/large/0067hdZugy1g7ss38x252j30yi0yitau.jpg",R.drawable.fig3,"mihu","不会真有人以为..."),
    };

    private List<Comment> commentList = new ArrayList<>();

    private CommentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) actionBar.hide();
        Button button_back = findViewById(R.id.home);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setHalfTransparent();
        Button button_setting = (Button) findViewById(R.id.setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        if (commentList.size()==0){
            initComments();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//开始设置RecyclerView
        recyclerView.setHasFixedSize(true);//设置固定大小
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//创建线性布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
        adapter = new CommentListAdapter(commentList);
        recyclerView.setAdapter(adapter);
    }

    private void initComments() {
        commentList.clear();
        for(int i = 0; i < comments.length; i++) {
            commentList.add(comments[i]);
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
