package com.example.followlist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.followlist.ui.viewpager.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);

        // 设置适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 设置默认页面为第2个页面（索引为1）
        viewPager.setCurrentItem(1, false);

        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("互关");
                            break;
                        case 1:
                            tab.setText("关注");
                            break;
                        case 2:
                            tab.setText("粉丝");
                            break;
                        case 3:
                            tab.setText("朋友");
                            break;
                    }
                }
        );
        tabLayoutMediator.attach();
    }
}