package com.example.bianca.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.restapi.Client;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ItemViewHolder> {
    private Context context;
    private List<QuestionItem> questionItems;

    public QuestionAdapter(Context context, List<QuestionItem> questionItems) {
        this.context = context;
        this.questionItems = questionItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position){
        AsyncTaskClass taskClass = new AsyncTaskClass(holder.itemImage, context);
        taskClass.execute(Client.getApiQuestionsUrlAppendix()+questionItems.get(position).getImage());

        //holder.itemImage.setImageResource(R.drawable.image);
    }

    @Override
    public int getItemCount(){
        return questionItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
        }
    }
}
