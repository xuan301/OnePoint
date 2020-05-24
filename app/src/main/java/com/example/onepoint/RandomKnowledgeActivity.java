package com.example.onepoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RandomKnowledgeActivity extends AppCompatActivity {
    private Button bt;
    private ImageView img_of_knowledge;
    private TextView title_of_knowledge;
    private TextView author_of_knowledge;
    private TextView text_of_knowledge;
    GestureDetector Detector;
    protected static final float FLIP_DISTANCE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_knowledge);
        if(getSupportActionBar() != null){ getSupportActionBar().hide(); }
        setHalfTransparent();

        img_of_knowledge = this.findViewById(R.id.image_of_knowledge);
        title_of_knowledge = this.findViewById(R.id.title_of_knowledge);
        author_of_knowledge = this.findViewById(R.id.author_of_knowledge);
        text_of_knowledge = this.findViewById(R.id.textView);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imageSrc = intent.getStringExtra("imageSrc");
        if(title != null && imageSrc != null){
            Glide.with(img_of_knowledge.getContext()).load(imageSrc).into(img_of_knowledge);
            text_of_knowledge.setText(title);
            author_of_knowledge.setText(null);
            title_of_knowledge.setText(null);
        }

        bt=this.findViewById(R.id.like);

        bt.setOnClickListener(new View.OnClickListener()
        {//收藏按钮的切换
            boolean isActive = false;
            @Override
            public void onClick(View view){
                Drawable liked = getResources().getDrawable(R.drawable.ic_liked);
                Drawable tolike = getResources().getDrawable(R.drawable.ic_like_black_24dp);

                if(! isActive) {
                    liked.setBounds(0,0,liked.getMinimumWidth(),liked.getMinimumHeight());
                    bt.setCompoundDrawables(null, liked, null, null);
                    bt.setText(getResources().getString(R.string.liked));
                }
                else{
                    tolike.setBounds(0,0,tolike.getMinimumWidth(),tolike.getMinimumHeight());
                    bt.setCompoundDrawables(null, tolike, null, null);
                    bt.setText(getResources().getString(R.string.like));
                }

                isActive = ! isActive;
            }
        });

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
                if(e1.getX()-e2.getX()>FLIP_DISTANCE){
                    Intent intent = new Intent(RandomKnowledgeActivity.this, RandomKnowledgeActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(e2.getX()-e1.getX()>FLIP_DISTANCE){
                    Intent intent = new Intent(RandomKnowledgeActivity.this, RandomKnowledgeActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return Detector.onTouchEvent(event);
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
