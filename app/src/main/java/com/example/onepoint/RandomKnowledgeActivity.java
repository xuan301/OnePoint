package com.example.onepoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RandomKnowledgeActivity extends AppCompatActivity {
    private Button favorite;
    GestureDetector Detector;
    private List<Knowledge> knowledge_list;
    private int index;
    protected static final float FLIP_DISTANCE = 150;
    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_knowledge);
        if(getSupportActionBar() != null){ getSupportActionBar().hide(); }
        setHalfTransparent();

        ImageView img_of_knowledge = this.findViewById(R.id.image_of_knowledge);
        TextView title_of_knowledge = this.findViewById(R.id.title_of_knowledge);
        TextView author_of_knowledge = this.findViewById(R.id.author_of_knowledge);
        TextView text_of_knowledge = this.findViewById(R.id.textView);
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);
        knowledge_list = intent.getParcelableArrayListExtra("list");
        String title,imageSrc,text;
        if(knowledge_list != null) {
            Knowledge knowledge = knowledge_list.get(index);
            title = knowledge.getTitle();
            imageSrc = knowledge.getImageSrc();
            text = knowledge.getContent();
        }
        else{
            title = intent.getStringExtra("title");
            imageSrc = intent.getStringExtra("imageSrc");
            text = intent.getStringExtra("content");
        }
            if (title != null && imageSrc != null && text != null) {
                Glide.with(img_of_knowledge.getContext()).load(imageSrc).into(img_of_knowledge);
                text_of_knowledge.setText(text);
                author_of_knowledge.setText(R.string.author);
                title_of_knowledge.setText(title);
            }

        favorite =this.findViewById(R.id.like);

        favorite.setOnClickListener(new View.OnClickListener()
        {//收藏按钮的切换
            boolean isActive = false;
            @Override
            public void onClick(View view){
                Drawable liked = getResources().getDrawable(R.drawable.ic_liked);
                Drawable tolike = getResources().getDrawable(R.drawable.ic_like_black_24dp);

                if(! isActive) {
                    liked.setBounds(0,0,liked.getMinimumWidth(),liked.getMinimumHeight());
                    favorite.setCompoundDrawables(null, liked, null, null);
                    favorite.setText(getResources().getString(R.string.liked));
                }
                else{
                    tolike.setBounds(0,0,tolike.getMinimumWidth(),tolike.getMinimumHeight());
                    favorite.setCompoundDrawables(null, tolike, null, null);
                    favorite.setText(getResources().getString(R.string.like));
                }

                isActive = ! isActive;
            }
        });
        //以下为分享bottomsheet的代码
       /* View bottomsheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomsheet状态的改变
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });*/

        Detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return left_or_right(e1,e2);
            }
        });

        ScrollView content = findViewById(R.id.content);
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return Detector.onTouchEvent(event);
            }
        });

    }
    //以下为评论和分享dialog
    public void doclick(View v)
    {
        switch (v.getId()) {
            case R.id.share:
                BottomSheetDialog mBottomSheetDialog1 = new BottomSheetDialog(this);
                View view1 = getLayoutInflater().inflate(R.layout.share_dialog_bottom_sheet, null);
                mBottomSheetDialog1.setContentView(view1);
                mBottomSheetDialog1.show();
                break;
            case R.id.comment:
                BottomSheetDialog mBottomSheetDialog2 = new BottomSheetDialog(this);
                View view2 = getLayoutInflater().inflate(R.layout.comment_dialog_bottom_sheet, null);
                mBottomSheetDialog2.setContentView(view2);
                mBottomSheetDialog2.show();
                break;
        }
    }

//    @Override
//    public void finish()
//    {
//        Intent intent = getIntent();
//        boolean fromLeft = intent.getBooleanExtra("prev",false);
//        boolean fromRight = intent.getBooleanExtra("next",false);
//        super.finish();
//        if(fromLeft){
//            overridePendingTransition(R.anim.trans_in_alpha,R.anim.trans_out_left);
//        }
//        else if(fromRight){
//            overridePendingTransition(R.anim.trans_in_alpha,R.anim.trans_out_right);
//        }
//    }

    private boolean left_or_right(MotionEvent e1, MotionEvent e2)
    {
        if(Math.abs(e1.getY()-e2.getY()) < FLIP_DISTANCE/2) {
            if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                if(knowledge_list!=null && index+1<knowledge_list.size()) {
                    Intent intent = new Intent(RandomKnowledgeActivity.this, RandomKnowledgeActivity.class);
//                    intent.putExtra("next", true);
                    intent.putExtra("index", index + 1);
                    intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) knowledge_list);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_in_right, R.anim.trans_out_alpha);
                }
                else{
                    Toast.makeText(getApplicationContext(),"已经是最后一页了",Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                if(knowledge_list!=null && index>0) {
                    Intent intent = new Intent(RandomKnowledgeActivity.this, RandomKnowledgeActivity.class);
//                    intent.putExtra("prev", true);
                    intent.putExtra("index",index-1);
                    intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) knowledge_list);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //返回时可以直接回到页面
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_in_left, R.anim.trans_out_alpha);
                }
                else{
                    Toast.makeText(getApplicationContext(),"已经是第一页了",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }
        return false;
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
