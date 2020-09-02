package com.agnext.qualixfarmer.network;

import com.agnext.qualixfarmer.base.Constant;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ApiClient {

//    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(2, TimeUnit.MINUTES)
//            .readTimeout(2, TimeUnit.MINUTES)
//            .writeTimeout(2, TimeUnit.MINUTES)
//            .build();

    public static Retrofit retrofit = null;
    private static Retrofit scmRetrofit = null;
    private static Retrofit oauthRetrofit = null;

    private static final String BASE_URL = "http://23.98.216.140";
    private static GsonConverterFactory gsonFactory = null;
    private static OkHttpClient okHttpClient = null;

    private static GsonConverterFactory getGsonFactory() {
        if (gsonFactory == null) {
            gsonFactory = GsonConverterFactory.create();
        }
        return gsonFactory;
    }

    private static OkHttpClient getOkhttpClient() {




        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor(msg -> Timber.tag("OkHttp").d(msg))
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addNetworkInterceptor(new RequestInterceptor())
                    .build();
        }
        return okHttpClient;
    }

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BaseURl)
                .addConverterFactory(getGsonFactory())
                .client(getOkhttpClient())
                .build();

        return retrofit;
    }

    public static Retrofit getScmClient() {
        if (scmRetrofit == null) {
            scmRetrofit = new Retrofit.Builder()
                    .baseUrl(String.format("%s:8085", BASE_URL))
                    .addConverterFactory(getGsonFactory())
                    .client(getOkhttpClient())
                    .build();
        }

        return scmRetrofit;
    }

    public static Retrofit getQauthClient() {
        if (oauthRetrofit == null) {
            oauthRetrofit = new Retrofit.Builder()
                    .baseUrl(String.format("%s:8071", BASE_URL))
                    .addConverterFactory(getGsonFactory())
                    .client(getOkhttpClient())
                    .build();
        }

        return oauthRetrofit;
    }
}
