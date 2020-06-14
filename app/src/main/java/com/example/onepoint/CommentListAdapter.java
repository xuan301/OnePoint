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

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView knowledgeImage;
        ImageView UserImage;
        TextView knowledgeTitle;
        TextView UserName;
        TextView Comment;
        TextView delete;
        TextView time;



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
            time = view.findViewById(R.id.time);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.mycomment_item,
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
        holder.time.setText(comment.getTime());
        Glide.with(mContext).load(comment.getKnowledgeImagesrc()).into(holder.knowledgeImage);
        Glide.with(mContext).load(comment.getUserImage()).into(holder.UserImage);
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
                intent.putExtra("author",comment.getAuthor());
                intent.putExtra("id",comment.getKnowid());
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
