package com.example.onepoint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    private List<Knowledge> knowledgeList = new ArrayList<>();

    private KnowledgeAdapter adapter;

    private Know know = new Know();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        Button button_back = (Button) findViewById(R.id.home);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        setHalfTransparent();
        Button button_setting = (Button) findViewById(R.id.setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                JSONParse(know.getRank(LoginActivity.myUsername,10));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeAdapter(knowledgeList);
        recyclerView.setAdapter(adapter);
    }

    private void JSONParse(String source) throws Exception {
        try {
            JSONArray objList = new JSONArray(source);
            knowledgeList.clear();
            for (int i = 0; i < objList.length(); i++) {
                JSONObject obj = objList.getJSONObject(i);
                knowledgeList.add(
                        new Knowledge(
                                obj.getString("TITLE"), obj.getString("URL"),
                                obj.getString("CONTENT"), obj.getString("AUTHOR"),
                                obj.getInt("ID")
                        )
                );
            }
        }
        catch (JSONException e){
            JSONObject object = new JSONObject(source);
            if(object.getString("Message").equals("Error")){
                Toast.makeText(getApplicationContext(),"暂无数据，请重试",Toast.LENGTH_SHORT).show();
            }
            else{
                throw e;
            }
        }
    }


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

}
