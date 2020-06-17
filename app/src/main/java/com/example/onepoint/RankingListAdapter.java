package com.example.onepoint;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.ViewHolder> {
    private Context mContext;
    private List<Knowledge> mKnowledgeList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView knowledgeImage;
        TextView knowledgeTitle;
        TextView favoriteNumber;

        ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            knowledgeImage = view.findViewById(R.id.ranking_image);
            knowledgeTitle = view.findViewById(R.id.ranking_title);
            favoriteNumber = view.findViewById(R.id.favorite_number);
        }
    }

    RankingListAdapter(List<Knowledge> knowledgeList){
        mKnowledgeList = knowledgeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.ranking_item_cardview,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Knowledge knowledge = mKnowledgeList.get(position);
        holder.knowledgeTitle.setText(knowledge.getTitle());
        holder.favoriteNumber.setText("❤ " + knowledge.getLike_num());

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
//                Knowledge knowledge = mKnowledgeList.get(position);
                Intent intent = new Intent(mContext, RandomKnowledgeActivity.class);
                //put extra info here, e.g.
//                intent.putExtra("title", knowledge.getTitle());
//                intent.putExtra("imageSrc", knowledge.getImageSrc());
//                intent.putExtra("content",knowledge.getContent());
                intent.putExtra("index",position);
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) mKnowledgeList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKnowledgeList.size();
    }

}
