package com.minminaya.rxjava_retrofit_demo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Niwa on 2017/4/9.
 */

public class GankBeautyResult {
    public boolean error;
    //服务器获取到的整个json数据变为一个序列，可以用MAP来过滤一些我们用不上的数据
    public
    @SerializedName("results")
    List<GankBeauty> beauties;
}