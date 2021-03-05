package com.example.bianca.restapi;

import android.graphics.Bitmap;

import com.example.bianca.restapi.models.Activates;
import com.example.bianca.restapi.models.Answers;
import com.example.bianca.restapi.models.Covers;
import com.example.bianca.restapi.models.Examines;
import com.example.bianca.restapi.models.Lesson;
import com.example.bianca.restapi.models.Question;
import com.example.bianca.restapi.models.Topic;
import com.example.bianca.restapi.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {
    // user
    @GET("api/user/read.php")
    Call<List<User>> getUsers();
    @POST("api/user/create.php")
    Call<Integer> createUser(@Body User user);
    @GET("api/user/read_by_name.php")
    Call<User> getUserByName(@Query("name") String name);
    @GET("api/user/read_by_name_pass.php")
    Call<User> getUserByNamePass(@Query("name") String name, @Query("pass") String pass);
    @POST("api/user/update_pass.php")
    Call<Integer> updateUser(@Body User user);
    @POST("api/user/update_elo.php")
    Call<Integer> updateUserElo(@Body User user);

    // lesson
    @GET("api/lesson/read.php")
    Call<List<Lesson>> getLessons();

    // topic
    @GET("api/topic/read.php")
    Call<List<Topic>> getTopics();

    // question
    @GET("api/question/read.php")
    Call<List<Question>> getQuestions();
    @GET("api/question/read_by_lid.php")
    Call<List<Question>> getQuestionsByLid(@Query("l_id") int l_id);

    // answers
    @GET("api/answers/read.php")
    Call<List<Answers>> getAnswers();
    @POST("api/answers/create.php")
    Call<Integer> createAnswers(@Body Answers answers);

    // examines
    @GET("api/examines/read.php")
    Call<List<Examines>> getExamines();

    // covers
    @GET("api/covers/read.php")
    Call<List<Covers>> getCovers();

    // activates
    @GET("api/activates/read_by_uid.php")
    Call<List<Activates>> getActivatesByUid(@Query("u_id") int u_id);
    @POST("api/activates/replace.php")
    Call<Integer> replaceActivates(@Body Activates activates);
}
