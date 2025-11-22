package com.example.followlist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String name;
    private String avatarName;

    public User() {}

    public User(int id, String name, String avatarName) {
        this.id = id;
        this.name = name;
        this.avatarName = avatarName;
    }

    protected User(Parcel in) {
        id = in.readInt();
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(avatarName);
    }

    // Getter和Setter
    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }

    public String getAvatarName() { return avatarName; }
//    public void setAvatarResId(String avatarName) { this.avatarName = avatarName; }
}
