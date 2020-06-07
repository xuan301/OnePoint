package com.example.onepoint;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;


public class AddCommentListAdapter extends RecyclerView.Adapter<AddCommentListAdapter.ViewHolder>  {
    private Context mContext;
    private List<FirstLevelBean> mCommentList;
    RecyclerView recyclerView;
    private InputTextMsgDialog inputTextMsgDialog;
    private int offsetY;
    private float slideOffset=0;
    //private List<SecondLevelBean> replyList = new ArrayList<>();
    private ReplyCommentListAdpater adapter;
    private BottomSheetDialog reply_dialog;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView UserImage;
        TextView UserName;
        TextView Comment;
        ImageView like;
        TextView like_cout;
        ImageView reply;


        public ViewHolder(View view){
            super(view);
            //cardView = (CardView) view;
            UserImage = view.findViewById(R.id.iv_header);
            UserName = view.findViewById(R.id.tv_user_name);
            Comment = view.findViewById(R.id.tv_content);
            like = view.findViewById(R.id.iv_like);
            like_cout = view.findViewById(R.id.tv_like_count);
            reply = view.findViewById(R.id.reply);
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
        //final int islike = firstLevelBean.getIsLike();
        holder.UserName.setText(firstLevelBean.getUserName());
        holder.Comment.setText(firstLevelBean.getContent());
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
                //init_relpy_list

                /*if (replyList.size()==0){
                    none.setText("暂无回复，快来抢沙发！");
                }*/
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

        /*holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                if (position<0) return;
                Comment comment = mCommentList.get(position);
                Intent intent = new Intent(mContext, RandomKnowledgeActivity.class);
                //put extra info here, e.g.
                //intent.putExtra("index",position);
                //intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) mCommentList);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("title", comment.getTitle());
                intent.putExtra("imageSrc", comment.getKnowledgeImagesrc());
                intent.putExtra("content",comment.getContent());
                mContext.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position<0) return;
                mCommentList.remove(position);
                notifyItemRemoved(position);
                //Intent intent = new Intent(mContext,CommentActivity.class);
                //mContext.startActivity(intent);
            }
        });*/
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
                @Override
                public void onTextSend(String msg) {
                    addComment(isReply,headImg,position,msg,replyList);
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
    public void addComment(boolean isReply, String headImg, final int position, String msg,List<SecondLevelBean> replyList){
        //首先在评论框内添加评论 有了服务器后要将信息发送给服务器 与相应的阅读知识id相关联
        SecondLevelBean secondLevelBean = new SecondLevelBean(mContext.getString(R.string.xigua_img),"liyifei",msg,0,0);
        replyList.add(secondLevelBean);
        /*View view = View.inflate(mContext,R.layout.reply_dialog_bottom_sheet,null);
        TextView none = view.findViewById(R.id.none);
        if (none.getText()!=null){
            none.setText(null);
            show(view);
        }*/
        //其次在我的评论界面要添加评论 这里需要先向服务器添加相关内容 然后我的评论界面在打开时再向服务器获取mcommentlist
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
