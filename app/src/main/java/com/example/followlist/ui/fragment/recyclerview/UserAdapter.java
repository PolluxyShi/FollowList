package com.example.followlist.ui.fragment.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.followlist.R;
import com.example.followlist.data.model.UserBean;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<UserBean> mUserList;
    private OnItemActionListener mListener;

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

    /**
     * 第一步：Item创建ViewHolder（加载item布局）
     *
     * @return
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_user, parent, false);
        //创建返回的ViewHolder
        return new UserViewHolder(itemRootView);
    }

    /**
     * // 第二步：绑定数据到ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        //绑定数据
        //当前位置的数据
        UserBean userBean = mUserList.get(position);
        // 调用ViewHolder的bindData方法绑定数据
        holder.bindData(userBean);

        // 设置监听
        holder.clUser.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "已选中 " + holder.tvName.getText(), Toast.LENGTH_SHORT).show();
        });

        holder.cvIsFollow.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onIsFollowClick(position, userBean);
            }
        });

        holder.ivMore.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onMoreClick(position, userBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList != null ? mUserList.size() : 0;
    }
}

