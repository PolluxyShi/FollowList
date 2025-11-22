package com.example.followlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.followlist.R;
import com.example.followlist.model.UserBean;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MoreOptionDialog extends BottomSheetDialog {
    UserBean userBean;
    Switch swSpecialFollow;
    ImageView ivSetNote;
    TextView tvUserName;

    private OnOptionChangeListener listener;

    public interface OnOptionChangeListener {
        void onSpecialFollowChanged(UserBean userBean, boolean isSpecialFollow);
        void onSetNoteClicked(UserBean userBean);
//        void onCancelFollowClicked(UserBean userBean);
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
    }

    public void bindData(UserBean userBean){
        tvUserName.setText(userBean.getDisplayName());
        swSpecialFollow.setChecked(userBean.isSpecialFollow());
    }
}
