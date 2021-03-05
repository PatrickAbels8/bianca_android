package com.example.bianca;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.adapter.RecyclerAdapterLesson;
import com.example.bianca.adapter.RecyclerAdapterRank;
import com.example.bianca.model.RecyclerItemLesson;
import com.example.bianca.model.RecyclerItemRank;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.Lesson;
import com.example.bianca.restapi.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private Context context;
    private Retrofit retrofit;
    private Api api;

    private RecyclerView recycler;
    private RecyclerAdapterRank adapter;
    private List<RecyclerItemRank> recyclerItemRankList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();

        //init db
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);

        // init recycler
        recycler = v.findViewById(R.id.rank_recycler);
        recyclerItemRankList = new ArrayList<RecyclerItemRank>(Arrays.asList(
                new RecyclerItemRank("Noch keine Nutzer.", 42)
        ));
        adapter = new RecyclerAdapterRank(context, recyclerItemRankList);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        readRanks();

        return v;
    }

    public void readRanks(){
        recyclerItemRankList.clear();
        adapter.notifyDataSetChanged();
        Call<List<User>> call = api.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                List<User> users = response.body();
                for(int i=0; i<users.size(); i++){
                    RecyclerItemRank r_item = new RecyclerItemRank(users.get(i).getName(), users.get(i).getElo());
                    recyclerItemRankList.add(r_item);
                }
                adapter.notifyDataSetChanged();
                rankUsers();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void rankUsers(){
        Collections.sort(recyclerItemRankList, new Comparator<RecyclerItemRank>() {
            @Override
            public int compare(RecyclerItemRank o1, RecyclerItemRank o2) {
                return o1.getScore().compareTo(o2.getScore()) * -1;
            }
        });
        adapter.notifyDataSetChanged();
    }
}
