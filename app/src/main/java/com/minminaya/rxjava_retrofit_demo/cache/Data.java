package com.minminaya.rxjava_retrofit_demo.cache;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.minminaya.rxjava_retrofit_demo.App;
import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.model.Item;
import com.minminaya.rxjava_retrofit_demo.network.NetWork;
import com.minminaya.rxjava_retrofit_demo.util.GankBeautyResultToItemsMapper;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Niwa on 2017/4/11.
 */

public class Data {

    private static Data INSTANCE;
    private static final int DATA_SOURCE_MEMORY = 1;
    private static final int DATA_SOURCE_DISK = 2;
    private static final int DATA_SOURCE_NETWORK = 3;

    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK})
    @interface DataSource {
    }


    BehaviorSubject<List<Item>> cache;

    private int dataSource;

    public static Data getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Data();
        }
        return INSTANCE;
    }

    public Data() {
    }

    public void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return App.getINSTANCE().getString(dataSourceTextRes);
    }


    public void loadFromNetWork() {
        NetWork.getGankApi()
                .getBeauties(10, 1)
                .subscribeOn(Schedulers.io())
                .map(GankBeautyResultToItemsMapper.getInstance())
                .doOnNext(new Consumer<List<Item>>() {
                    @Override
                    public void accept(List<Item> items) throws Exception {
                        //存
                        Log.e("TAG", "加载网络这里");
                        Database.getINSTANCE().writeItems(items);

                    }
                })
                .subscribe(new Consumer<List<Item>>() {
                    @Override
                    public void accept(List<Item> items) throws Exception {
                        cache.onNext(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //处理错误
                        throwable.printStackTrace();
                    }
                });
    }

    //Rxjava2.0这里有点不一样了
    public void subscribeData(@Nullable Observer<List<Item>> observer) {
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new ObservableOnSubscribe<List<Item>>() {
                @Override
                public void subscribe(ObservableEmitter<List<Item>> e) throws Exception {
                    List<Item> items = Database.getINSTANCE().readItems();
                    if (items == null) {
                        Log.d("TAG", "subscribeData");
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadFromNetWork();
                    } else {
                        setDataSource(DATA_SOURCE_DISK);
                        e.onNext(items);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);
        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void clearMemoryCache(){
        cache = null;
    }

    public void clearMemoryAndDiskCache(){
        clearMemoryCache();
        Database.getINSTANCE().delete();
    }
}
