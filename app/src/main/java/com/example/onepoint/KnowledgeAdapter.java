package com.example.onepoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.ViewHolder> {
    private Context mContext;
    private List<Knowledge> mKnowledgeList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView knowledgeImage;
        TextView knowledgeTitle;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            knowledgeImage = (ImageView) view.findViewById(R.id.knowledge_image);
            knowledgeTitle = (TextView) view.findViewById(R.id.knowledge_title);
        }
    }

    public KnowledgeAdapter(List<Knowledge> knowledgeList ){
        mKnowledgeList = knowledgeList;
    }

    @Override
    public ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType){
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.knowledge_item_cardview,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //int position = holder.getAdapterPosition();
                //Knowledge knowledge = mKnowledgeList.get(position);
                Intent intent = new Intent(mContext, RandomKnowledgeActivity.class);
                //put extra info here, e.g.
                //intent.putExtra(RandomKnowledgeActivity.KNOWLEDGE_TITLE, knowledge.getTitle());
                mContext.startActivity(intent);
            }
        });
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Knowledge knowledge = mKnowledgeList.get(position);
        holder.knowledgeTitle.setText(knowledge.getTitle());
        Glide.with(mContext).load(knowledge.getImageSrc()).into(holder.knowledgeImage);
    }

    @Override
    public int getItemCount() {
        return mKnowledgeList.size();
    }

}
