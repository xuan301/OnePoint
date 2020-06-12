package com.example.onepoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity{
    private final String USER_AGENT = "Mozilla/5.0";
    private void getSMS(String phone) throws Exception {

        String url = "http://212.64.70.206:5000/getsms/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "phone="+phone;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void register(String phone, String password, String code, String username) throws Exception{
//        String publicKeyFilePath = "public.der";
//        Path publicKeyPath = Paths.get(publicKeyFilePath);
//        System.out.println(publicKeyPath.toAbsolutePath());
//        byte[] publicKeyBytes = Files.readAllBytes(publicKeyPath);
        String filename = "public.der";
        InputStream inputStream = getAssets().open(filename);
        byte[] publicKeyBytes = toByteArray(inputStream);
        inputStream.close();

        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encStr = cipher.doFinal(password.getBytes());
        String pass = Base64.getEncoder().encodeToString(encStr);
        String url = "http://212.64.70.206:5000/register/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "phone="+phone+"&password=\""+pass+"\"&code="+code+"&username="+username;

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

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setHalfTransparent();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        Button button_back = (Button) findViewById(R.id.home);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button_code = (Button) findViewById(R.id.send_phone_number);
        button_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        EditText et_phone = (EditText) findViewById(R.id.et_phone);
                        String phone = et_phone.getText().toString().trim();
                        getSMS(phone);
                        Toast.makeText(getApplicationContext(),"验证码已发送",Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button button_register = (Button) findViewById(R.id.btn_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        EditText et_phone = (EditText) findViewById(R.id.et_phone);
                        EditText et_code = (EditText) findViewById(R.id.et_code);
                        EditText et_username = (EditText) findViewById(R.id.et_user);
                        EditText et_password = (EditText) findViewById(R.id.et_paw);
                        String phone = et_phone.getText().toString().trim();
                        String code = et_code.getText().toString().trim();
                        String username = et_username.getText().toString().trim();
                        String password = et_password.getText().toString().trim();
                        register(phone,password,code,username);
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


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
