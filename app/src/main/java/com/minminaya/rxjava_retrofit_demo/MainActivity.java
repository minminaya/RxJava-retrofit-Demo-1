package com.minminaya.rxjava_retrofit_demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.minminaya.rxjava_retrofit_demo.fragment.CacheFragment;
import com.minminaya.rxjava_retrofit_demo.fragment.ElementaryFragment;
import com.minminaya.rxjava_retrofit_demo.fragment.MapFragment;
import com.minminaya.rxjava_retrofit_demo.fragment.ZipFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);


        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ElementaryFragment();
                    case 1:
                        return new MapFragment();
                    case 2:
                        return new ZipFragment();
                    case 3:
                        return new CacheFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "基本";
                    case 1:
                        return "转换(MAP)";
                    case 2:
                        return "压合(zip)";
                    case 3:
                        return "缓存(cache)";
                    default:
                        return "基本";
                }
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
