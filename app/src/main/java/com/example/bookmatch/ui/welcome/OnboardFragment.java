package com.example.bookmatch.ui.welcome;

import static android.content.Context.MODE_PRIVATE;

import static com.example.bookmatch.utils.Constants.KEY_ONBOARD_OPENED;
import static com.example.bookmatch.utils.Constants.SHARED_PREF_NAME;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.OnboardViewPagerAdapter;
import com.example.bookmatch.databinding.FragmentOnboardBinding;
import com.example.bookmatch.model.OnboardItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardFragment extends Fragment {

    private FragmentOnboardBinding binding;
    private Animation btnAnim;
    private int position = 0;

    public OnboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.button_animation);

        final List<OnboardItem> mList = new ArrayList<>();

        String[] onboardString = getResources().getStringArray(R.array.onboard_string_array);
        String[] onboardDesc = getResources().getStringArray(R.array.onboard_desc_array);
        int[] imageIds = {R.drawable.onboard_first, R.drawable.onboard_second, R.drawable.onboard_third, R.drawable.onboard_fourth};

        for (int i = 0; i < onboardString.length; i++) {
            mList.add(new OnboardItem(onboardString[i], onboardDesc[i] , imageIds[i]));
        }

        OnboardViewPagerAdapter onboardViewPagerAdapter = new OnboardViewPagerAdapter(requireContext(), mList);
        binding.screenViewpager.setAdapter(onboardViewPagerAdapter);
        binding.tabIndicator.setupWithViewPager(binding.screenViewpager);


        binding.btnNext.setOnClickListener(v -> {
            position = binding.screenViewpager.getCurrentItem();
            if (position < mList.size()) {
                position++;
                binding.screenViewpager.setCurrentItem(position);
            }

            if (position == (mList.size() - 1)) {
                loadLastScreen();
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            position = binding.screenViewpager.getCurrentItem();
            if (position > 0) {
                position--;
                binding.screenViewpager.setCurrentItem(position);
            }
        });

        binding.tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.btnGetStarted.setOnClickListener(v -> {
            savePrefData();
            Navigation.findNavController(view).navigate(R.id.action_onboardFragment_to_loginFragment);

        });


        if (isOnboardOpened()) {
            Navigation.findNavController(view).navigate(R.id.action_onboardFragment_to_loginFragment);
        }


        binding.tvSkip.setOnClickListener(v -> binding.screenViewpager.setCurrentItem(mList.size()));

    }

    @SuppressLint("ClickableViewAccessibility")
    private void loadLastScreen() {
        binding.btnBack.setVisibility(View.INVISIBLE);
        binding.btnNext.setVisibility(View.INVISIBLE);
        binding.btnGetStarted.setVisibility(View.VISIBLE);
        binding.tvSkip.setVisibility(View.INVISIBLE);
        binding.tabIndicator.setVisibility(View.INVISIBLE);
        binding.btnGetStarted.setAnimation(btnAnim);

        binding.screenViewpager.setOnTouchListener((v, event) -> true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void savePrefData() {
        SharedPreferences pref = requireContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_ONBOARD_OPENED, true);
        editor.apply();
    }

    private boolean isOnboardOpened() {
        SharedPreferences pref = requireContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return pref.getBoolean(KEY_ONBOARD_OPENED, false);
    }

}