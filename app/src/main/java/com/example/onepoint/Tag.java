package com.example.onepoint;

import android.os.Build;

import androidx.annotation.RequiresApi;

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
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.net.URLEncoder;
import java.util.Objects;


public class Tag {

    private final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args) throws Exception {
        Tag tag = new Tag();
//        tag.showTags();
//        tag.addTag("测试");
    }

    void addTag(String tag) throws Exception {

        String url = "http://212.64.70.206:5000/addtag/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "tag="+URLEncoder.encode(tag,"utf-8");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        if(responseCode != 200){
            throw new Exception();
        }
//        BufferedReader in;
//        if(responseCode != 400)
//        {
//            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        }
//        else{
//            in =new BufferedReader(new InputStreamReader(con.getErrorStream()));
//        }
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//        System.out.println(response.toString());
    }
    private String showTags_origin() throws Exception {

        String url = "http://212.64.70.206:5000/gettags/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
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
        return response.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String showTags(){
        String result = "";
        while(true){
            try{
                result = showTags_origin();
            }
            catch (Exception e){
                if(Objects.equals(e.getMessage(), "unexpected end of stream")){
                    continue;
                }
                else{
                    e.printStackTrace();
                }
            }
            break;
        }
        return result;

    }

}
