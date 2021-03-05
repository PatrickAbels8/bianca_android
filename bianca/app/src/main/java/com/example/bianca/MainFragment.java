package com.example.bianca;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bianca.helper.Constants;
import com.example.bianca.main.QuestionAdapter;
import com.example.bianca.main.QuestionItem;
import com.example.bianca.prefs.SharedPrefs;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.Activates;
import com.example.bianca.restapi.models.Answers;
import com.example.bianca.restapi.models.Lesson;
import com.example.bianca.restapi.models.Question;
import com.example.bianca.restapi.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainFragment extends Fragment implements CardStackListener {
    private Context context;

    private Retrofit retrofit;
    private Api api;

    private CardStackView stack;
    private FloatingActionButton like;
    private FloatingActionButton rewind;
    private FloatingActionButton skip;
    private TextView score;

    private CardStackLayoutManager manager;
    private QuestionAdapter adapter;
    private List<QuestionItem> questionItems;

    private TextView item_name;
    private TextView item_score;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        context = getContext();

        // init api
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);

        // init coms
        stack = v.findViewById(R.id.card_stack_view);
        like = v.findViewById(R.id.like_button);
        rewind = v.findViewById(R.id.rewind_button);
        skip = v.findViewById(R.id.skip_button);
        score = v.findViewById(R.id.score);
        item_name = v.findViewById(R.id.item_name);
        item_score = v.findViewById(R.id.item_score);

        // init user stats
        item_name.setText(SharedPrefs.getUsername(context));
        readScore(SharedPrefs.getUsername(context));

        // init card stack
        manager = new CardStackLayoutManager(context, this);
        stack.setLayoutManager(manager);
        questionItems = new ArrayList<QuestionItem>(Arrays.asList(
                new QuestionItem(0, "testimage.jpg", 1, 1)
        ));
        adapter = new QuestionAdapter(context, questionItems);
        stack.setAdapter(adapter);

        addQuestions();

        // init buttons
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                stack.swipe();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                stack.swipe();
            }
        });
        /*rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Top)
                        .setDuration(Duration.Normal.duration)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                stack.rewind();
            }
        });*/

        return v;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
    }

    @Override
    public void onCardSwiped(Direction direction) {
        boolean answer = direction==Direction.Right;
        boolean correct = questionItems.get(manager.getTopPosition()-1).getAnswer()==1;
        int points = questionItems.get(manager.getTopPosition()-1).getPoints();

        addAnswered(questionItems.get(manager.getTopPosition()-1).getId(), answer==correct, points);
    }

    @Override
    public void onCardRewound() {
    }

    @Override
    public void onCardCanceled() {
    }

    @Override
    public void onCardAppeared(View view, int position) {
    }

    @Override
    public void onCardDisappeared(View view, int position) {
    }

    public void shuffleQuestions(){
        Collections.shuffle(questionItems);
        adapter.notifyDataSetChanged();
    }

    public void startAnimation(boolean correct, int points){
        String s = "";
        if(correct) {
            score.setTextColor(getResources().getColor(R.color.good));
            s += "+ ";
        }else{
            score.setTextColor(getResources().getColor(R.color.bad));
            s += "- ";
        }
        s += points;
        score.setText(s);
        score.setVisibility(View.VISIBLE);
        score.setAlpha(1f);
        float x = score.getX();
        float y = score.getY();
        score.animate()
                .setDuration(2000)
                .rotationYBy(360f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        score.animate()
                                .setDuration(1000)
                                .translationYBy(-1000)
                                .translationXBy(300)
                                .alpha(0f)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        score.setX(x);
                                        score.setY(y);
                                        item_score.setText(Integer.toString(Integer.parseInt(item_score.getText().toString())+(correct?1:-1)*points));
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }

    /**
     * api
     */

    public void readScore(String username){
        Call<User> call = api.getUserByName(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                User user = response.body();
                Log.d("user", user.toString());
                item_score.setText(Integer.toString(user.getElo()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void addAnswered(int q_id, boolean correct, int points){
        Call<Integer> call = api.createAnswers(new Answers(q_id, SharedPrefs.getUserid(context), correct?1:0));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                Integer success = response.body();
                if(success== Constants.SUCCESS) {
                    Call<Integer> call_update = api.updateUserElo(new User(SharedPrefs.getUserid(context), points*(correct?1:-1)));
                    call_update.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(!response.isSuccessful()){
                                Log.d("onResponse", "Code: "+response.code());
                                return;
                            }
                            Integer success = response.body();
                            if(success== Constants.SUCCESS)
                                startAnimation(correct, points);
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void addQuestions(){
        questionItems.clear();
        Call<List<Lesson>> call = api.getLessons();
        call.enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                List<Lesson> lessons = response.body();
                for(final Lesson l: lessons){
                    Call<List<Activates>> call2 = api.getActivatesByUid(SharedPrefs.getUserid(context));
                    call2.enqueue(new Callback<List<Activates>>() {
                        @Override
                        public void onResponse(Call<List<Activates>> call, Response<List<Activates>> response) {
                            if(!response.isSuccessful()){
                                Log.d("onResponse", "Code: "+response.code());
                                return;
                            }
                            List<Activates> activates = response.body();
                            int l_id = l.getId();
                            boolean found = false;
                            for(Activates a: activates){
                                if(a.getL_id()==l_id) {
                                    found = true;
                                    if(a.getYes() == 1){
                                       Call<List<Question>> call3 = api.getQuestionsByLid(l_id);
                                       call3.enqueue(new Callback<List<Question>>() {
                                           @Override
                                           public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                                               if(!response.isSuccessful()){
                                                   Log.d("onResponse", "Code: "+response.code());
                                                   return;
                                               }
                                               List<Question> questions = response.body();
                                               for(int i=0; i<questions.size(); i++){
                                                   int id = questions.get(i).getId();
                                                   String image = questions.get(i).getImage();
                                                   int answer = questions.get(i).getAnswer();
                                                   int points = questions.get(i).getPoints();
                                                   QuestionItem q_item = new QuestionItem(id, image, answer, points);
                                                   questionItems.add(q_item);
                                                   adapter.notifyDataSetChanged();
                                               }
                                               shuffleQuestions();
                                           }

                                           @Override
                                           public void onFailure(Call<List<Question>> call, Throwable t) {
                                               t.printStackTrace();
                                           }
                                       });
                                   }
                                }
                            }
                            if(!found){
                                Call<List<Question>> call4 = api.getQuestionsByLid(l_id);
                                call4.enqueue(new Callback<List<Question>>() {
                                    @Override
                                    public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                                        if(!response.isSuccessful()){
                                            Log.d("onResponse", "Code: "+response.code());
                                            return;
                                        }
                                        List<Question> questions = response.body();
                                        for(int i=0; i<questions.size(); i++){
                                            int id = questions.get(i).getId();
                                            String image = questions.get(i).getImage();
                                            int answer = questions.get(i).getAnswer();
                                            int points = questions.get(i).getPoints();
                                            QuestionItem q_item = new QuestionItem(id, image, answer, points);
                                            questionItems.add(q_item);
                                            adapter.notifyDataSetChanged();
                                        }
                                        shuffleQuestions();
                                    }

                                    @Override
                                    public void onFailure(Call<List<Question>> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Activates>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
