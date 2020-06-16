package com.example.onepoint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.spec.*;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Random;
import java.net.URLEncoder;


public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mListView;
    private KnowledgeAdapter adapter;
    private List<Knowledge> knowledgeList = new ArrayList<>();
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setHalfTransparent();
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
                Intent intent = new Intent(SearchActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });

        mSearchView = (SearchView) findViewById(R.id.searview);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("输入搜索内容");

        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) mSearchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        int id = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) mSearchView.findViewById(id);
        if (searchEditText != null) {
            searchEditText.setGravity(Gravity.CENTER);
        }

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(currentNightMode != Configuration.UI_MODE_NIGHT_NO){
            int idd =mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = mSearchView.findViewById(idd);
            textView.setTextColor(Color.WHITE);
            mSearchView.setQueryHint(Html.fromHtml("<font color = #AAAAAA>" + "输入搜索内容" + "</font>"));
        }


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeAdapter(knowledgeList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(0);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*Knowledge[] knowledges = {//不要把这个数组放在函数外面
                        new Knowledge(getString(R.string.chanbu_title), getString(R.string.snow_img), getString(R.string.chanbu_content), "网络|developer"),
                        new Knowledge(getString(R.string.cola_title), getString(R.string.cola_img), getString(R.string.cola_content),"网络|developer"),
                        new Knowledge(getString(R.string.HCL_title), getString(R.string.HCL_img), getString(R.string.HCL_content),"网络|developer"),
                        new Knowledge(getString(R.string.hug_title), getString(R.string.hug_img), getString(R.string.hug_content),"网络|developer"),
                        new Knowledge(getString(R.string.seli_title), getString(R.string.seli_img), getString(R.string.seli_content),"网络|developer"),
                        new Knowledge(getString(R.string.toubal_title), getString(R.string.toubal_img), getString(R.string.toubal_content),"网络|developer"),
                        new Knowledge(getString(R.string.sanmiao_title), getString(R.string.sanmiao_img), getString(R.string.sanmiao_content),"网络|developer"),
                        new Knowledge(getString(R.string.xigua_title), getString(R.string.xigua_img), getString(R.string.xigua_content),"网络|developer"),
                        new Knowledge(getString(R.string.famei_title), getString(R.string.famei_img), getString(R.string.famei_content),"网络|developer")
                };*/
                int size = knowledgeList.size();
                knowledgeList.clear();
                adapter.notifyItemRangeRemoved(0, size);
                recyclerView.setItemViewCacheSize(0);
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        SharedPreferences sharedPreferences= getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("token",null);
                        String username = sharedPreferences.getString("loginUserName",null);
                        searchKnow(username, 10, query);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int size = knowledgeList.size();
                knowledgeList.clear();
                adapter.notifyItemRangeRemoved(0, size);
                return false;
            }
        });

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = knowledgeList.size();
                knowledgeList.clear();
                adapter.notifyItemRangeRemoved(0, size);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchKnow(String username, int num, String text)throws Exception{
        SharedPreferences sharedPreferences= getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        String url = "http://212.64.70.206:5000/searchknow/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=(token).getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\""+"&num="+num+"&text="+URLEncoder.encode(text,"utf-8");
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
        JSONParse(response.toString());
    }

    private void JSONParse(String source) throws JSONException {
        int size = knowledgeList.size();
        knowledgeList.clear();
        adapter.notifyItemRangeRemoved(0, size);
        JSONArray objList = new JSONArray(source);
        for(int i = 0; i < objList.length(); i++ ){
            JSONObject obj =  objList.getJSONObject(i);
            knowledgeList.add(
                    new Knowledge(
                            obj.getString("TITLE"), obj.getString("URL"),
                            obj.getString("CONTENT"), obj.getString("AUTHOR"),
                            obj.getInt("ID")
                    )
            );
            Log.d("Showing content", "JSONParse: "+knowledgeList.toString());
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