package com.example.bianca.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.adapter.RecyclerAdapterLesson;
import com.example.bianca.model.RecyclerItemLesson;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.Lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClassDialog extends DialogFragment {
    private Context context;
    private Retrofit retrofit;
    private Api api;

    private TextView header;
    private RecyclerView recycler;
    private RecyclerAdapterLesson adapter;
    private List<RecyclerItemLesson> recyclerItemLessonList;

    private String key, del;

    public interface OnSelected{
        void selected(String video_url);
    }
    public OnSelected onSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_lessons, container, false);
        context = getContext();

        //init db
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);

        //init coms
        header = v.findViewById(R.id.header);
        recycler = v.findViewById(R.id.lesson_recycler);

        //get class+school
        Bundle args = getArguments();
        key = args.getString(getString(R.string.bundle_key));
        del = getString(R.string.delimiter_key);
        String school_string = key.split(del)[0];
        String class_string = key.split(del)[1];
        header.setText(school_string+getString(R.string.prefix_lessonHeader)+" "+class_string);

        // init recycler
        recyclerItemLessonList = new ArrayList<RecyclerItemLesson>(Arrays.asList(
                new RecyclerItemLesson(0, "Noch keine Einheiten.", "", "")
        ));
        adapter = new RecyclerAdapterLesson(context, recyclerItemLessonList);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        readLessons(school_string, class_string);

        // start lesson
        adapter.setOnItemClickListener(new RecyclerAdapterLesson.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String video_url = recyclerItemLessonList.get(position).getVideoUrl();
                onSelected.selected(video_url);
                getDialog().dismiss();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout((9*getResources().getDisplayMetrics().widthPixels)/10, (4*getResources().getDisplayMetrics().heightPixels)/5);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onSelected = (OnSelected) getTargetFragment();
        }catch(ClassCastException e){
            Log.e("ClassDialog", "onAttach: ClassCastException : "+e.getMessage());
        }
    }

    /**
     * api
     */
    public void readLessons(String school_string, String class_string){
        final String _school_string = school_string;
        final String _class_string = class_string;
        recyclerItemLessonList.clear();
        adapter.notifyDataSetChanged();
        Call<List<Lesson>> call = api.getLessons();
        call.enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                List<Lesson> lessons = response.body();
                for(int i=0; i<lessons.size(); i++){
                    //contodo only show if relevant to school and class
                    RecyclerItemLesson l_item = new RecyclerItemLesson(lessons.get(i).getId(), lessons.get(i).getTitle(), lessons.get(i).getImage(), lessons.get(i).getVideo());
                    recyclerItemLessonList.add(l_item);
                    adapter.notifyItemInserted(i);
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
