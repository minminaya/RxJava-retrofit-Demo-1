package com.minminaya.rxjava_retrofit_demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.adapter.ItemListAdapter;
import com.minminaya.rxjava_retrofit_demo.cache.Data;
import com.minminaya.rxjava_retrofit_demo.model.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Niwa on 2017/4/11.
 */

public class CacheFragment extends BaseFragment {


    @BindView(R.id.loadingTimeTv)
    AppCompatTextView loadingTimeTv;
    @BindView(R.id.loadBt)
    AppCompatButton loadBt;
    @BindView(R.id.tipbt)
    Button tipbt;
    @BindView(R.id.cacheRv)
    RecyclerView cacheRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.clearMemoryCacheBt)
    AppCompatButton clearMemoryCacheBt;
    @BindView(R.id.clearMemoryAndDiskCacheBt)
    AppCompatButton clearMemoryAndDiskCacheBt;
    Unbinder unbinder;
    private long startingTime;
    private ItemListAdapter itemListAdapter = new ItemListAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        unbinder = ButterKnife.bind(this, view);
        cacheRv.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        cacheRv.setAdapter(itemListAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.RED);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_cache;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.loadBt, R.id.clearMemoryCacheBt, R.id.clearMemoryAndDiskCacheBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loadBt:
                swipeRefreshLayout.setRefreshing(true);
                startingTime = System.currentTimeMillis();
                dispose();
                Data.getINSTANCE()
                        .subscribeData(new Observer<List<Item>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<Item> value) {
                                swipeRefreshLayout.setRefreshing(false);
                                int loadingTime = (int) (System.currentTimeMillis() - startingTime);
                                loadingTimeTv.setText(getString(R.string.loading_time_and_source, loadingTime, Data.getINSTANCE().getDataSourceText()));
                                itemListAdapter.setImages(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.clearMemoryCacheBt:
                Data.getINSTANCE().clearMemoryCache();
                itemListAdapter.setImages(null);
                Toast.makeText(getActivity(), "内存缓存已经清空", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clearMemoryAndDiskCacheBt:
                Data.getINSTANCE().clearMemoryAndDiskCache();
                itemListAdapter.setImages(null);
                Toast.makeText(getActivity(), "内存缓存和磁盘缓存都已经清空", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
