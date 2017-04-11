package com.minminaya.rxjava_retrofit_demo.network;

import com.minminaya.rxjava_retrofit_demo.network.api.GankApi;
import com.minminaya.rxjava_retrofit_demo.network.api.ZhuangbiApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NIWA on 2017/4/7.
 */

public class NetWork {
    private static ZhuangbiApi zhuangbiApi;
    private static GankApi gankApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    public static ZhuangbiApi getZhuangbiApi(){
      if(zhuangbiApi == null){
          Retrofit retrofit = new Retrofit.Builder()
                  .client(okHttpClient)
                  .baseUrl("http://www.zhuangbi.info/")
                  .addConverterFactory(gsonConverterFactory)
                  .addCallAdapterFactory(rxJavaCallAdapterFactory)
                  .build();
          zhuangbiApi = retrofit.create(ZhuangbiApi.class);
      }
        return zhuangbiApi;
    }

    public static GankApi getGankApi(){
        if(gankApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }
}
