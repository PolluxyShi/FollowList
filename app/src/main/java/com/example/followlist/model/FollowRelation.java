package com.example.followlist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FollowRelation implements Parcelable {
    private int id;                    // 关系ID
    private String followerId;         // 关注者ID（当前用户）
    private String followedUserId;     // 被关注用户ID
    private int isFollow;          // 是否关注
    private int isSpecialFollow;   // 是否特别关注
    private String note;               // 备注名
    private String followTime;           // 关注时间戳

    public FollowRelation(int id, String followerId, String followedUserId, int isFollow, int isSpecialFollow, String note, String followTime) {
        this.id = id;
        this.followerId = followerId;
        this.followedUserId = followedUserId;
        this.isFollow = isFollow;
        this.isSpecialFollow = isSpecialFollow;
        this.note = note;
        this.followTime = followTime;
    }

    protected FollowRelation(Parcel in) {
        id = in.readInt();
        followerId = in.readString();
        followedUserId = in.readString();
        isFollow = in.readInt();
        isSpecialFollow = in.readInt();
        note = in.readString();
        followTime = in.readString();
    }

    public static final Creator<FollowRelation> CREATOR = new Creator<FollowRelation>() {
        @Override
        public FollowRelation createFromParcel(Parcel in) {
            return new FollowRelation(in);
        }

        @Override
        public FollowRelation[] newArray(int size) {
            return new FollowRelation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(followerId);
        dest.writeString(followedUserId);
        dest.writeInt(isFollow);
        dest.writeInt(isSpecialFollow);
        dest.writeString(note);
        dest.writeString(followTime);
    }

    // Getter和Setter
    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }

    public String getFollowerId() { return followerId; }
//    public void setFollowerId(int followerId) { this.followerId = followerId; }

    public String getFollowedUserId() { return followedUserId; }
//    public void setFollowedUserId(int followedUserId) { this.followedUserId = followedUserId; }

    public boolean isFollow() { return isFollow == 1; }
    public void setFollow(boolean follow) { isFollow = follow ? 1 : 0; }

    public boolean isSpecialFollow() { return isSpecialFollow == 1; }
    public void setSpecialFollow(boolean specialFollow) { isSpecialFollow = specialFollow ? 1 : 0; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getFollowTime() { return followTime; }
    public void setFollowTime(String followTime) { this.followTime = followTime; }
}
