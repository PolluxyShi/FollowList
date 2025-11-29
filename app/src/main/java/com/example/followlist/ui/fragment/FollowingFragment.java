package com.example.followlist.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.followlist.R;
import com.example.followlist.data.model.UserBean;
import com.example.followlist.data.network.ApiResponse;
import com.example.followlist.data.network.MockFollowListApi;
import com.example.followlist.ui.dialog.MoreOptionDialog;
import com.example.followlist.ui.dialog.SetNoteDialog;
import com.example.followlist.ui.fragment.recyclerview.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<UserBean> mUserList;
    private UserAdapter mAdapter;
    private MockFollowListApi mApi;

    // 分页相关
    private int mCurrentPage = 1;
    private boolean mIsLoading = false;
    private boolean mHasMore = true;
    private LinearLayoutManager mLayoutManager;

    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static String TAG = "FollowingFragment";

    public static FollowingFragment newInstance(int position) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.following_page, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initViews();
        setupSwipeRefresh();
        setupRecyclerViewScrollListener();
        setupItemListeners();

        // 初始加载第一页数据
        loadMoreData(true);
    }

    private void initData() {
        mUserList = new ArrayList<>();
        mApi = new MockFollowListApi();
    }

    private void initViews() {
        assert getView() != null;
        mSwipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = getView().findViewById(R.id.recyclerview_id);

        mLayoutManager = new LinearLayoutManager(requireContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 性能优化设置
        mRecyclerView.setHasFixedSize(true); // 如果列表项高度固定，RecyclerView 会跳过 onMeasure() 的重新计算，可以提升性能
        mRecyclerView.setItemViewCacheSize(20); // 增加缓存数量，缓存刚刚滚出屏幕的 View，当用户反向滚动时可以直接复用

        mAdapter = new UserAdapter(mUserList);
        mRecyclerView.setAdapter(mAdapter);

        updateFollowCount(mApi.getTotal());
    }

    private void setupSwipeRefresh() {
        // 设置刷新时动画的颜色
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        // 设置下拉刷新的监听器
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            // 重置分页状态
            mCurrentPage = 1;
            mHasMore = true;
            loadMoreData(true);
        });
    }

    /**
     * 设置 RecyclerView 滚动监听，实现分页加载和头像预加载
     */
    private void setupRecyclerViewScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 预加载：当距离底部还有3个item时开始加载
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                // 如果还有更多数据且不在加载中，且接近底部
                if (!mIsLoading && mHasMore) {
                    if (firstVisibleItemPosition + visibleItemCount >= totalItemCount - 3) {
                        loadMoreData(false);
                    }
                }

                // 头像预加载：预加载即将显示的item的头像
                preloadAvatars(firstVisibleItemPosition, visibleItemCount);
            }
        });
    }

    /**
     * 预加载即将显示的头像
     * @param firstVisiblePosition 第一个可见位置
     * @param visibleCount 可见item数量
     */
    private void preloadAvatars(int firstVisiblePosition, int visibleCount) {
        // 预加载当前可见区域前后各5个item的头像
        int preloadStart = Math.max(0, firstVisiblePosition - 5);
        int preloadEnd = Math.min(mUserList.size(), firstVisiblePosition + visibleCount + 5);

        Context context = requireContext();
        for (int i = preloadStart; i < preloadEnd && i < mUserList.size(); i++) {
            UserBean userBean = mUserList.get(i);
            String avatarName = userBean.getAvatarName();
            int avatarResId = context.getResources().getIdentifier(
                    avatarName,
                    "drawable",
                    context.getPackageName()
            );

            if (avatarResId != 0) {
                // 使用 Glide 预加载图片到内存缓存
                Glide.with(context)
                        .load(avatarResId)
                        .preload(); // 预加载到内存缓存
            }
        }
    }

    /**
     * 加载更多数据
     * @param isRefresh 是否是刷新操作
     */
    private void loadMoreData(boolean isRefresh) {
        if (mIsLoading) {
            return;
        }

        mIsLoading = true;
        mAdapter.setLoading(true);

        // 如果是刷新，重置页码
        if (isRefresh) {
            mCurrentPage = 1;
        }

        // 异步加载数据
        mApi.getFollowingListAsync(mCurrentPage,
                new MockFollowListApi.ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        mMainHandler.post(() -> {
                            // 添加调试日志
                            Log.d(TAG, "加载成功，数据量: " +
                                    (response.getData() != null ? response.getData().size() : 0));
                            Log.d(TAG, "是否有更多数据: " + response.isHasMore());
                            Log.d(TAG, "总数量: " + response.getTotal());

                            mIsLoading = false;
                            mAdapter.setLoading(false);

                            if (isRefresh) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                // 在成功回调中一次性更新数据
                                mAdapter.setData(response.getData());
                            } else {
                                // 加载更多时追加数据
                                mAdapter.addData(response.getData());
                            }

                            mHasMore = response.isHasMore();
                            mAdapter.setHasMore(mHasMore);
                            mCurrentPage++;
                            updateFollowCount(response.getTotal());
                        });
                    }

                    @Override
                    public void onError(String error) {
                        mMainHandler.post(() -> {
                            mIsLoading = false;
                            mAdapter.setLoading(false);
                            if (isRefresh) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            Toast.makeText(requireContext(), "加载失败: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private void setupItemListeners() {
        mAdapter.setOnItemActionListener(new UserAdapter.OnItemActionListener() {
            // 关注/取消关注
            @Override
            public void onIsFollowClick(int position, UserBean userBean) {
                // 更新数据
                mApi.setFollow(userBean);
                // 更新UI
                mAdapter.notifyItemChanged(position);
            }

            // 打开更多
            @Override
            public void onMoreClick(int position, UserBean userBean) {
                if (!userBean.isFollow()) {
                    // 未关注状态下无法弹出MoreOptionDialog
                    Toast.makeText(requireContext(), "已取关，无法使用", Toast.LENGTH_SHORT).show();
                } else {
                    MoreOptionDialog moreOptionDialog = new MoreOptionDialog(
                            requireContext(),
                            userBean,
                            new MoreOptionDialog.OnOptionChangeListener() {
                                // 设置特别关注
                                @Override
                                public void onSpecialFollowChanged(UserBean userBean, boolean isSpecialFollow) {
                                    // 更新数据
                                    mApi.setSpecialFollow(userBean, isSpecialFollow);
                                    // 如果需要更新UI
                                    mAdapter.notifyItemChanged(position);
                                }

                                // 设置备注
                                @Override
                                public void onSetNoteClicked(UserBean userBean) {
                                    // 弹出编辑备注的对话框
                                    SetNoteDialog dialog = SetNoteDialog.newInstance(userBean);
                                    dialog.setOnNoteSetListener(new SetNoteDialog.OnNoteSetListener() {
                                        @Override
                                        public void onNoteSet(UserBean userBean, String note) {
                                            // 更新数据
                                            mApi.setNote(userBean, note);
                                            // 更新UI
                                            mAdapter.notifyItemChanged(position);
                                        }
                                    });
                                    dialog.show(getParentFragmentManager(), "SetNoteDialog");
                                }

                                // 取消关注
                                @Override
                                public void onCancelFollowClicked(UserBean userBean) {
                                    // 更新数据
                                    mApi.setFollow(userBean);
                                    // 更新UI
                                    mAdapter.notifyItemChanged(position);
                                }
                            });
                    moreOptionDialog.show();
                }
            }
        });
    }

    private void updateFollowCount(int cnt) {
        assert getView() != null;
        TextView tv_followNum = getView().findViewById(R.id.tv_followNum);
        if (tv_followNum != null) {
            tv_followNum.setText("我的关注（" + cnt + "人）");
        }
    }
}
