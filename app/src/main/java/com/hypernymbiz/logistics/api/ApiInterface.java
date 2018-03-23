package com.hypernymbiz.logistics.api;

import com.hypernymbiz.logistics.model.User;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.MyApplication;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Metis on 22-Mar-18.
 */

public interface ApiInterface {

    String HTTP = "http://159.65.7.152/";
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiInterface.HTTP)
            .addConverterFactory(GsonConverterFactory.create())
            .client(MyOkHttpClient.getHttpClient());
    ApiInterface retrofit = builder.build().create(ApiInterface.class);

    @POST("api/users/login/")
    Call<WebAPIResponse<User>> loginUser(@Body HashMap<String, Object> body);


    class MyOkHttpClient {

        public static OkHttpClient getHttpClient() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.readTimeout(30, TimeUnit.SECONDS);
            clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
            if (true) {
                clientBuilder.addInterceptor(logging);
            }
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Cache-Control", "no-cache")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
//                    String token = "Token a1f32065fe1f5bdaf7f3075d22fccbe469a6b498";
                    String token = LoginUtils.getUserToken(MyApplication.getAppContext());
                    if (token != null) {
                        requestBuilder.header("Authorization", "Token "+token);
                    }
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }


            });

            return clientBuilder.build();
        }




    }

}
