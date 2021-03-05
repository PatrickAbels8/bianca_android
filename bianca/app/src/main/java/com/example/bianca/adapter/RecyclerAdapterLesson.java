package com.example.bianca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.main.AsyncTaskClass;
import com.example.bianca.model.RecyclerItemLesson;
import com.example.bianca.restapi.Client;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapterLesson extends RecyclerView.Adapter<RecyclerAdapterLesson.LessonViewHolder>{
    private Context context;
    private List<RecyclerItemLesson> recyclerItemLessonList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public RecyclerAdapterLesson(Context context, List<RecyclerItemLesson> recyclerItemLessonList) {
        this.context = context;
        this.recyclerItemLessonList = recyclerItemLessonList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LessonViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_lesson, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        //holder.id.setText("IX");
        String id = getRomanNumber(recyclerItemLessonList.get(position).getId());
        holder.id.setText(id);
        holder.title.setText(recyclerItemLessonList.get(position).getTitle());
        //holder.image.setImageResource(recyclerItemLessonList.get(position).getImageUrl());
        AsyncTaskClass taskClass = new AsyncTaskClass(holder.image, context);
        taskClass.execute(Client.getApiThumbnailsUrlAppendix()+recyclerItemLessonList.get(position).getImageUrl());

    }

    public String getRomanNumber(int number) {
        return String.join("", Collections.nCopies(number, "I"))
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }

    @Override
    public int getItemCount() {
        return recyclerItemLessonList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView title;
        ImageView image;

        public LessonViewHolder(@NonNull View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            id = itemView.findViewById(R.id.item_id);
            title = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
