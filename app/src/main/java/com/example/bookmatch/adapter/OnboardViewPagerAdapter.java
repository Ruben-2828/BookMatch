package com.example.bookmatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bookmatch.databinding.FragmentOnboardScreenBinding;
import com.example.bookmatch.model.OnboardItem;

import java.util.List;


public class OnboardViewPagerAdapter extends PagerAdapter {

    Context mContext ;
    List<OnboardItem> mListScreen;

    public OnboardViewPagerAdapter(Context mContext, List<OnboardItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        FragmentOnboardScreenBinding binding = FragmentOnboardScreenBinding.inflate(LayoutInflater.from(mContext), container, false);

        binding.onboardTitle.setText(mListScreen.get(position).getTitle());
        binding.onboardDescription.setText(mListScreen.get(position).getDescription());
        binding.onboardImg.setImageResource(mListScreen.get(position).getScreenImg());

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        FragmentOnboardScreenBinding binding = FragmentOnboardScreenBinding.bind((View) object);
        container.removeView(binding.getRoot());
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

}
