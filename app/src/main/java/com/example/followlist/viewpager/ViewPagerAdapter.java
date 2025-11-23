package com.example.followlist.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.followlist.fragment.BlankFragment;
import com.example.followlist.fragment.FollowingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int PAGE_COUNT = 4; // 4个页面

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return BlankFragment.newInstance(position, "暂无互关");
            case 1:
                return FollowingFragment.newInstance(position);
            case 2:
                return BlankFragment.newInstance(position, "暂无粉丝");
            case 3:
                return BlankFragment.newInstance(position, "暂无朋友");
            default:
                return BlankFragment.newInstance(position, "暂无内容");
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}