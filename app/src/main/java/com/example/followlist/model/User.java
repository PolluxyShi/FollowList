package com.example.followlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
    private String id;
    private String name;
    private String avatarName;
    public static String TAG = "User";

    public User() {}

    public User(String id, String name, String avatarName) {
        this.id = id;
        this.name = name;
        this.avatarName = avatarName;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        avatarName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(avatarName);
    }

    // Getter和Setter
    public String getId() { return id; }
//    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }

    public String getAvatarName() { return avatarName; }
//    public void setAvatarResId(String avatarName) { this.avatarName = avatarName; }
}
