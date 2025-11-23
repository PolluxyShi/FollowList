package com.example.followlist.fragment.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.followlist.R;
import com.example.followlist.model.UserBean;

public class UserViewHolder extends RecyclerView.ViewHolder {
    ImageView ivAvatar;
    TextView tvName;
    CardView cvIsFollow;
    TextView tvIsFollow;
    CardView cvIsSpecialFollow;
    ConstraintLayout clUser;
    ImageView ivMore;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        // 绑定视图ID
        ivAvatar = itemView.findViewById(R.id.iv_avatar);
        tvName = itemView.findViewById(R.id.tv_name);
        cvIsFollow = itemView.findViewById(R.id.cv_isFollow);
        cvIsSpecialFollow = itemView.findViewById(R.id.cv_isSpecialFollow);
        tvIsFollow = cvIsFollow.findViewById(R.id.tv_isFollow);
        clUser = itemView.findViewById(R.id.cl_user);
        ivMore = itemView.findViewById(R.id.iv_more);
    }

    // 绑定数据到视图
    public void bindData(UserBean userBean) {
        // 设置头像（实际开发中推荐用Glide/Picasso加载网络图片）
        Context context = itemView.getContext();
        int avatarResId = context.getResources().getIdentifier(
                userBean.getAvatarName(),
                "drawable",
                context.getPackageName()
        );
        ivAvatar.setImageResource(avatarResId);
        // 设置用户名称
        tvName.setText(userBean.getDisplayName());
        // 设置关注状态
        setFollowUI(userBean.isFollow());
        // 设置特别关注
        setSpecialFollowUI(userBean.isSpecialFollow());
    }

    private void setFollowUI(boolean isFollow) {
        if (isFollow){
            tvIsFollow.setText("已关注");
            tvIsFollow.setTextColor(Color.parseColor("#000000"));
            cvIsFollow.setCardBackgroundColor(Color.parseColor("#E8E8E8"));
        } else {
            tvIsFollow.setText("关注");
            tvIsFollow.setTextColor(Color.parseColor("#FFFFFF"));
            cvIsFollow.setCardBackgroundColor(Color.parseColor("#FF0000"));
        }
    }

    private void setSpecialFollowUI(boolean isSpecialFollow){
        if (isSpecialFollow){
            cvIsSpecialFollow.setVisibility(View.VISIBLE);
        } else {
            cvIsSpecialFollow.setVisibility(View.GONE);
        }
    }
}
