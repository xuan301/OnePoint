package com.example.onepoint;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AddCommentListAdapter extends RecyclerView.Adapter<AddCommentListAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mCommentList;
    //private List<Knowledge> knowledgeList= new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView UserImage;
        TextView UserName;
        TextView Comment;
        ImageView like;


        public ViewHolder(View view){
            super(view);
            //cardView = (CardView) view;
            UserImage = view.findViewById(R.id.iv_header);
            UserName = view.findViewById(R.id.tv_user_name);
            Comment = view.findViewById(R.id.tv_content);
            like = view.findViewById(R.id.iv_like);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.addcomment_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        holder.UserName.setText(comment.getUser());
        holder.Comment.setText(comment.getComment());
        Glide.with(mContext).load(comment.getUserImageId()).into(holder.UserImage);
        holder.like.setOnClickListener(new View.OnClickListener() {
            boolean isActive = false;
            @Override
            public void onClick(View v) {
                if (isActive){
                    holder.like.setImageResource(R.mipmap.icon_topic_post_item_like);
                    isActive = false;
                }else {
                    holder.like.setImageResource(R.mipmap.icon_topic_post_item_like_blue);
                    isActive = true;
                }
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

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

}
