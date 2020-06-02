package com.example.onepoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddCommentListAdapter extends RecyclerView.Adapter<AddCommentListAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mCommentList;
    //private List<Knowledge> knowledgeList= new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView knowledgeImage;
        ImageView UserImage;
        TextView knowledgeTitle;
        TextView UserName;
        TextView Comment;
        TextView delete;



        public ViewHolder(View view){
            super(view);
            //cardView = (CardView) view;
            knowledgeImage = (ImageView) view.findViewById(R.id.knowledge_image);
            UserImage = view.findViewById(R.id.user_image);
            UserName = view.findViewById(R.id.username);
            Comment = view.findViewById(R.id.comment);
            knowledgeTitle = (TextView) view.findViewById(R.id.knowledge_title);
            cardView = view.findViewById(R.id.knowledge);
            delete = view.findViewById(R.id.delete);
        }
    }

    public AddCommentListAdapter(List<Comment> commentList ){
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
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        String title = comment.getTitle();
        if (title.length()>8){
            holder.knowledgeTitle.setText(title.substring(0,8)+"...");
        }
        else {
            holder.knowledgeTitle.setText(title);
        }
        holder.UserName.setText(comment.getUser());
        holder.Comment.setText(comment.getComment());
        Glide.with(mContext).load(comment.getKnowledgeImagesrc()).into(holder.knowledgeImage);
        Glide.with(mContext).load(comment.getUserImageId()).into(holder.UserImage);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
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
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

}
