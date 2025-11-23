// SetNoteDialog.java
package com.example.followlist.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.followlist.R;
import com.example.followlist.model.UserBean;

public class SetNoteDialog extends DialogFragment {
    private UserBean mUserBean;
    private OnNoteSetListener mListener;
    private TextView mOriName;
    private EditText mEditTextNote;
    private Button mBtnCancel, mBtnConfirm;

    public static String TAG = "SetNoteDialog";

    public interface OnNoteSetListener {
        void onNoteSet(UserBean userBean, String note);
    }

    public static SetNoteDialog newInstance(UserBean userBean) {
        SetNoteDialog dialog = new SetNoteDialog();
        Bundle args = new Bundle();
        args.putParcelable("userBean", userBean);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteSetListener) {
            mListener = (OnNoteSetListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mUserBean = getArguments().getParcelable("userBean");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_set_note, null);

        initViews(view);
        setupClickListeners();
        builder.setView(view);

        return builder.create();
    }

    private void initViews(View view) {
        mOriName = view.findViewById(R.id.tv_ori_name);
        mEditTextNote = view.findViewById(R.id.et_note);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);

        String displayName = mUserBean.getDisplayName();
        mEditTextNote.setText(displayName);
        mEditTextNote.setSelection(displayName.length());

        // 判断是否显示原名
        if (mUserBean != null && mUserBean.hasNote()) {
            mOriName.setText("名字: " + mUserBean.getUserName());
            mOriName.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        // 取消按钮点击事件
        mBtnCancel.setOnClickListener(v -> {
            dismiss(); // 关闭对话框
        });

        // 确认按钮点击事件
        mBtnConfirm.setOnClickListener(v -> {
            setNoteForUser();
            dismiss(); // 关闭对话框
        });
    }

    private void setNoteForUser() {
        String note = mEditTextNote.getText().toString().trim();
        if (mListener != null && mUserBean != null) {
            mListener.onNoteSet(mUserBean, note);
        }
    }

    public void setOnNoteSetListener(OnNoteSetListener listener) {
        mListener = listener;
    }
}