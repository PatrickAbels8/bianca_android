package com.example.bianca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bianca.R;
import com.example.bianca.model.RecyclerItemLesson;
import com.example.bianca.model.RecyclerItemSetting;
import com.example.bianca.prefs.SharedPrefs;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.Activates;
import com.example.bianca.restapi.models.Lesson;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerAdapterSetting extends RecyclerView.Adapter<RecyclerAdapterSetting.SettingViewHolder>{
    private Context context;
    private Retrofit retrofit;
    private Api api;

    private List<RecyclerItemSetting> recyclerItemSettingList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onOkClick(int position, RelativeLayout layout);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public RecyclerAdapterSetting(Context context, List<RecyclerItemSetting> recyclerItemSettingList) {
        this.context = context;
        this.retrofit = Client.getRetrofitInstance();
        this.api = retrofit.create(Api.class);
        this.recyclerItemSettingList = recyclerItemSettingList;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_setting, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        String item_title = recyclerItemSettingList.get(position).getTitle();
        holder.title.setText(item_title);
        holder.layout_expandable.setVisibility(recyclerItemSettingList.get(position).isExpanded()? View.VISIBLE: View.GONE);
        holder.layout_inflatable.removeAllViews();

        // logout
        if(item_title.equals(context.getString(R.string.settings_logout))){
            holder.layout_inflatable.addView(View.inflate(context, R.layout.view_settings_logout, null));
            holder.ok.setText(context.getString(R.string.settings_logout));

        // change name
        }else if(item_title.equals(context.getString(R.string.settings_changeName))){
            holder.layout_inflatable.addView(View.inflate(context, R.layout.view_settings_changename, null));
            holder.ok.setText(context.getString(R.string.settings_save));

        // change pass
        }else if(item_title.equals(context.getString(R.string.settings_changePass))){
            holder.layout_inflatable.addView(View.inflate(context, R.layout.view_settings_changepass, null));
            holder.ok.setText(context.getString(R.string.settings_save));

        // change language
        }else if(item_title.equals(context.getString(R.string.settings_changeLang))) {
            holder.layout_inflatable.addView(View.inflate(context, R.layout.view_settings_changelang, null));
            holder.ok.setText(context.getString(R.string.settings_save));
            String current_lang = SharedPrefs.getLanguage(context);
            RadioGroup group = (RadioGroup) ((RelativeLayout) holder.layout_inflatable.getChildAt(0)).getChildAt(0);
            if (current_lang.equals(context.getString(R.string.lang_de)))
                group.check(R.id.setting_item_radio_de);
            else if (current_lang.equals(context.getString(R.string.lang_en)))
                group.check(R.id.setting_item_radio_en);
            else if(current_lang.equals(context.getString(R.string.lang_tr)))
                group.check(R.id.setting_item_radio_tr);

            // select questions
        }else if(item_title.equals(context.getString(R.string.settings_selectQuestions))){
            holder.layout_inflatable.addView(View.inflate(context, R.layout.view_settings_selectquestions, null));
            holder.ok.setText(context.getString(R.string.settings_save));
            addLessonsCheckBoxes((LinearLayout)holder.layout_inflatable.getChildAt(0));
        }
    }

    public void addLessonsCheckBoxes(LinearLayout parent){
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
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(lessons.get(i).getTitle());
                    checkBox.setTag(lessons.get(i).getId());
                    checkBox.setTypeface(ResourcesCompat.getFont(context, R.font.comfortaa));
                    parent.addView(checkBox);
                }
                Call<List<Activates>> call2 = api.getActivatesByUid(SharedPrefs.getUserid(context));
                call2.enqueue(new Callback<List<Activates>>() {
                    @Override
                    public void onResponse(Call<List<Activates>> call, Response<List<Activates>> response) {
                        if(!response.isSuccessful()){
                            Log.d("onResponse", "Code: "+response.code());
                            return;
                        }
                        List<Activates> activates = response.body();
                        for(int i=0; i<parent.getChildCount(); i++){
                            CheckBox box = (CheckBox)parent.getChildAt(i);
                            int l_id = (int)box.getTag();

                            boolean found = false;
                            for(Activates a: activates){
                                if(a.getL_id()==l_id) {
                                    box.setChecked(a.getYes() == 1 ? true : false);
                                    found = true;
                                }
                            }
                            if(!found)
                                box.setChecked(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Activates>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerItemSettingList.size();
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        Button ok;
        RelativeLayout layout_expandable;
        RelativeLayout layout_inflatable;

        public SettingViewHolder(@NonNull View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            ok = itemView.findViewById(R.id.item_ok);
            layout_expandable = itemView.findViewById(R.id.layout_expandable);
            layout_inflatable = itemView.findViewById(R.id.layout_inflatable);

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

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onOkClick(position, layout_inflatable);
                        }
                    }
                }
            });
        }
    }
}
