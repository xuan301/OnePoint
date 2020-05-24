package com.example.onepoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
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
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Knowledge knowledge = mKnowledgeList.get(position);
        holder.knowledgeTitle.setText(knowledge.getTitle());

//        RequestListener mRequestListener  = new RequestListener() {
//            @Override
//            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
//                Log.e("Glide report:"," onException: "+ e.toString() + " model: "+ model.toString() + " isFirstResource: "+ isFirstResource);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
//                return false;
//            }
//        };
//
//  Glide的RequestListener，用于检查图片加载时的错误。
//  典型报错：java.net.SocketException: socket failed: EPERM (Operation not permitted)。原因是创建App时未添加网络权限，现在已经添加，但需要刷新。
//  在模拟器的设置中卸载App后重新运行即可恢复

        Glide.with(mContext).load(knowledge.getImageSrc())
                //.listener(mRequestListener)
                .into(holder.knowledgeImage);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                if(position<0) return;
                Knowledge knowledge = mKnowledgeList.get(position);
                Intent intent = new Intent(mContext, RandomKnowledgeActivity.class);
                //put extra info here, e.g.
                intent.putExtra("title", knowledge.getTitle());
                intent.putExtra("imageSrc", knowledge.getImageSrc());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKnowledgeList.size();
    }

}
