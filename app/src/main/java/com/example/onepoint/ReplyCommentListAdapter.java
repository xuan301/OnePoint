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


class ReplyCommentListAdpater extends RecyclerView.Adapter<ReplyCommentListAdpater.ViewHolder>  {
    private Context mContext;
    private List<SecondLevelBean> mReplyList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView UserImage;
        TextView UserName;
        TextView reply;
        ImageView like;
        TextView like_cout;


        public ViewHolder(View view){
            super(view);
            UserImage = view.findViewById(R.id.iv_header);
            UserName = view.findViewById(R.id.tv_user_name);
            reply = view.findViewById(R.id.tv_content);
            like = view.findViewById(R.id.iv_like);
            like_cout = view.findViewById(R.id.tv_like_count);
        }
    }

    public ReplyCommentListAdpater(List<SecondLevelBean> replyList ){
        mReplyList = replyList;
    }

    @Override
    public ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType){
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.reply_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SecondLevelBean secondLevelBean = mReplyList.get(position);
        //final int islike = firstLevelBean.getIsLike();
        holder.UserName.setText(secondLevelBean.getUserName());
        holder.reply.setText(secondLevelBean.getContent());
        holder.like_cout.setText(secondLevelBean.getLikeCount()+"");
        Glide.with(mContext).load(secondLevelBean.getHeadImg()).into(holder.UserImage);
        holder.like.setOnClickListener(new View.OnClickListener() {
            boolean isActive = false;
            @Override
            public void onClick(View v) {
                if (isActive){
                    holder.like.setImageResource(R.mipmap.icon_topic_post_item_like);
                    secondLevelBean.setIsLike(0);
                    isActive = false;
                }else {
                    holder.like.setImageResource(R.mipmap.icon_topic_post_item_like_blue);
                    secondLevelBean.setIsLike(1);
                    isActive = true;
                }
                holder.like_cout.setText(secondLevelBean.getLikeCount()+secondLevelBean.getIsLike()+"");
            }
        });

    }


    @Override
    public int getItemCount() {
        return mReplyList.size();
    }

}
