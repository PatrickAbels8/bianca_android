package com.example.bianca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bianca.helper.Constants;
import com.example.bianca.prefs.SharedPrefs;
import com.example.bianca.restapi.Api;
import com.example.bianca.restapi.Client;
import com.example.bianca.restapi.models.User;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {
    private Context context;
    private Retrofit retrofit;
    private Api api;

    private TextInputLayout layout_name;
    private TextInputLayout layout_pass;
    private TextInputLayout layout_pass_repeat;
    private Button sign_in;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = getApplicationContext();
        retrofit = Client.getRetrofitInstance();
        api = retrofit.create(Api.class);

        layout_name = findViewById(R.id.register_name);
        layout_pass = findViewById(R.id.register_pass);
        layout_pass_repeat = findViewById(R.id.register_pass_repeat);
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = layout_name.getEditText().getText().toString();
                String pass = layout_pass.getEditText().getText().toString();
                String pass_repeat = layout_pass_repeat.getEditText().getText().toString();
                try_register(name, pass, pass_repeat);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void try_register(String name, String pass, String pass_repeat){
        final String _name = name;
        final String _pass = pass;
        final String _pass_repeat = pass_repeat;

        if(_name.isEmpty() || _pass.isEmpty() || _pass_repeat.isEmpty())
            return;
        if(!_pass.equals(_pass_repeat)){
            Toast.makeText(context, getString(R.string.error_login_wrongPassRepeat), Toast.LENGTH_SHORT).show();
            return;
        }
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
                    Toast.makeText(context, getString(R.string.error_register_alreadyExists), Toast.LENGTH_SHORT).show();
                }else{
                    Call<Integer> call_create = api.createUser(new User(_name, _pass));
                    call_create.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(!response.isSuccessful()){
                                Log.d("onResponse", "Code: "+response.code());
                                return;
                            }
                            Integer success = response.body();
                            if(success== Constants.SUCCESS)
                                register(_name);
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
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

    public void register(String name){
        SharedPrefs.setLoggedIn(context, true);
        SharedPrefs.setUsername(context, name);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(getString(R.string.bundle_name), name);
        startActivity(intent);
        finish();
    }

    public void login(){
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}