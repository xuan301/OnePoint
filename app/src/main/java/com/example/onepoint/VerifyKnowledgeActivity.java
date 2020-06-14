package com.example.onepoint;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.List;

public class VerifyKnowledgeActivity extends AppCompatActivity {
    private Know know = new Know();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //dialog的new创建只能写在Oncreate中或之后
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_knowledge);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setHalfTransparent();

        ImageView img_of_knowledge = this.findViewById(R.id.image_of_knowledge);
        TextView title_of_knowledge = this.findViewById(R.id.title_of_knowledge);
        TextView author_of_knowledge = this.findViewById(R.id.author_of_knowledge);
        TextView text_of_knowledge = this.findViewById(R.id.textView);
        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        List<Knowledge> knowledge_list = intent.getParcelableArrayListExtra("list");
        String title, imageSrc, text, author;
        int id;
        if (knowledge_list != null && knowledge_list.size() != 0) {
            Knowledge knowledge = knowledge_list.get(index);
            title = knowledge.getTitle();
            imageSrc = knowledge.getImageSrc();
            text = knowledge.getContent();
            author = knowledge.getAuthor();
            id = knowledge.getId();
        } else {
            title = intent.getStringExtra("title");
            imageSrc = intent.getStringExtra("imageSrc");
            text = intent.getStringExtra("content");
            author = intent.getStringExtra("author");
            id = intent.getIntExtra("id",0);
        }
        if (title != null && imageSrc != null && text != null) {
            Glide.with(img_of_knowledge.getContext()).load(imageSrc).into(img_of_knowledge);
            text_of_knowledge.setText(text);
            title_of_knowledge.setText(title);
            if (author != null) {
                author_of_knowledge.setText(author);
            } else {
                author_of_knowledge.setText(R.string.author);
            }
        }

        Button button_verify = findViewById(R.id.verify);
        final int finalId = id;
        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    know.auditPass(LoginActivity.myUsername, finalId);
                    JSONObject object = new JSONObject(know.getAudit(LoginActivity.myUsername));
                    if(!object.optString("Message").equals("NO Knowledge Waiting to be Audited")) {
                        Intent intent = new Intent(VerifyKnowledgeActivity.this, VerifyKnowledgeActivity.class);
                        intent.putExtra("title", object.getString("TITLE"));
                        intent.putExtra("content", object.getString("CONTENT"));
                        intent.putExtra("imageSrc", object.getString("URL"));
                        intent.putExtra("author", object.getString("AUTHOR"));
                        intent.putExtra("id", object.getInt("ID"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(VerifyKnowledgeActivity.this, "无可审核的知识", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button button_reject = findViewById(R.id.reject);
        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    know.auditFail(LoginActivity.myUsername, finalId);
                    JSONObject object = new JSONObject(know.getAudit(LoginActivity.myUsername));
                    if(!object.optString("Message").equals("NO Knowledge Waiting to be Audited")) {
                        Intent intent = new Intent(VerifyKnowledgeActivity.this, VerifyKnowledgeActivity.class);
                        intent.putExtra("title", object.getString("TITLE"));
                        intent.putExtra("content", object.getString("CONTENT"));
                        intent.putExtra("imageSrc", object.getString("URL"));
                        intent.putExtra("author", object.getString("AUTHOR"));
                        intent.putExtra("id", object.getInt("ID"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(VerifyKnowledgeActivity.this, "无可审核的知识", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
