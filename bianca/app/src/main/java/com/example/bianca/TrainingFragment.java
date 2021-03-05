package com.example.bianca;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.adapter.RecyclerAdapterSchool;
import com.example.bianca.dialog.ClassDialog;
import com.example.bianca.model.RecyclerItemClass;
import com.example.bianca.model.RecyclerItemSchool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainingFragment extends Fragment implements ClassDialog.OnSelected{
    private Context context;

    private RecyclerView schoolRecycler;
    private RecyclerAdapterSchool schoolAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_training, container, false);
        context = getContext();

        // read user data

        // nested recycler
        schoolRecycler = v.findViewById(R.id.school_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        schoolRecycler.setLayoutManager(layoutManager);
        schoolAdapter = new RecyclerAdapterSchool(context, getSchools(), TrainingFragment.this, getFragmentManager());
        schoolRecycler.setAdapter(schoolAdapter);

        return v;
    }

    @Override
    public void selected(String video_url) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_url));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video_url));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }

    private List<RecyclerItemSchool> getSchools(){
        String g8 = getString(R.string.gymnasium_g8);
        String g9 = getString(R.string.gymnasium_g9);
        String berufgym = getString(R.string.berufliches_gymnasium);
        return new ArrayList<RecyclerItemSchool>(Arrays.asList(
            new RecyclerItemSchool(berufgym, new ArrayList<RecyclerItemClass>(Arrays.asList(
                    new RecyclerItemClass(1, R.color.bad, getString(R.string.gymnasium_e), berufgym)/*,
                    new RecyclerItemClass(2, R.color.bad, getString(R.string.gymnasium_q1), berufgym),
                    new RecyclerItemClass(3, R.color.bad, getString(R.string.gymnasium_q2), berufgym),
                    new RecyclerItemClass(4, R.color.bad, getString(R.string.gymnasium_q3), berufgym),
                    new RecyclerItemClass(5, R.color.bad, getString(R.string.gymnasium_q4), berufgym)*/
            )))/*,
            new RecyclerItemSchool(g8, new ArrayList<RecyclerItemClass>(Arrays.asList(
                new RecyclerItemClass(1, R.color.good, getString(R.string.gymnasium_5), g8),
                new RecyclerItemClass(2, R.color.good, getString(R.string.gymnasium_6), g8),
                new RecyclerItemClass(3, R.color.good, getString(R.string.gymnasium_7), g8),
                new RecyclerItemClass(4, R.color.medium, getString(R.string.gymnasium_8), g8),
                new RecyclerItemClass(5, R.color.medium, getString(R.string.gymnasium_9), g8),
                new RecyclerItemClass(6, R.color.bad, getString(R.string.gymnasium_e), g8),
                new RecyclerItemClass(7, R.color.bad, getString(R.string.gymnasium_q1), g8),
                new RecyclerItemClass(8, R.color.bad, getString(R.string.gymnasium_q2), g8),
                new RecyclerItemClass(9, R.color.bad, getString(R.string.gymnasium_q3), g8),
                new RecyclerItemClass(10, R.color.bad, getString(R.string.gymnasium_q4), g8)
            ))),
            new RecyclerItemSchool(g9, new ArrayList<RecyclerItemClass>(Arrays.asList(
                new RecyclerItemClass(1, R.color.good,getString(R.string.gymnasium_5), g9),
                new RecyclerItemClass(2, R.color.good,getString(R.string.gymnasium_6), g9),
                new RecyclerItemClass(3, R.color.good,getString(R.string.gymnasium_7), g9),
                new RecyclerItemClass(4, R.color.medium,getString(R.string.gymnasium_8), g9),
                new RecyclerItemClass(5, R.color.medium,getString(R.string.gymnasium_9), g9),
                new RecyclerItemClass(6, R.color.medium, getString(R.string.gymnasium_10), g9),
                new RecyclerItemClass(7, R.color.bad,getString(R.string.gymnasium_e), g9),
                new RecyclerItemClass(8, R.color.bad, getString(R.string.gymnasium_q1), g9),
                new RecyclerItemClass(9, R.color.bad, getString(R.string.gymnasium_q2), g9),
                new RecyclerItemClass(10, R.color.bad, getString(R.string.gymnasium_q3), g9),
                new RecyclerItemClass(11, R.color.bad, getString(R.string.gymnasium_q4), g9)
            )))*/
        ));
    }
}
