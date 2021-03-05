package com.example.bianca.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.TrainingFragment;
import com.example.bianca.dialog.ClassDialog;
import com.example.bianca.model.RecyclerItemClass;

import java.util.List;

public class RecyclerAdapterClass extends RecyclerView.Adapter<RecyclerAdapterClass.ClassViewHolder> {
    private Context context;
    private List<RecyclerItemClass> recyclerItemClassList;

    private Fragment f;
    private FragmentManager fm;

    public RecyclerAdapterClass(Context context, List<RecyclerItemClass> recyclerItemClassList, Fragment f, FragmentManager fm) {
        this.context = context;
        this.recyclerItemClassList = recyclerItemClassList;

        this.f = f;
        this.fm = fm;
    }

    @NonNull
    @Override
    public RecyclerAdapterClass.ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClassViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.image.setImageResource(recyclerItemClassList.get(position).getImageUrl());
        holder.title.setText(recyclerItemClassList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = recyclerItemClassList.get(position).getKey(context);

                Bundle args = new Bundle();
                args.putString(context.getString(R.string.bundle_key), key);
                ClassDialog dialog = new ClassDialog();
                dialog.setTargetFragment(f, 1);
                dialog.setArguments(args);
                dialog.show(fm, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerItemClassList.size();
    }

    public static final class ClassViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
        }
    }
}
