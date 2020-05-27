package com.example.onepoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private List<Knowledge> knowledgeList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().hide();
        /*if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }*/
        setContentView(R.layout.activity_main);

        Button button_randomknowledge = findViewById(R.id.randomknowledge);
        button_randomknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RandomKnowledgeActivity.class);
                initKnowledges();
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) knowledgeList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        })
        ;

        Button button_read = findViewById(R.id.read);
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent);
            }
        });

        Button button_like = findViewById(R.id.like);
        button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LikeActivity.class);
                startActivity(intent);
            }
        })
        ;

        Button button_comment = findViewById(R.id.comment);
        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                startActivity(intent);
            }
        })
        ;

        Button button_search = findViewById(R.id.search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        })
        ;

        Button button_rank = findViewById(R.id.rank);
        button_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
            }
        })
        ;

        Button button_setting = findViewById(R.id.setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        })
        ;
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
        Collections.addAll(knowledgeList, knowledges);

    }
}
