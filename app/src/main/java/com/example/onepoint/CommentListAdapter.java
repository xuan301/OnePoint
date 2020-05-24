package com.example.onepoint;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //CardView cardView;
        ImageView knowledgeImage;
        ImageView UserImage;
        TextView knowledgeTitle;
        TextView UserName;
        TextView Comment;


        public ViewHolder(View view){
            super(view);
            //cardView = (CardView) view;
            knowledgeImage = (ImageView) view.findViewById(R.id.knowledge_image);
            UserImage = view.findViewById(R.id.user_image);
            UserName = view.findViewById(R.id.username);
            Comment = view.findViewById(R.id.comment);
            knowledgeTitle = (TextView) view.findViewById(R.id.knowledge_title);
        }
    }

    public CommentListAdapter(List<Comment> commentList ){
        mCommentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType){
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.knowledgeImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //int position = holder.getAdapterPosition();
                //Knowledge knowledge = mKnowledgeList.get(position);
                Intent intent = new Intent(mContext, LikeActivity.class);
                //put extra info here, e.g.
                //intent.putExtra(RandomKnowledgeActivity.KNOWLEDGE_TITLE, knowledge.getTitle());
                mContext.startActivity(intent);
            }
        });
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        holder.knowledgeTitle.setText(comment.getTitle());
        holder.UserName.setText(comment.getUser());
        holder.Comment.setText(comment.getComment());
        Glide.with(mContext).load("https://wx2.sinaimg.cn/large/0067hdZuly1g7szjhsld7j30u01901kz.jpg").into(holder.knowledgeImage);
        Glide.with(mContext).load(comment.getUserImageId()).into(holder.UserImage);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

}
