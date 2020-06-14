package com.example.onepoint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends AppCompatActivity {

    private final String USER_AGENT = "Mozilla/5.0";
    private DrawerLayout mDrawerLayout;

    private List<Knowledge> knowledgeList = new ArrayList<>();

    private KnowledgeAdapter adapter;

    private Know know = new Know();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
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
                Intent intent = new Intent(LikeActivity.this, InformationActivity.class);
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
                JSONParse(know.getLike(LoginActivity.myUsername));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeAdapter(knowledgeList);
        recyclerView.setAdapter(adapter);

    }

    /*private void initKnowledges() {
        Knowledge[] knowledges = {//不要把这个数组放在函数外面
                new Knowledge(getString(R.string.chanbu_title), getString(R.string.snow_img), getString(R.string.chanbu_content), "网络|developer"),
                new Knowledge(getString(R.string.cola_title), getString(R.string.cola_img), getString(R.string.cola_content),"网络|developer"),
                new Knowledge(getString(R.string.HCL_title), getString(R.string.HCL_img), getString(R.string.HCL_content),"网络|developer"),
                new Knowledge(getString(R.string.hug_title), getString(R.string.hug_img), getString(R.string.hug_content),"网络|developer"),
                new Knowledge(getString(R.string.seli_title), getString(R.string.seli_img), getString(R.string.seli_content),"网络|developer"),
                new Knowledge(getString(R.string.toubal_title), getString(R.string.toubal_img), getString(R.string.toubal_content),"网络|developer"),
                new Knowledge(getString(R.string.sanmiao_title), getString(R.string.sanmiao_img), getString(R.string.sanmiao_content),"网络|developer"),
                new Knowledge(getString(R.string.xigua_title), getString(R.string.xigua_img), getString(R.string.xigua_content),"网络|developer"),
                new Knowledge(getString(R.string.famei_title), getString(R.string.famei_img), getString(R.string.famei_content),"网络|developer")
        };
        knowledgeList.clear();
        for(int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(knowledges.length);
            knowledgeList.add(knowledges[index]);
        }
    }*/

    private void JSONParse(String source) throws Exception {
        if(source.equals("登陆过期")){
            Toast.makeText(getApplicationContext(),"登录已过期，请重新登录",Toast.LENGTH_SHORT).show();
            return;
        }
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
                Toast.makeText(getApplicationContext(),"无收藏数据",Toast.LENGTH_SHORT).show();
            }
            else{
                throw e;
            }
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

    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
    /*protected void setDrawerLayoutFitSystemWindow() {
        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
            int statusBarHeight = getStatusHeight(this);
            if (contentViewGroup == null) {
                contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            }
            if (contentViewGroup instanceof DrawerLayout) {
                DrawerLayout drawerLayout = (DrawerLayout) contentViewGroup;
                drawerLayout.setClipToPadding(true);
                drawerLayout.setFitsSystemWindows(false);
                for (int i = 0; i < drawerLayout.getChildCount(); i++) {
                    View child = drawerLayout.getChildAt(i);
                    child.setFitsSystemWindows(false);
                    child.setPadding(0,statusBarHeight, 0, 0);
                }

            }
        }
    }*/
}
