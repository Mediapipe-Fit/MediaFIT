package com.gauravk.bubblebarsample.DB.Userdata;

import com.gauravk.bubblebarsample.DB.Userdata.user.dataall;
import com.gauravk.bubblebarsample.DB.Userdata.user.post_response;
import com.gauravk.bubblebarsample.DB.Userdata.user.user;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {


    @GET("user")
    Call<dataall> get_All_data();
    /*
    //유저 목록 가져오는 함수
    retrofitAPI.get_All_data().enqueue(new Callback<dataall>(){
        @Override
        public void onResponse(@NonNull Call<dataall> call, @NonNull Response<dataall> response){
            if(response.isSuccessful()) {
                dataall datalist = response.body();
                for(user temp: datalist.getData()){
                    data.add(temp.getEmail());
                }
                adapter.notifyDataSetChanged();
                Log.d("TEST", "성공성공)");
            }
        }
        @Override
        public void onFailure(Call<dataall> call, Throwable t){
            Log.d("TEST" , "실패실패");
            t.printStackTrace();
        }
    });
    */

    //사용불가
    @FormUrlEncoded
    @POST("/user")
    Call<user> postData(
            @Field("Email") String email,
            @Field("name") String name,
            @Field("sex") int sex,
            @Field("age") String age,
            @Field("Score") int score
    );

    @FormUrlEncoded
    @POST("/user")
    Call<user> Datapost(@FieldMap HashMap<String,Object> param);


    @POST("/user")
    Call<post_response> dp(@Body user user);
    /*
    보내는 함수
    postuser에 다 넣고 보내면 댐

    user postuser = new user();
        postuser.setEmail("it is work?");
        postuser.setName("fwadl");
        postuser.setScore(50);
        postuser.setSex(1);
        postuser.setAge("50~60");


        retrofitAPI.dp(postuser).enqueue(new Callback<post_response>() {
            @Override
            public void onResponse(Call<post_response> call, Response<post_response> response){
     //           Log.d("test",response.message());
                if(response.isSuccessful()){
                    Log.d("test","섹스!");
                }
                else{
                    Log.d("test","ㅈㅈ");
                }
            }
            public void onFailure(Call<post_response> call, Throwable t){
                Log.d("test","개짜증");
            }
        });
     */

}


