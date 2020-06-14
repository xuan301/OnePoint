package com.example.onepoint;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onepoint.bean.FirstLevelBean;
import com.example.onepoint.dialog.InputTextMsgDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class RandomKnowledgeActivity extends AppCompatActivity {
    public final String token = LoginActivity.token;
    private final String USER_AGENT = "Mozilla/5.0";
    private Button favorite;
    GestureDetector Detector;
    private List<Knowledge> knowledge_list;
    private int index;
    protected static final float FLIP_DISTANCE = 150;
    BottomSheetBehavior behavior;
    private Know know = new Know();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //dialog的new创建只能写在Oncreate中或之后
        mBottomSheetDialog2 = new BottomSheetDialog(this,R.style.dialog);

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
        String title,imageSrc,text,author;
        int id = 0;
        if(knowledge_list != null && knowledge_list.size() != 0) {
            Knowledge knowledge = knowledge_list.get(index);
            title = knowledge.getTitle();
            imageSrc = knowledge.getImageSrc();
            text = knowledge.getContent();
            author = knowledge.getAuthor();
            id = knowledge.getId();
        }
        else{
            title = intent.getStringExtra("title");
            imageSrc = intent.getStringExtra("imageSrc");
            text = intent.getStringExtra("content");
            author = intent.getStringExtra("author");
        }
            if (title != null && imageSrc != null && text != null) {
                Glide.with(img_of_knowledge.getContext()).load(imageSrc).into(img_of_knowledge);
                text_of_knowledge.setText(text);
                title_of_knowledge.setText(title);
                if(author != null){author_of_knowledge.setText(author);}else{author_of_knowledge.setText(R.string.author);}
            }

        try {
            know.viewOne(LoginActivity.myUsername,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int finalId;
        favorite =this.findViewById(R.id.like);
        if (knowledge_list!=null){
            finalId = knowledge_list.get(index).getId();
        }else{
            Intent intent1 = getIntent();
            finalId = intent.getIntExtra("id",0);
        }
        final boolean isActive = know.isLike(LoginActivity.myUsername,finalId);
        final Drawable liked = getResources().getDrawable(R.drawable.ic_liked);
        final Drawable tolike = getResources().getDrawable(R.drawable.ic_like_black_24dp);
        if(isActive){
            liked.setBounds(0,0,liked.getMinimumWidth(),liked.getMinimumHeight());
            favorite.setCompoundDrawables(null, liked, null, null);
            favorite.setText(getResources().getString(R.string.liked));
        }
        favorite.setOnClickListener(new View.OnClickListener()
        {//收藏按钮的切换
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view){

                if(! isActive) {
                    try {
                        know.likeOne(LoginActivity.myUsername, finalId);
                        liked.setBounds(0,0,liked.getMinimumWidth(),liked.getMinimumHeight());
                        favorite.setCompoundDrawables(null, liked, null, null);
                        favorite.setText(getResources().getString(R.string.liked));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"收藏失败",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    try {
                        know.likeOne(LoginActivity.myUsername,finalId);
                        tolike.setBounds(0,0,tolike.getMinimumWidth(),tolike.getMinimumHeight());
                        favorite.setCompoundDrawables(null, tolike, null, null);
                        favorite.setText(getResources().getString(R.string.like));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"取消收藏失败",Toast.LENGTH_SHORT).show();
                    }
                }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void likeOne(String username, int id)throws Exception {
        String url = "http://212.64.70.206:5000/likeone/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date = new Date();
        byte[] cont = String.valueOf(date.getTime()).getBytes();
        byte[] keyBytes = LoginActivity.token.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username=" + username + "&time=\"" + t + "\"" + "&id=" + id;
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
        if (responseCode != 400) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
    }

    //以下为评论和分享dialog
    //添加评论需要的变量

    private AddCommentListAdapter adapter;
    private List<FirstLevelBean> commentList = new ArrayList<>();
    private BottomSheetDialog mBottomSheetDialog2;
    private InputTextMsgDialog inputTextMsgDialog;
    private int offsetY;
    RecyclerView recyclerView;
    private float slideOffset = 0;
    //分享需要的变量
    //private Tencent mTencent;
    //private static final String APP_ID = "1105602574";

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doclick(View v)
    {
        switch (v.getId()) {
            case R.id.share:
                String title,author,content;
                if (knowledge_list!=null && knowledge_list.size()!=0){
                    Knowledge knowledge = knowledge_list.get(index);
                    content = knowledge.getContent();
                    title  = knowledge.getTitle();
                    author = knowledge.getAuthor();
                }
                else{
                    Intent intent = getIntent();
                    title = intent.getStringExtra("title");
                    content = intent.getStringExtra("content");
                    author = intent.getStringExtra("author");
                }
               // if(title!=null && content!=null && author!=null){
                    //调用系统的分享功能实现
                    Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    share_intent.setType("text/plain");//设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_SUBJECT,"这是一段分享的文字");//添加分享内容标题
                    share_intent.putExtra(Intent.EXTRA_TEXT,"【One Point分享】"+"\n"+"《"+title+"》"+"\n"+author+"\n"+content);//添加分享内容 这里可以为文章的id对应的网址
                    //创建分享的Dialog
                    share_intent =   Intent.createChooser(share_intent,"分享");
                    startActivity(share_intent);
                //}
                break;
            case R.id.add_comment:
                View view2 = getLayoutInflater().inflate(R.layout.comment_dialog_bottom_sheet, null);
                recyclerView = view2.findViewById(R.id.addcomment_recycler);
                recyclerView.setHasFixedSize(true);//设置固定大小
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);//创建线性布局
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        int know_id;
                        if (knowledge_list!=null){
                            know_id = knowledge_list.get(index).getId();
                        }else{
                            Intent intent = getIntent();
                            know_id = intent.getIntExtra("id",0);
                        }
                        getComment(LoginActivity.myUsername,know_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                /*commentList.clear();
                FirstLevelBean[] comments = {
                        new FirstLevelBean(getString(R.string.cola_img),"wulala2580","啊这",System.currentTimeMillis(),10,0),
                        new FirstLevelBean(getString(R.string.famei_img),"jizhe","abababa",System.currentTimeMillis(),20,0),
                        new FirstLevelBean( getString(R.string.snow_img),"mihu","你们说的都对",System.currentTimeMillis(),100,0)
                };
                for(int i = 0; i < comments.length; i++) {
                    commentList.add(comments[i]);
                }*/

                adapter = new AddCommentListAdapter(commentList);
                recyclerView.setAdapter(adapter);
                mBottomSheetDialog2.setContentView(view2);
                mBottomSheetDialog2.setCanceledOnTouchOutside(true);
                final BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view2.getParent());
                mDialogBehavior.setPeekHeight(getWindowHeight());
                mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetDialog.dismiss();
                            mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                            if (slideOffset <= -0.28) {
                                mBottomSheetDialog2.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        RandomKnowledgeActivity.this.slideOffset = slideOffset;

                    }
                });
                mBottomSheetDialog2.show();
                ImageView comment_close = view2.findViewById(R.id.comment_close);
                comment_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog2.dismiss();
                    }
                });
                RelativeLayout inputText = view2.findViewById(R.id.rl_comment);
                inputText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initInputTextMsgDialog(null, false, null, -1);
                    }
                });
                break;
        }
    }
    private void initInputTextMsgDialog(View view, final boolean isReply, final String headImg, final int position) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog_center);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTextSend(String msg) throws Exception {
                    addComment(isReply,headImg,position,msg);
                }

                @Override
                public void dismiss() {
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addComment(boolean isReply, String headImg, final int position, String msg) throws Exception {
        //首先在评论框内添加评论 有了服务器后要将信息发送给服务器 与相应的阅读知识id相关联
        FirstLevelBean firstLevelBean = new FirstLevelBean(0,getString(R.string.cola_img),LoginActivity.myUsername,msg,"刚刚",0,0,null);
        commentList.add(0,firstLevelBean);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
        Intent intent = getIntent();
        int know_id;
        if (knowledge_list!=null){
            know_id = knowledge_list.get(index).getId();
        }else{
            know_id=  intent.getIntExtra("id",0);
        }
        commentOne(LoginActivity.myUsername,know_id,msg);

        //其次在我的评论界面要添加评论 这里需要先向服务器添加相关内容 然后我的评论界面在打开时再向服务器获取mcommentlist
        //这里无需代码
    }
    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }

    private void showInputTextMsgDialog() {
        inputTextMsgDialog.show();
    }
    // item滑动到原位
    public void scrollLocation(int offsetY) {
        try {
            recyclerView.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 自定义监听器实现IUiListener，需要3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class ShareUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            //分享成功

        }

        @Override
        public void onError(UiError uiError) {
            //分享失败

        }

        @Override
        public void onCancel() {
            //分享取消

        }
    }
    private int getWindowHeight() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getComment(String username, int id)throws Exception{
        String url = "http://212.64.70.206:5000/getcomment/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=LoginActivity.token.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\""+"&id="+id;
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
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        JSONParse(response.toString());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void commentOne(String username, int id, String comment)throws Exception{
        String url = "http://212.64.70.206:5000/commentone/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        Date date=new Date();
        byte[] cont=String.valueOf(date.getTime()).getBytes();
        byte [] keyBytes=LoginActivity.token.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        byte[] result = cipher.doFinal(cont);
        String t = Base64.getEncoder().encodeToString(result);
        String urlParameters = "username="+username+"&time=\""+t+"\""+"&id="+id+"&comment="+ URLEncoder.encode(comment,"utf-8");
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
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
    }
    private void JSONParse(String source) throws JSONException {
        JSONArray objList = new JSONArray(source);
        commentList.clear();
        for(int i = 0; i < objList.length(); i++ ){
            JSONObject obj =  objList.getJSONObject(i);
            JSONArray reply = obj.getJSONArray("REPLY");
            //FirstLevelBean firstLevelBean = (FirstLevelBean) JSONObject.
            commentList.add(
                    new FirstLevelBean(
                            obj.getInt("COMMENTID"),getString(R.string.cola_img),obj.getString("AUTHOR"),obj.getString("COMMENT"),
                    obj.getString("PUBTIME"), (long) i,0,reply)
            );
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
                    overridePendingTransition(R.anim.trans_in_alpha, R.anim.trans_out_left);
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
