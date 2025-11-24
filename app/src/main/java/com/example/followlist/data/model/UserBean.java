package com.example.followlist.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserBean implements Parcelable {
    private User user;                  // 用户基本信息
    private FollowRelation followRelation; // 关注关系信息

    public static String TAG = "UserBean";

    public UserBean(User user, FollowRelation followRelation) {
        this.user = user;
        this.followRelation = followRelation;
    }

    protected UserBean(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        followRelation = in.readParcelable(FollowRelation.class.getClassLoader());
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeParcelable(followRelation, flags);
    }

    // 便捷方法 - 用于列表显示
    public String getAvatarName() {
        return user.getAvatarName();
    }

    public String getUserId() { return user.getId(); }
    public String getUserName() { return  user.getName(); }
    public String getNote() {
        return followRelation.getNote();
    }

    public void setNote(String note) {
        if (Objects.equals(note, user.getName())) {
            followRelation.setNote("");
        } else {
            followRelation.setNote(note);
        }
    }

    public boolean hasNote() {
        String note = followRelation.getNote();
        return  note != null && !note.isEmpty();
    }

    public String getDisplayName() {
        return followRelation.getNote() != null && !followRelation.getNote().isEmpty()
                ? followRelation.getNote()
                : user.getName();
    }

    public boolean isFollow() {
        return followRelation.isFollow();
    }

    public void setFollow(boolean isFollow) {
        if (isFollow != isFollow()){
            if (isFollow) {
                updateFollowTime();
            } else {
                setSpecialFollow(false);
            }
        }
        followRelation.setFollow(isFollow);
    }

    public boolean isSpecialFollow() {
        return followRelation.isSpecialFollow();
    }

    public void setSpecialFollow(boolean isSpecialFollow) {
        followRelation.setSpecialFollow(isSpecialFollow);
    }

    public int getFollowRelationId() {
        return followRelation.getId();
    }

    public String getFollowTime() {
        return followRelation.getFollowTime();
    }

    private void updateFollowTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String followTime = LocalDateTime.now().format(formatter);
        followRelation.setFollowTime(followTime);
    }
}
