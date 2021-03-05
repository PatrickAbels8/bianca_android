package com.example.bianca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.bianca.helper.Helpers;
import com.example.bianca.prefs.SharedPrefs;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Retrofit retrofit;
    private Api api;
    private Bundle bundle;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        // init db
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);

        // init user
        Intent intent = getIntent();
        String u_name = intent.getStringExtra(getString(R.string.bundle_name));
        getU_id(u_name);

        // init settings (language, question selection)
        Helpers.setLocale(getBaseContext(), SharedPrefs.getLanguage(context));

        // init bottom nav
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_main);
        bundle = new Bundle();

        // launch swiper frag
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MainFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        /*case R.id.nav_about:
                            selectedFragment = new AboutFragment();
                            break;*/
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_main:
                            selectedFragment = new MainFragment();
                            break;
                        case R.id.nav_training:
                            selectedFragment = new TrainingFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    /**
     * api
     */
    public void getU_id(String name){
        Call<User> call = api.getUserByName(name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }
                User user = response.body();
                if(user.getId()>0)
                    SharedPrefs.setUserid(context, user.getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}