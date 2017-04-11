package com.minminaya.rxjava_retrofit_demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.adapter.ItemListAdapter;
import com.minminaya.rxjava_retrofit_demo.model.Item;
import com.minminaya.rxjava_retrofit_demo.model.ZhuanbiImage;
import com.minminaya.rxjava_retrofit_demo.network.NetWork;
import com.minminaya.rxjava_retrofit_demo.util.GankBeautyResultToItemsMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Niwa on 2017/4/10.
 */

public class ZipFragment extends BaseFragment {


    @BindView(R.id.zipLoadBt)
    Button zipLoadBt;
    @BindView(R.id.tipbt)
    Button tipbt;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;


    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<Item> value) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(value);
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "数据加载出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {

        }
    };

    ItemListAdapter adapter = new ItemListAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        unbinder = ButterKnife.bind(this, view);
        gridRv.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_zip;

    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.zipLoadBt)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.zipLoadBt:
                swipeRefreshLayout.setRefreshing(true);
                dispose();
                Observable.zip(
                        NetWork.getGankApi().getBeauties(200, 1).map(GankBeautyResultToItemsMapper.getInstance()),
                        NetWork.getZhuangbiApi().search("110"),
                        new BiFunction<List<Item>, List<ZhuanbiImage>, List<Item>>() {
                            @Override
                            public List<Item> apply(List<Item> gankItems, List<ZhuanbiImage> zhuanbiImages) throws Exception {

                                List<Item> items = new ArrayList<Item>();
                                for (int i = 0; i < gankItems.size() / 2 && i < zhuanbiImages.size(); i++) {
                                    //gankItems处理过程，加入俩张
                                    items.add(gankItems.get(i * 2));
                                    items.add(gankItems.get(i * 2 + 1));
                                    //zhuangbiItem取一张
                                    Item zhuangbiItem = new Item();
                                    ZhuanbiImage zhuanbiItem = zhuanbiImages.get(i);
                                    zhuangbiItem.description = zhuanbiItem.description;
                                    zhuangbiItem.imageUrl = zhuanbiItem.image_url;
                                    items.add(zhuangbiItem);
                                }
                                return items;
                            }
                        }
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
                break;
        }

    }
}
