package com.example.onepoint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddKnowledgeActivity extends AppCompatActivity {
    String title,content, imgString,id;
    Know know = new Know();
    private Uri imageUri;
    private Uri uritempFile;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

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

        Button button_set_tags = findViewById(R.id.select_tag);
        button_set_tags.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                getTag();
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
                else if(imgString == null || imgString.equals("")){Toast.makeText(getApplicationContext(), "图片未添加",Toast.LENGTH_SHORT).show();}
                else if(id == null || id.equals("")){Toast.makeText(getApplicationContext(), "标签未添加",Toast.LENGTH_SHORT).show();}
                else{
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        try {
                            System.out.println(title);
                            System.out.println(content);
                            System.out.println(imgString);
                            System.out.println(id);
                            know.addKnow(LoginActivity.myUsername, title, content, imgString, id);
                            Toast.makeText(AddKnowledgeActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            if(Objects.equals(e.getMessage(), "Attempt to invoke virtual method 'byte[] java.lang.String.getBytes()' on a null object reference"))
                            {
                                Toast.makeText(getApplicationContext(),"登录已过期，请重新登录",Toast.LENGTH_SHORT).show();
                            }
                            else if(Objects.equals(e.getMessage(), "Connection reset")){
                                Toast.makeText(getApplicationContext(),"服务器未启动",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                e.printStackTrace();
                                Toast.makeText(AddKnowledgeActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

    }

    private void getPicture()
    {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setTitle("选择图片")
                .setItems(new String[]{"从相机拍摄", "从相册选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(which);
                        switch (which) {
                            case 0:
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
                                    imageUri = FileProvider.getUriForFile(AddKnowledgeActivity.this,
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
                            case 1:
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
                                    imageUri = FileProvider.getUriForFile(AddKnowledgeActivity.this,
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
                });

        editDialog.create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getTag() {
        final String[] items = {"经典音乐","经典书籍","冷知识","经典影视","人生哲理","生活窍门","艺术作品"};
        final Map<String, Integer> idTable = new HashMap<>();
        int j = 0;
        for(int i: new int[]{1, 2, 5, 8, 9, 10, 11}){
            idTable.put(items[j],i);
            j++;
        }
        final boolean[] checkState = new boolean[items.length];
        final AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
        builder4.setTitle("选择标签");
        // 设置多选选项，并绑定点击事件
        builder4.setMultiChoiceItems(items, new boolean[items.length],
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkState[which] = isChecked;
                    }
                });
        // 点击“确定”按钮后输出选中的选项
        builder4.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = "";
                id = "";
                for (int i = 0; i < items.length; i++) {
                    if (checkState[i]) {
                        message += items[i] + " ";
                        if(!id.equals("")){id += ",";}
                        id += idTable.get(items[i]);

                    }
                }
                if(message.equals("")){message = "未选择标签";}
                Toast.makeText(getApplicationContext(), "选择标签：" + message, Toast.LENGTH_SHORT).show();
                System.out.println(id);
            }
        });
        builder4.create().show();
    }

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
        intent.putExtra("aspectX", 410);
        intent.putExtra("aspectY", 237);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 410);
        intent.putExtra("outputY", 237);


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
                    System.out.println("图片设置成功！");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                crop(imageUri);
            } else {
                Toast.makeText(AddKnowledgeActivity.this, "未设置成功！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            System.out.println("相机返回");
            if (resultCode==RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    System.out.println("图片设置成功！");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                crop(imageUri);
            } else {
                Toast.makeText(AddKnowledgeActivity.this, "未设置成功！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                try{
                    verifyStoragePermissions(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddKnowledgeActivity.this, "图片设置成功", Toast.LENGTH_SHORT).show();
                }
                /**
                 * 获得图片
                 */

            }

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
        String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("addimage", imageString);
        editor.apply();

        //上传头像
        //setImgByStr(imageString,"");
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

}
