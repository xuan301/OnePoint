package com.example.onepoint;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddKnowledgeActivity extends AppCompatActivity {
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
                            System.out.println(id);
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
                                "图片链接: " + phurl,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
                Toast.makeText(getApplicationContext(), "选择标签：" + message, Toast.LENGTH_SHORT).show();
                System.out.println(id);
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
