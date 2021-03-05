package com.example.bianca.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.model.RecyclerItemLesson;
import com.example.bianca.model.RecyclerItemRank;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapterRank extends RecyclerView.Adapter<RecyclerAdapterRank.RankViewHolder>{
    private Context context;
    private List<RecyclerItemRank> recyclerItemRankList;

    public RecyclerAdapterRank(Context context, List<RecyclerItemRank> recyclerItemRankList) {
        this.context = context;
        this.recyclerItemRankList = recyclerItemRankList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RankViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_rank, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        int rank = position+1;
        String id = Integer.toString(rank)+".";
        holder.id.setText(id);
        if(rank==1) {
            holder.id.setBackgroundColor(context.getColor(R.color.gold));
            holder.id.setTextColor(context.getColor(R.color.first));
        }
        else if(rank==2) {
            holder.id.setBackgroundColor(context.getColor(R.color.silver));
            holder.id.setTextColor(context.getColor(R.color.first));
        }
        else if(rank==3) {
            holder.id.setBackgroundColor(context.getColor(R.color.bronze));
            holder.id.setTextColor(context.getColor(R.color.first));
        }
        else{
            holder.id.setBackgroundColor(context.getColor(R.color.first));
            holder.id.setTextColor(context.getColor(R.color.second));
        }
        holder.name.setText(recyclerItemRankList.get(position).getName());
        holder.score.setText(Integer.toString(recyclerItemRankList.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return recyclerItemRankList.size();
    }

    public static class RankViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView name;
        TextView score;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_id);
            name = itemView.findViewById(R.id.item_name);
            score = itemView.findViewById(R.id.item_score);
        }
    }
}
