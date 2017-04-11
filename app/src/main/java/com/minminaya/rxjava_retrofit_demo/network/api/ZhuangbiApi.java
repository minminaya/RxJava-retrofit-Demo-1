package com.minminaya.rxjava_retrofit_demo.network.api;

import com.minminaya.rxjava_retrofit_demo.model.ZhuanbiImage;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by NIWA on 2017/4/7.
 */

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ZhuanbiImage>> search(@Query("q") String query);
}
