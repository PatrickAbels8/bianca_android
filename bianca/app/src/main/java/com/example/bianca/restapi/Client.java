package com.example.bianca.restapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bianca.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static String API_BASE_URL = "http://192.168.178.20:80/bianca/";//"2001:16b8:2100:58d7:de39:6fff:fe63:f8a2:80";//"http://192.168.178.54:80/bianca/"; //"+BuildConfig.SERVER+"/bianca/";
    private static String API_QUESTIONS_URL_APPENDIX = "static/questions/";
    private static String API_THUMBNAILS_URL_APPENDIX = "static/thumbnails/";
    private static Retrofit retrofit;
    private static Gson gson;

    public static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static String getApiBaseUrl(){
        return API_BASE_URL;
    }

    public static String getApiQuestionsUrlAppendix(){
        return API_QUESTIONS_URL_APPENDIX;
    }

    public static String getApiThumbnailsUrlAppendix(){
        return API_THUMBNAILS_URL_APPENDIX;
    }
}
