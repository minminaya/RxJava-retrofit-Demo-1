package com.minminaya.rxjava_retrofit_demo.util;

import com.minminaya.rxjava_retrofit_demo.model.GankBeauty;
import com.minminaya.rxjava_retrofit_demo.model.GankBeautyResult;
import com.minminaya.rxjava_retrofit_demo.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * 这里的接口Function就是以前的Func1
 * <p>
 * 接口方法apply()就是以前的call()
 * Created by Niwa on 2017/4/9.
 */

public class GankBeautyResultToItemsMapper implements Function<GankBeautyResult, List<Item>> {

    private static GankBeautyResultToItemsMapper INSTANCE = new GankBeautyResultToItemsMapper();
    public static GankBeautyResultToItemsMapper getInstance (){
        return INSTANCE;
    }

    public GankBeautyResultToItemsMapper() {
    }

    @Override
    public List<Item> apply(GankBeautyResult gankBeautyResult) throws Exception {

        //拿到序列results
        List<GankBeauty> gankBeauties = gankBeautyResult.beauties;

        //要返回的数据
        List<Item> items = new ArrayList<>(gankBeauties.size());

        //初始化要输入以及要输出的时间格式
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for (GankBeauty gankBeauty :
                gankBeauties) {
            Item item = new Item();
            try {
                Date date = inputFormat.parse(gankBeauty.createdAt);
                item.description = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.description = "未知时间";
            }
            item.imageUrl = gankBeauty.url;
            item.type = gankBeauty.type;
            items.add(item);
        }
        return items;
    }

}
