package com.example.onepoint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onepoint.bean.FirstLevelBean;
import com.example.onepoint.bean.SecondLevelBean;
import com.example.onepoint.dialog.InputTextMsgDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class AddCommentListAdapter extends RecyclerView.Adapter<AddCommentListAdapter.ViewHolder>  {
    private final String USER_AGENT = "Mozilla/5.0";
    private Context mContext;
    private List<FirstLevelBean> mCommentList;
    RecyclerView recyclerView;
    private InputTextMsgDialog inputTextMsgDialog;
    private int offsetY;
    private float slideOffset=0;
    private ReplyCommentListAdpater adapter;
    private BottomSheetDialog reply_dialog;
    private String comment;//为了给reply传递索引
    private int commentid;


    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView UserImage;
        TextView UserName;
        TextView Comment;
        ImageView like;
        TextView like_cout;
        ImageView reply;
        TextView time;


        public ViewHolder(View view){
            super(view);
            //cardView = (CardView) view;
            UserImage = view.findViewById(R.id.iv_header);
            UserName = view.findViewById(R.id.tv_user_name);
            Comment = view.findViewById(R.id.tv_content);
            like = view.findViewById(R.id.iv_like);
            like_cout = view.findViewById(R.id.tv_like_count);
            reply = view.findViewById(R.id.reply);
            time = view.findViewById(R.id.tv_time);
        }
    }

    public AddCommentListAdapter(List<FirstLevelBean> commentList ){
        mCommentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType){
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.addcomment_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FirstLevelBean firstLevelBean = mCommentList.get(position);
        comment = firstLevelBean.getContent();
        commentid  = firstLevelBean.getCommentid();
        holder.UserName.setText(firstLevelBean.getUserName());
        holder.Comment.setText(firstLevelBean.getContent());
        holder.time.setText(firstLevelBean.getCreateTime());
        holder.like_cout.setText(firstLevelBean.getLikeCount()+"");
        Glide.with(mContext).load(firstLevelBean.getHeadImg()).into(holder.UserImage);
        holder.like.setOnClickListener(new View.OnClickListener() {
            boolean isActive = false;
            @Override
            public void onClick(View v) {
                if (isActive){
                    holder.like.setImageResource(R.mipmap.icon_topic_post_item_like);
                    firstLevelBean.setIsLike(0);
                    isActive = false;
                }else {
                    holder.like.setImageResource(R.mipmap.icon_topic_post_item_like_blue);
                    firstLevelBean.setIsLike(1);
                    isActive = true;
                }
                holder.like_cout.setText(firstLevelBean.getLikeCount()+firstLevelBean.getIsLike()+"");
            }
        });
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                View view = View.inflate(mContext,R.layout.reply_dialog_bottom_sheet,null);
                TextView none = view.findViewById(R.id.none);
                recyclerView = view.findViewById(R.id.reply_recycler);
                recyclerView.setHasFixedSize(true);//设置固定大小
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);//创建线性布局
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
                final List<SecondLevelBean> replyList = new ArrayList<>();
                //输入数据
                JSONArray reply_list = firstLevelBean.getReply();
                for (int i = 0;i<reply_list.length();++i){
                    while(true) {
                        try {
                            JSONObject reply = reply_list.getJSONObject(i);
                            replyList.add(
                                    new SecondLevelBean(getPhoto(LoginActivity.myUsername, reply.getString("AUTHOR")), reply.getString("AUTHOR"), reply.getString("REPLY"), 10, 1)
                            );
                        } catch (JSONException e) {
                            if (Objects.equals(e.getMessage(), "unexpected end of stream")) {
                                continue;
                            }
                            else{
                                e.printStackTrace();
                            }
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
                }
                //设置adapter
                adapter = new ReplyCommentListAdpater(replyList);
                recyclerView.setAdapter(adapter);
                show(view);
                ImageView close = view.findViewById(R.id.reply_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reply_dialog.dismiss();
                    }
                });
                RelativeLayout inputText = view.findViewById(R.id.rl_comment);
                inputText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initInputTextMsgDialog(null, false, null, -1,replyList);
                    }
                });
            }
        });
    }
    private  void show(View view){
        reply_dialog = new BottomSheetDialog(mContext,R.style.dialog);
        reply_dialog.setContentView(view);
        reply_dialog.setCanceledOnTouchOutside(true);
        final BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetDialog.dismiss();
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        reply_dialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                AddCommentListAdapter.this.slideOffset = slideOffset;

            }
        });
        reply_dialog.show();
    }
    private void initInputTextMsgDialog(View view, final boolean isReply, final String headImg, final int position, final List<SecondLevelBean> replyList) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(mContext, R.style.dialog_center);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTextSend(String msg) throws Exception {
                    addReply(isReply,headImg,position,msg,replyList);
                }

                @Override
                public void dismiss() {
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
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
    String imagesrc;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addReply(boolean isReply, String headImg, final int position, String msg, List<SecondLevelBean> replyList) throws Exception {
        //首先在评论框内添加评论 有了服务器后要将信息发送给服务器 与相应的阅读知识id相关联
        while (true){
            try {
                imagesrc = getPhoto(LoginActivity.myUsername,LoginActivity.myUsername);
            }catch (Exception e){
                if (Objects.equals(e.getMessage(), "unexpected end of stream")) {
                    continue;
                }
                else{
                    e.printStackTrace();
                }
            }
            break;
        }
        SecondLevelBean secondLevelBean = new SecondLevelBean(imagesrc,LoginActivity.myUsername,msg,0,0);
        replyList.add(0,secondLevelBean);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
        Know know = new Know();
        SharedPreferences sharedPreferences= mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        know.token = sharedPreferences.getString("token",null);
        replyOne(LoginActivity.myUsername,commentid,msg);
        //其次在我的评论界面要添加评论 这里需要先向服务器添加相关内容 然后我的评论界面在打开时再向服务器获取mcommentlist
        //这里无需代码
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void replyOne(String username, int id, String comment)throws Exception{
        String url = "http://212.64.70.206:5000/replyone/";
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
    private int getWindowHeight() {
        Resources res = mContext.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

}
