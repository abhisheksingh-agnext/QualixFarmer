package com.agnext.qualixfarmer.network;

import android.content.Context;
import android.util.Log;

import com.agnext.qualixfarmer.base.Constant;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

//    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(2, TimeUnit.MINUTES)
//            .readTimeout(2, TimeUnit.MINUTES)
//            .writeTimeout(2, TimeUnit.MINUTES)
//            .build();

    public static Retrofit retrofit = null;
    private static Retrofit scmRetrofit = null;
    private static Retrofit oauthRetrofit = null;
    private static Retrofit vmsRetrofit = null;
    private static Retrofit dcmRetrofit = null;



    private static final String BASE_URL = "http://23.98.216.140";
    private static GsonConverterFactory gsonFactory = null;
    private static OkHttpClient okHttpClient = null;

    private static GsonConverterFactory getGsonFactory() {
        if (gsonFactory == null) {
            gsonFactory = GsonConverterFactory.create();
        }
        return gsonFactory;
    }

    private static OkHttpClient getOkhttpClient(Context... context) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor(msg -> Log.d("OkHttp", msg))
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addNetworkInterceptor(new RequestInterceptor())
                    .cache(null);

            if (context.length != 0) {
                Context ctx = context[0];
                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ctx));

                builder.cookieJar(cookieJar);
            }

            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://23.98.216.140:5679")
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

    public static Retrofit getQauthClient(Context context) {
        if (oauthRetrofit == null) {
            oauthRetrofit = new Retrofit.Builder()
                    .baseUrl(String.format("%s:8071", BASE_URL))
                    .addConverterFactory(getGsonFactory())
                    .client(getOkhttpClient(context))
                    .build();
        }
        return oauthRetrofit;
    }

    public static Retrofit getDCMClient(Context context) {
        if (dcmRetrofit == null) {
            dcmRetrofit = new Retrofit.Builder()
                    .baseUrl(String.format("%s:8072", BASE_URL))
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(getGsonFactory())
                    .client(getOkhttpClient(context))
                    .build();
        }
        return dcmRetrofit;
    }
    public static Retrofit getVMSClient(Context context) {
        if (vmsRetrofit == null) {
            vmsRetrofit = new Retrofit.Builder()
                    .baseUrl(String.format("%s:8074", BASE_URL))
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(getGsonFactory())
                    .client(getOkhttpClient(context))
                    .build();
        }
        return vmsRetrofit;
    }
}
