package com.minminaya.rxjava_retrofit_demo.network.api;

import com.minminaya.rxjava_retrofit_demo.model.GankBeautyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Niwa on 2017/4/9.
 */

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
