package com.minminaya.rxjava_retrofit_demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.adapter.ItemListAdapter;
import com.minminaya.rxjava_retrofit_demo.model.Item;
import com.minminaya.rxjava_retrofit_demo.network.NetWork;
import com.minminaya.rxjava_retrofit_demo.util.GankBeautyResultToItemsMapper;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Niwa on 2017/4/9.
 */

public class MapFragment extends BaseFragment {
    private int page = 0;

    @BindView(R.id.pageTv)
    TextView pageTv;
    @BindView(R.id.previousPageBt)
    AppCompatButton previousPageBt;
    @BindView(R.id.nextPageBt)
    AppCompatButton nextPageBt;
    @BindView(R.id.tipbt)
    Button tipbt;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;


    ItemListAdapter itemListAdapter = new ItemListAdapter();


    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<Item> value) {
            swipeRefreshLayout.setRefreshing(false);
            pageTv.setText(getString(R.string.page_with_number,page));
            itemListAdapter.setImages(value);
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(itemListAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.previousPageBt, R.id.nextPageBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previousPageBt:
                loadPage(--page);
                if(page == 1){
                    //如果到1了，把按钮变无效,避免引起空指针异常，下面同理
                    previousPageBt.setEnabled(false);
                }
                break;
            case R.id.nextPageBt:
                loadPage(++page);
                if(page == 2){
                    previousPageBt.setEnabled(true);
                }
                break;
        }
    }

    private void loadPage(int page) {
        swipeRefreshLayout.setRefreshing(true);
        dispose();
        NetWork.getGankApi()
                .getBeauties(15,page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
