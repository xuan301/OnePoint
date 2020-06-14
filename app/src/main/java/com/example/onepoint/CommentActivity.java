package com.example.onepoint;

import androidx.annotation.RequiresApi;
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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

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
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import static com.example.onepoint.R.string.xigua_title;

public class CommentActivity extends AppCompatActivity {

    //private DrawerLayout mDrawerLayout;
    private final String USER_AGENT = "Mozilla/5.0";

    private List<Comment> commentList = new ArrayList<>();

    private CommentListAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                Intent intent = new Intent(CommentActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
        /*if (commentList.size()==0){
            initComments();
        }*/
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                getmyComment(LoginActivity.myUsername);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//开始设置RecyclerView
        recyclerView.setHasFixedSize(true);//设置固定大小
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//创建线性布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
        adapter = new CommentListAdapter(commentList);
        recyclerView.setAdapter(adapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getmyComment(String username)throws Exception{
        String url = "http://212.64.70.206:5000/getmycomment/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes= LoginActivity.token.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\"";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in;
        if(responseCode != 400)
        {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        else{
            in =new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        JSONParse1(response.toString());
    }
    String title;
    String imagesrc;
    String content;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void JSONParse1(String source) throws Exception {
        JSONArray objList = new JSONArray(source);
        commentList.clear();
        for(int i = 0; i < objList.length(); i++ ){
            JSONObject obj =  objList.getJSONObject(i);
            int knowid = obj.getInt("KNOWLEDGEID");
            getOne(LoginActivity.myUsername,knowid);
            commentList.add(
                    new Comment(title,imagesrc,getString(R.string.cola_img),LoginActivity.myUsername,
                            obj.getString("COMMENT"),content,obj.getString("AUTHOR"),knowid,obj.getString("PUBTIME"))
            );
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getOne(String username, int id)throws Exception{
        String url = "http://212.64.70.206:5000/getone/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=LoginActivity.token.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\""+"&id="+id;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in;
        if(responseCode != 400)
        {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        else{
            in =new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        JSONParse2(response.toString());
    }
    private void JSONParse2(String source) throws JSONException {
        JSONObject obj = new JSONObject(source);
        title = obj.getString("TITLE");
        imagesrc = obj.getString("URL");
        content = obj.getString("CONTENT");
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
