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
import android.widget.RadioButton;
import android.widget.Toast;

import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.adapter.ZhuangbiListAdapter;
import com.minminaya.rxjava_retrofit_demo.model.ZhuanbiImage;
import com.minminaya.rxjava_retrofit_demo.network.NetWork;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by NIWA on 2017/4/7.
 */
public class ElementaryFragment extends BaseFragment {


    @BindView(R.id.griRv)
    RecyclerView mGriRv;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    Unbinder unbinder;

    ZhuangbiListAdapter mAdapter = new ZhuangbiListAdapter();
    Observer<List<ZhuanbiImage>> mObserver = new Observer<List<ZhuanbiImage>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<ZhuanbiImage> value) {
            //发射数据
            //完成后把刷新条去除
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.setImages(value);
        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton button, boolean checked){
        if(checked){
            dispose();
            mAdapter.setImages(null);
            mSwipeRefreshLayout.setRefreshing(true);
            seach(button.getText().toString());
        }
    }

    private void seach(String s) {
        // TODO: 2017/4/7 mDisposable = ???
      NetWork.getZhuangbiApi()
                .search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        unbinder = ButterKnife.bind(this, view);

        mGriRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGriRv.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
