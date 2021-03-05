package com.example.bianca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bianca.helper.Helpers;
import com.example.bianca.prefs.SharedPrefs;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.User;
import com.google.android.material.textfield.TextInputLayout;

import javax.xml.namespace.QName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity  {
    private Context context;
    private Retrofit retrofit;
    private Api api;

    private RelativeLayout layout;
    private TextInputLayout layout_name;
    private TextInputLayout layout_pass;
    private Button sign_in;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);

        layout = findViewById(R.id.layout);
        layout_name = findViewById(R.id.login_name);
        layout_pass = findViewById(R.id.login_pass);
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);

        if(SharedPrefs.getLoggedIn(context)){
            String name = SharedPrefs.getUsername(context);
            if(name.length()>0){
                login(name);
            }else{
                Toast.makeText(context, getString(R.string.error_default), Toast.LENGTH_SHORT).show();
            }
        }else{
            layout.setVisibility(View.VISIBLE);
        }

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = layout_name.getEditText().getText().toString();
                String pass = layout_pass.getEditText().getText().toString();
                try_login(name, pass);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void try_login(String name, String pass){
        final String _name = name;
        final String _pass = pass;

        if(_name.isEmpty() || _pass.isEmpty())
            return;
        Call<User> call_namepass = api.getUserByNamePass(_name, _pass);
        call_namepass.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.d("onResponse", "Code: "+response.code());
                    return;
                }

                User user = response.body();
                if(user.getName()!=null && user.getPass()!=null){
                    login(_name);
                }else{
                    Call<User> call_name = api.getUserByName(_name);
                    call_name.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(!response.isSuccessful()){
                                Log.d("onResponse", "Code: "+response.code());
                                return;
                            }
                            User user = response.body();
                            if(user.getName()!=null && user.getPass()!=null){
                                Toast.makeText(LoginActivity.this, getString(R.string.error_login_wrongPass), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, getString(R.string.error_login_wrongName), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void login(String name) {
        SharedPrefs.setLoggedIn(context, true);
        SharedPrefs.setUsername(context, name);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(getString(R.string.bundle_name), name);
        startActivity(intent);
        finish();
    }

    public void register(){
        Intent intent = new Intent(context, RegisterActivity.class);
        startActivity(intent);
    }
}