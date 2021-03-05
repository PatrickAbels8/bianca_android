package com.example.bianca;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.adapter.RecyclerAdapterSetting;
import com.example.bianca.dialog.ClassDialog;
import com.example.bianca.helper.Constants;
import com.example.bianca.helper.Helpers;
import com.example.bianca.model.RecyclerItemLesson;
import com.example.bianca.model.RecyclerItemSetting;
import com.example.bianca.prefs.SharedPrefs;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.Activates;
import com.example.bianca.restapi.models.Lesson;
import com.example.bianca.restapi.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsFragment extends Fragment {
    private Context context;
    private Retrofit retrofit;
    private Api api;
    private View v;
    private int u_id;

    private RecyclerView recycler;
    private RecyclerAdapterSetting adapter;
    private List<RecyclerItemSetting> recyclerItemSettingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        context = getContext();

        //init db
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);
        u_id = SharedPrefs.getUserid(context);

        // init recycler
        recycler = v.findViewById(R.id.setting_recycler);
        recyclerItemSettingList = new ArrayList<RecyclerItemSetting>(Arrays.asList(
                new RecyclerItemSetting("")
        ));
        adapter = new RecyclerAdapterSetting(context, recyclerItemSettingList);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        getSettings();

        // expand setting
        adapter.setOnItemClickListener(new RecyclerAdapterSetting.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                recyclerItemSettingList.get(position).setExpanded(!recyclerItemSettingList.get(position).isExpanded());
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onOkClick(int position, RelativeLayout layout) {
                activateOkButton(position, layout);
            }
        });

        return v;
    }

    public void getSettings(){
        recyclerItemSettingList.clear();
        recyclerItemSettingList.addAll(Arrays.asList(
                new RecyclerItemSetting(getString(R.string.settings_selectQuestions)),
                //new RecyclerItemSetting(getString(R.string.settings_changeName)),
                new RecyclerItemSetting(getString(R.string.settings_changeLang)),
                new RecyclerItemSetting(getString(R.string.settings_logout))
        ));
        if(SharedPrefs.getUsername(context).equals(getString(R.string.admin_username)))
            recyclerItemSettingList.add(new RecyclerItemSetting(getString(R.string.settings_changePass)));
        adapter.notifyDataSetChanged();
    }

    public void activateOkButton(int position, RelativeLayout layout){
        String title = recyclerItemSettingList.get(position).getTitle();
        if(title.equals(getString(R.string.settings_logout))){
            logout();
        }else if(title.equals(getString(R.string.settings_changeName))){
            String name = ((TextInputLayout)((RelativeLayout)layout.getChildAt(0)).getChildAt(0)).getEditText().getText().toString();
            changeName(name);
        }else if(title.equals(getString(R.string.settings_changePass))){
            String name = ((TextInputLayout)((RelativeLayout)layout.getChildAt(0)).getChildAt(0)).getEditText().getText().toString();
            String pass = ((TextInputLayout)((RelativeLayout)layout.getChildAt(0)).getChildAt(1)).getEditText().getText().toString();
            String pass_repeat = ((TextInputLayout)((RelativeLayout)layout.getChildAt(0)).getChildAt(2)).getEditText().getText().toString();
            changePass(name, pass, pass_repeat);
        }else if(title.equals(getString(R.string.settings_selectQuestions))){
            int num_childs = ((LinearLayout)layout.getChildAt(0)).getChildCount();
            for(int i=0; i<num_childs; i++){
                CheckBox box = (CheckBox)((LinearLayout)layout.getChildAt(0)).getChildAt(i);
                boolean yes = box.isChecked();
                int l_id = (int)box.getTag();
                activateLesson(l_id, yes);
            }
        }else if(title.equals(getString(R.string.settings_changeLang))){
            RadioGroup group = (RadioGroup)((RelativeLayout)layout.getChildAt(0)).getChildAt(0);
            int checked_id = group.getCheckedRadioButtonId();
            RadioButton button = group.findViewById(checked_id);
            String lang = button.getText().toString();
            if(lang.equals(getString(R.string.settings_changeLang_de)))
                changeLang(getString(R.string.lang_de));
            else if(lang.equals(getString(R.string.settings_changeLang_en)))
                changeLang(getString(R.string.lang_en));
            else if(lang.equals(getString(R.string.settings_changeLang_tr)))
                changeLang(getString(R.string.lang_tr));
        }
        getActivity().recreate();
    }

    /**
     * setting functions / api
     */

    public void logout(){
        SharedPrefs.setLoggedIn(context, false);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    public void changeLang(String lang){
        SharedPrefs.setLanguage(context, lang);
        Helpers.setLocale(getActivity().getBaseContext(), lang);
    }

    public void activateLesson(int l_id, boolean yes){
        Call<Integer> call = api.replaceActivates(new Activates(u_id, l_id, yes?1:0));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                Integer success = response.body();
                if(success==Constants.SUCCESS)
                    Log.d("settings", "activation changed");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void changeName(String name){
        if(name.length()==0)
            return;
        Toast.makeText(context, "new name: "+name, Toast.LENGTH_SHORT).show();
    }

    public void changePass(String name, String pass, String pass_repeat){
        if(name.isEmpty() || pass.isEmpty() || pass_repeat.isEmpty())
            return;
        if(!pass.equals(pass_repeat)) {
            Toast.makeText(context, getString(R.string.error_login_wrongPassRepeat), Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Call<Integer> call_update = api.updateUser(new User(name, pass));
            call_update.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(!response.isSuccessful()){
                        Log.d("onResponse", "Code: "+response.code());
                        return;
                    }
                    Integer success = response.body();
                    if(success== Constants.SUCCESS)
                        Toast.makeText(context, getString(R.string.error_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
