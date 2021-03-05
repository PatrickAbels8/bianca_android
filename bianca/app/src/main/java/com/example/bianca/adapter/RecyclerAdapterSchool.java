package com.example.bianca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.model.RecyclerItemSchool;

import java.util.List;

public class RecyclerAdapterSchool extends RecyclerView.Adapter<RecyclerAdapterSchool.SchoolViewHolder> {
    private Context context;
    private List<RecyclerItemSchool> recyclerItemSchoolList;

    private Fragment f;
    private FragmentManager fm;

    public RecyclerAdapterSchool(Context context, List<RecyclerItemSchool> recyclerItemSchoolList, Fragment f, FragmentManager fm) {
        this.context = context;
        this.recyclerItemSchoolList = recyclerItemSchoolList;

        this.f = f;
        this.fm = fm;
    }

    @NonNull
    @Override
    public RecyclerAdapterSchool.SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_school, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSchool.SchoolViewHolder holder, int position) {
        holder.title.setText(recyclerItemSchoolList.get(position).getTitle());
        holder.recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.recycler.setAdapter(new RecyclerAdapterClass(context, recyclerItemSchoolList.get(position).getRecyclerItemClassList(), f, fm));
    }

    @Override
    public int getItemCount() {
        return recyclerItemSchoolList.size();
    }

    public static final class SchoolViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        RecyclerView recycler;

        public SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.school_title);
            recycler = itemView.findViewById(R.id.class_recycler);
        }
    }
}
