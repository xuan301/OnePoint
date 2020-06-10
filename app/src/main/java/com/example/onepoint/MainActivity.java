package com.example.onepoint;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class MainActivity extends AppCompatActivity {

    public final String token = "75958514";
    private final String USER_AGENT = "Mozilla/5.0";
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        Know know = new Know();
                        JSONParse(know.getKnow("username", 10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(MainActivity.this, RandomKnowledgeActivity.class);
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

        Button button_nightmode = findViewById(R.id.nightmode);
        button_nightmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                AppCompatDelegate.setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
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

        Button button_addKnowledge = findViewById(R.id.addKnowledge);
        button_addKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddKnowledgeActivity.class);
                startActivity(intent);
            }
        })
        ;
    }

    private void JSONParse(String source) throws JSONException {
        JSONArray objList = new JSONArray(source);
        knowledgeList.clear();
        for(int i = 0; i < objList.length(); i++ ){
            JSONObject obj =  objList.getJSONObject(i);
            knowledgeList.add(
                    new Knowledge(
                            obj.getString("TITLE"), obj.getString("URL"),
                            obj.getString("CONTENT"), obj.getString("AUTHOR"), obj.getInt("ID")
                    )
            );
        }
    }

    private void initKnowledges() {
        knowledgeList.clear();
        Knowledge[] knowledges = {//不要把这个数组放在函数外面
                new Knowledge(getString(R.string.xigua_title), getString(R.string.xigua_img), getString(R.string.xigua_content), "网络|developer", 1),
                new Knowledge(getString(R.string.famei_title), getString(R.string.famei_img), getString(R.string.famei_content), "网络|developer", 2),
                new Knowledge(getString(R.string.chanbu_title), getString(R.string.snow_img), getString(R.string.chanbu_content), "网络|developer" , 3),
                new Knowledge(getString(R.string.cola_title), getString(R.string.cola_img), getString(R.string.cola_content), "网络|developer", 4),
                new Knowledge(getString(R.string.HCL_title), getString(R.string.HCL_img), getString(R.string.HCL_content), "网络|developer", 5),
                new Knowledge(getString(R.string.hug_title), getString(R.string.hug_img), getString(R.string.hug_content), "网络|developer", 6),
                new Knowledge(getString(R.string.seli_title), getString(R.string.seli_img), getString(R.string.seli_content), "网络|developer", 7),
                new Knowledge(getString(R.string.toubal_title), getString(R.string.toubal_img), getString(R.string.toubal_content), "网络|developer", 8),
                new Knowledge(getString(R.string.sanmiao_title), getString(R.string.sanmiao_img), getString(R.string.sanmiao_content), "网络|developer", 9)
        };
        Collections.addAll(knowledgeList, knowledges);

    }
}
