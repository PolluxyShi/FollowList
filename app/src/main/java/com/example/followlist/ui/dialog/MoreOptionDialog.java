package com.example.followlist.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.followlist.R;
import com.example.followlist.data.model.UserBean;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MoreOptionDialog extends BottomSheetDialog {
    UserBean userBean;
    Switch swSpecialFollow;
    ImageView ivSetNote;
    TextView tvUserName;
    TextView tvUserId;
    TextView tvUserOriName;
    View vDivider;

    LinearLayout llCancelFollow;

    public static String TAG = "MoreOptionDialog";

    private OnOptionChangeListener listener;

    public interface OnOptionChangeListener {
        void onSpecialFollowChanged(UserBean userBean, boolean isSpecialFollow);
        void onSetNoteClicked(UserBean userBean);
        void onCancelFollowClicked(UserBean userBean);
    }

    public MoreOptionDialog(@NonNull Context context, UserBean userBean, OnOptionChangeListener listener) {
        super(context);
        this.userBean = userBean;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除默认标题栏（必须在setContentView前调用）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载布局
        setContentView(R.layout.bottom_sheet_user_options);
        // 初始化控件
        initView();
        // 设置点击事件
        initEvent();
        // 绑定数据
        bindData(userBean);
    }

    private void initView(){
        swSpecialFollow = findViewById(R.id.switch_special_follow);
        ivSetNote = findViewById(R.id.iv_set_note);
        tvUserName = findViewById(R.id.tv_bottom_sheet_user_name);
        tvUserId = findViewById(R.id.tv_user_id);
        tvUserOriName = findViewById(R.id.tv_user_origin_name);
        vDivider = findViewById(R.id.divider);
        llCancelFollow = findViewById(R.id.ll_cancel_follow);
    }

    private void initEvent(){
        // 特别关注开关
        swSpecialFollow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onSpecialFollowChanged(userBean, isChecked);
            }
        });

        // 设置备注
        ivSetNote.setOnClickListener(v -> {
            dismiss();

            if (listener != null) {
                listener.onSetNoteClicked(userBean);
            }
        });

        // 取消关注
        llCancelFollow.setOnClickListener(v -> {
            dismiss();

            if (listener != null) {
                listener.onCancelFollowClicked(userBean);
            }
        });

    }

    public void bindData(UserBean userBean){
        tvUserName.setText(userBean.getDisplayName());
        if (userBean.hasNote()) {
            tvUserOriName.setText("名字: " + userBean.getUserName());
            tvUserOriName.setVisibility(View.VISIBLE);
            vDivider.setVisibility(View.VISIBLE);
        } else {
            tvUserOriName.setVisibility(View.GONE);
            vDivider.setVisibility(View.GONE);
        }
        tvUserId.setText("抖音号: " + userBean.getUserId());
        swSpecialFollow.setChecked(userBean.isSpecialFollow());
    }
}
