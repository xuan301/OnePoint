package com.example.onepoint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_img;
    private Button bt_camera;
    private Button bt_xiangce;
    private Uri imageUri;
    private Uri uritempFile;
    private final String USER_AGENT = "Mozilla/5.0";
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setHalfTransparent();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build()); }

        SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String username_local = sharedPreferences.getString("loginUserName","None");
        TextView username = (TextView) findViewById(R.id.username);
        username.setText(username_local+" ， 你好！");


        Button button_back = (Button) findViewById(R.id.home);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button_withdraw = (Button) findViewById(R.id.withdraw);
        button_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("isLogin", false);
                editor.apply();
                finish();
            }
        });

        //加载控件
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        iv_img = (ImageView) findViewById(R.id.iv_img);
        bt_camera = (Button) findViewById(R.id.bt_camera);
        bt_xiangce = (Button) findViewById(R.id.bt_xiangce);
        //从SharedPreferences获取图片
       // getBitmapFromSharedPreferences();
        //从服务器获取图片
        setiv_image();
        //监听两个按钮，相册按钮和相机按钮
        bt_camera.setOnClickListener(this);
        bt_xiangce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera:
                // 激活相机
                File outputImage = new File(getExternalCacheDir(),"out_image1.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(InformationActivity.this,
                            "com.example.onepoint.fileprovider", outputImage);
                    Log.e("imageUri SDK_INT >=24" ,imageUri.toString());
                }else{
                    imageUri = Uri.fromFile(outputImage);
                    Log.e("imageUri SDK_INT <24" ,imageUri.toString());
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                break;
            case R.id.bt_xiangce:
                // 激活系统图库，选择一张图片
                File outputImage1 = new File(getExternalCacheDir(),"out_image1.jpg");
                try{
                    if(outputImage1.exists()){
                        outputImage1.delete();
                    }
                    outputImage1.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(InformationActivity.this,
                            "com.example.onepoint.fileprovider", outputImage1);
                    Log.e("imageUri SDK_INT >=24" ,imageUri.toString());
                }else{
                    imageUri = Uri.fromFile(outputImage1);
                    Log.e("imageUri SDK_INT <24" ,imageUri.toString());
                }
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                //intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent1.setType("image/*");
                /*Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY*/
                startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
                break;
        }
    }
    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        //判断ＳＤ卡是否是安装好的　　　media_mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
/*        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(getBaseContext(),uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }*/
        intent.setDataAndType(uri, "image/*");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);


        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        //intent.putExtra("return-data", true);

        uritempFile = Uri.parse("file://" + "/" + getExternalCacheDir().getPath()
                + "/" + "out_image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        Log.e("crop:",uritempFile.toString());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
        System.out.println("crop运行完毕");
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //防止低版本没有返回确认数据，导致崩溃
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println("result code: "+resultCode);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            System.out.println("相册返回");
            // 从相册返回的数据
            if (resultCode==RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    imageUri = data.getData();
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    Log.e("xiangceuri:",imageUri.toString());
                    iv_img.setImageBitmap(bitmap);
                    System.out.println("头像设置成功！");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                crop(imageUri);
            } else {
                Toast.makeText(InformationActivity.this, "未设置成功！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (resultCode==RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    iv_img.setImageBitmap(bitmap);
                    System.out.println("头像设置成功！");
                    saveBitmapToSharedPreferences(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                crop(imageUri);
            } else {
                Toast.makeText(InformationActivity.this, "未设置成功！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                try{
                    verifyStoragePermissions(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    iv_img.setImageBitmap(bitmap);
                    //保存到SharedPreferences
                    saveBitmapToSharedPreferences(bitmap);

                    SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                    //第一步:取出字符串形式的Bitmap
                    String here_username=sharedPreferences.getString("loginUserName", "");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
                    changePhoto(here_username,imageString);

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 * 获得图片
                 */

            }

        }

        if(data==null){
            return;
        }
    }

    //保存图片到SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();

        //上传头像
        //setImgByStr(imageString,"");
    }


    /**
     * 上传头像
     */
/*    public  void setImgByStr(String imgStr, String imgName) {
        //这里是头像接口，通过Post请求，拼接接口地址和ID，上传数据。
        String url = "http://这里写的是接口地址（具体接收格式要看后台怎么给）";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "11111111");// 11111111
        params.put("data", imgStr);
        OkHttp.postAsync(url, params, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.i("上传失败", "失败" + request.toString() + e.toString());
            }
            @Override
            public void requestSuccess(String result) throws Exception {
                Toast.makeText(MainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                Log.i("上传成功", result);
            }
        });
    }*/

    //从SharedPreferences获取图片
    /*private void getBitmapFromSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString=sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
        if(byteArray.length==0){
            iv_img.setImageResource(R.mipmap.ic_launcher);
        }else{
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);

            //第三步:利用ByteArrayInputStream生成Bitmap
            Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
            iv_img.setImageBitmap(bitmap);
        }

    }*/
    String imgsrc;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setiv_image(){
        while(true) {
            try {
                imgsrc = getPhoto(LoginActivity.myUsername, LoginActivity.myUsername);
            } catch (Exception e) {
                if (Objects.equals(e.getMessage(), "unexpected end of stream")) {
                    continue;
                }
                else{
                    e.printStackTrace();
                }
            }
            break;
        }
        if (imgsrc == null){
            iv_img.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(this).load(imgsrc).into(iv_img);
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changePhoto(String username, String imageString)throws Exception{
        String url = "http://212.64.70.206:5000/changephoto/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=(LoginActivity.token).getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = java.util.Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\""+"&pic="+imageString;
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
    private String getPhoto(String username,String query)throws Exception{
        String url = "http://212.64.70.206:5000/getphoto/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=(LoginActivity.token).getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = java.util.Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\""+"&query="+query;
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
        String str = response.toString();
        String regexp = "\"";
        str = str.replaceAll(regexp, "");
        return str;
    }


}
