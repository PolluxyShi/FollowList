package com.example.followlist.fragment;

import android.os.Bundle;
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

import com.example.followlist.R;
import com.example.followlist.database.FollowListDAO;
import com.example.followlist.model.User;
import com.example.followlist.model.UserBean;
import com.example.followlist.dialog.MoreOptionDialog;
import com.example.followlist.dialog.SetNoteDialog;
import com.example.followlist.fragment.recyclerview.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private User mCurrentUser;
    private List<UserBean> mUserList;
    private FollowListDAO mFollowListDAO;
    private UserAdapter mAdapter;

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

        // 设置监听器
        mAdapter.setOnItemActionListener(new UserAdapter.OnItemActionListener() {
            @Override
            public void onIsFollowClick(int position, UserBean userBean) {
                // 更新对象及数据库
                mFollowListDAO.changeFollow(userBean);
                // 更新UI
                mAdapter.notifyItemChanged(position);
            }

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
                                @Override
                                public void onSpecialFollowChanged(UserBean userBean, boolean isSpecialFollow) {
                                    // 更新对象及数据库
                                    mFollowListDAO.setSpecialFollow(userBean, isSpecialFollow);
                                    // 如果需要更新UI
                                    mAdapter.notifyItemChanged(position);
                                }

                                @Override
                                public void onSetNoteClicked(UserBean userBean) {
                                    // 弹出编辑备注的对话框
                                    SetNoteDialog dialog = SetNoteDialog.newInstance(userBean);
                                    dialog.setOnNoteSetListener(new SetNoteDialog.OnNoteSetListener() {
                                        @Override
                                        public void onNoteSet(UserBean userBean, String note) {
                                            // 更新对象及数据库
                                            mFollowListDAO.setNote(userBean, note);
                                            // 更新UI
                                            mAdapter.notifyItemChanged(position);
                                        }
                                    });
                                    dialog.show(getParentFragmentManager(), "SetNoteDialog");
                                }

                                @Override
                                public void onCancelFollowClicked(UserBean userBean) {
                                    // 更新对象及数据库
                                    mFollowListDAO.changeFollow(userBean);
                                    // 更新UI
                                    mAdapter.notifyItemChanged(position);
                                }
                            });
                    moreOptionDialog.show();
                }
            }
        });
    }

    private void initData() {
        mUserList = new ArrayList<>();
        mFollowListDAO = new FollowListDAO(requireContext());
        createUserList();
    }

    private void initViews() {
        mSwipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = getView().findViewById(R.id.recyclerview_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new UserAdapter(mUserList);
        mRecyclerView.setAdapter(mAdapter);

        updateFollowCount();
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                updateUserList();
            }
        });
    }

    private void updateUserList() {
        // 删除数据库中的非关注条目
        mFollowListDAO.deleteFollowRelation(mUserList);
        // 清空UserBaan列表
        mUserList.clear();
        // 重新加载UserBaan列表
        mUserList.addAll(mFollowListDAO.getUserBeanListByUser(mCurrentUser));
        // 更新关注人数显示
        updateFollowCount();
        // 通知适配器数据已更新
        mAdapter.notifyDataSetChanged();
        // 停止刷新动画
        mSwipeRefreshLayout.setRefreshing(false);
//        // 可选：显示刷新完成的提示
//        Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
    }

    private void updateFollowCount() {
        TextView tv_followNum = getView().findViewById(R.id.tv_followNum);
        if (tv_followNum != null) {
            tv_followNum.setText("我的关注（" + mAdapter.getItemCount() + "人）");
        }
    }

    private void createUserList() {
        // 默认 id=1042689609 的用户为当前用户
        mCurrentUser = mFollowListDAO.getUserById("1042689609");
        mUserList = mFollowListDAO.getUserBeanListByUser(mCurrentUser);
    }
}
