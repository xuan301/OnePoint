package com.example.onepoint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AddKnowledgeActivity extends AppCompatActivity {
    public final String token = "75958514";
    private final String USER_AGENT = "Mozilla/5.0";
    String title,content,phurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_knowledge);
        if(getSupportActionBar() != null){ getSupportActionBar().hide(); }
        setHalfTransparent();

        Button button_add_picture = findViewById(R.id.add_picture);
        button_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture();
            }
        });

        Button button_finish = findViewById(R.id.finish);
        button_finish.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                EditText title_in = findViewById(R.id.title_add);
                EditText content_in = findViewById(R.id.content_add);
                title = title_in.getText().toString();
                content = content_in.getText().toString();
                if(title.equals("")){Toast.makeText(getApplicationContext(), "题目未填写",Toast.LENGTH_SHORT).show();}
                else if(content.equals("")){Toast.makeText(getApplicationContext(), "内容未填写",Toast.LENGTH_SHORT).show();}
                else if(phurl == null || phurl.equals("")){Toast.makeText(getApplicationContext(), "图片未添加",Toast.LENGTH_SHORT).show();}
                else{
                    try {
                        System.out.println(title);
                        System.out.println(content);
                        System.out.println(phurl);
                        addKnow("username",title,content,phurl,"1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void getPicture()
    {
        final EditText edit = new EditText(this);

        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setTitle("输入图片链接");

        //设置dialog布局
        editDialog.setView(edit);

        //设置按钮
        editDialog.setPositiveButton("确认"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phurl = edit.getText().toString().trim();
                        Toast.makeText(getApplicationContext(),
                                phurl,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        editDialog.create().show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addKnow(String username, String title, String content, String phurl, String tags)throws Exception{
        String url = "http://212.64.70.206:5000/addknow/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=token.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&title="+ URLEncoder.encode(title,"utf-8")+"&content="+URLEncoder.encode(content,"utf-8")+"&url="+phurl+"&tags="+tags+"&time=\""+t+"\"";
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
