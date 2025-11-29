package com.example.followlist.ui.fragment.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.followlist.R;
import com.example.followlist.data.model.UserBean;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_USER = 0;
    private static final int TYPE_LOADING = 1;
    
    private List<UserBean> mUserList;
    private OnItemActionListener mListener;
    private boolean isLoading = false;
    private boolean hasMore = true;

    public interface OnItemActionListener {
        void onIsFollowClick(int position, UserBean userBean);
        void onMoreClick(int position, UserBean userBean);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.mListener = listener;
    }

    // 构造方法：接收数据集
    public UserAdapter(List<UserBean> userList) {
        this.mUserList = userList;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一项是加载状态
        if (position == mUserList.size()) {
            return TYPE_LOADING;
        }
        return TYPE_USER;
    }

    /**
     * 第一步：Item创建ViewHolder（加载item布局）
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View loadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(loadingView);
        } else {
            View itemRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_user, parent, false);
            return new UserViewHolder(itemRootView);
        }
    }

    /**
     * 第二步：绑定数据到ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
            loadingHolder.bindData(hasMore);
        } else if (holder instanceof UserViewHolder) {
            UserViewHolder userHolder = (UserViewHolder) holder;
            // 当前位置的数据
            UserBean userBean = mUserList.get(position);
            // 调用ViewHolder的bindData方法绑定数据
            userHolder.bindData(userBean);

            // 设置监听
            userHolder.clUser.setOnClickListener(v -> {
                Toast.makeText(userHolder.itemView.getContext(), "已选中 " + userHolder.tvName.getText(), Toast.LENGTH_SHORT).show();
            });

            userHolder.cvIsFollow.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onIsFollowClick(position, userBean);
                }
            });

            userHolder.ivMore.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onMoreClick(position, userBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = mUserList != null ? mUserList.size() : 0;
        // 如果有加载状态，增加一项
        if (isLoading && hasMore) {
            count += 1;
        }
        return count;
    }

    /**
     * 添加新数据（用于分页加载）
     */
    public void addData(List<UserBean> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = mUserList.size();
            mUserList.addAll(newData);
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }

    /**
     * 设置加载状态
     */
    public void setLoading(boolean loading) {
        if (isLoading != loading) {
            isLoading = loading;
            if (loading) {
                notifyItemInserted(mUserList.size());
            } else {
                notifyItemRemoved(mUserList.size());
            }
        }
    }

    /**
     * 设置是否还有更多数据
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        if (!hasMore && isLoading) {
            setLoading(false);
        }
    }

    /**
     * 设置数据
     */
    public void setData(List<UserBean> data) {
        mUserList.clear();
        if (data != null && !data.isEmpty()) {
            mUserList.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 加载状态 ViewHolder
     */
    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView tvLoading;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvLoading = itemView.findViewById(R.id.tv_loading);
        }

        void bindData(boolean hasMore) {
            if (hasMore) {
                progressBar.setVisibility(View.VISIBLE);
                tvLoading.setText("加载中...");
            } else {
                progressBar.setVisibility(View.GONE);
                tvLoading.setText("没有更多了");
            }
        }
    }
}

