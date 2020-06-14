package com.example.onepoint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String USER_AGENT = "Mozilla/5.0";
    private List<Knowledge> knowledgeList= new ArrayList<>();
    Know know = new Know();

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
                        JSONParse(know.getKnow(LoginActivity.myUsername, 10));
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
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button button_like = findViewById(R.id.like);
        button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    Intent intent = new Intent(MainActivity.this, LikeActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        })
        ;

        Button button_comment = findViewById(R.id.comment);
        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        })
        ;

        Button button_search = findViewById(R.id.search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        })
        ;

        Button button_rank = findViewById(R.id.rank);
        button_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    Intent intent = new Intent(MainActivity.this, RankActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
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
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(!loginState){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this,InformationActivity.class);
                    startActivity(intent);
                }
            }
        })
        ;

        Button button_addKnowledge = findViewById(R.id.addKnowledge);
        button_addKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    Intent intent = new Intent(MainActivity.this, AddKnowledgeActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        })
        ;

        Button button_verify_knowledge = findViewById(R.id.verifyKnowledge);
        button_verify_knowledge.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                boolean loginState = sharedPreferences.getBoolean("isLogin",false);
                if(loginState){
                    if(know.isAdmin(LoginActivity.myUsername)) {
                        try {
                            JSONObject object = new JSONObject(know.getAudit(LoginActivity.myUsername));
                            if(!object.optString("Message").equals("NO Knowledge Waiting to be Audited")) {
                                Intent intent = new Intent(MainActivity.this, VerifyKnowledgeActivity.class);
                                intent.putExtra("title", object.getString("TITLE"));
                                intent.putExtra("content", object.getString("CONTENT"));
                                intent.putExtra("imageSrc", object.getString("URL"));
                                intent.putExtra("author", object.getString("AUTHOR"));
                                intent.putExtra("id", object.getInt("ID"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "无可审核的知识", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "非管理员", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void JSONParse(String source) throws JSONException {
        if(source.equals("登陆过期")){
            Toast.makeText(getApplicationContext(),"登录已过期，请重新登录",Toast.LENGTH_SHORT).show();
            return;
        }
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
}
