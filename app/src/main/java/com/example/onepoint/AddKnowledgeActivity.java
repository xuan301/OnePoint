package com.example.onepoint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AddKnowledgeActivity extends AppCompatActivity {
    public final String token = "75958514";
    private final String USER_AGENT = "Mozilla/5.0";
    String title,content,phurl,id;
    Know know = new Know();
    Tag tag = new Tag();

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
                try {
                    getTag();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                            System.out.println(phurl);
                            know.addKnow(LoginActivity.myUsername, title, content, phurl, id);
                        } catch (Exception e) {
                            if(Objects.equals(e.getMessage(), "Attempt to invoke virtual method 'byte[] java.lang.String.getBytes()' on a null object reference"))
                            {
                                Toast.makeText(getApplicationContext(),"登录已过期，请重新登录",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                e.printStackTrace();
                            }
                        }
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

    private void sendTag(){
        final EditText edit = new EditText(this);

        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setTitle("输入标签");

        //设置dialog布局
        editDialog.setView(edit);

        //设置按钮
        editDialog.setPositiveButton("确认"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            tag.addTag(edit.getText().toString().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),
                                "添加标签: "+edit.getText().toString().trim(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        editDialog.create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getTag() throws JSONException {
        JSONObject tags = new JSONObject(tag.showTags());
        final String[] items = new String[tags.length()];
        final Map<Integer, String> idTable = new HashMap<>();
        int i=0;
        for (Iterator<String> it = tags.keys(); it.hasNext(); ) {
            String key = it.next();
            items[i] = tags.getString(key);
            idTable.put(i,key);
            i++;
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
                for (int i = 0; i < items.length; i++) {
                    if (checkState[i]) {
                        message += idTable.get(i) +",";
                    }
                }
                id = message;
                Toast.makeText(getApplicationContext(), "选择标签：" + message, Toast.LENGTH_SHORT).show();
            }
        });
        builder4.setNegativeButton("取消", null);
        builder4.setNeutralButton("添加新标签", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendTag();
            }
        });
        builder4.create().show();
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
