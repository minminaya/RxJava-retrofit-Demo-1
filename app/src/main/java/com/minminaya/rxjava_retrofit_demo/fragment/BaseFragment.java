package com.minminaya.rxjava_retrofit_demo.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.minminaya.rxjava_retrofit_demo.R;


import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/** 抽取相同的属性
 * Created by NIWA on 2017/4/7.
 */

public abstract class BaseFragment extends Fragment {



    //Rxjava1里以前叫做Subscription，用来取消订阅
    protected Disposable mDisposable;

    @OnClick(R.id.tipbt)
    void tip(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
    }

    protected void dispose() {
        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();


}
