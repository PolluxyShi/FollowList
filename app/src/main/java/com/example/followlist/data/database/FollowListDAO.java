package com.example.followlist.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.followlist.data.model.FollowRelation;
import com.example.followlist.data.model.User;
import com.example.followlist.data.model.UserBean;

import java.util.ArrayList;
import java.util.List;

public class FollowListDAO {
    private final FollowListDatabaseHelper dbHelper;
    private final SQLiteDatabase writableDatabase;

    public static String TAG = "FollowListDAO";

    public FollowListDAO(Context context) {
        this.dbHelper = new FollowListDatabaseHelper(context);
        this.writableDatabase = dbHelper.getReadableDatabase();
    }

    public User getUserById(String userId) {
        // 1. 定义带占位符的SQL语句（? 为参数占位符，避免SQL注入）
        String sql = "SELECT * FROM " + dbHelper.USER_TABLE_NOTES
                + " WHERE " + dbHelper.COLUMN_USER_ID + " = ?";

        // 2. 定义查询参数（与占位符一一对应，字符串数组）
        String[] selectionArgs = {userId};

        // 3. 执行查询
        Cursor cursor = writableDatabase.rawQuery(sql, selectionArgs);

        // 4. 处理结果
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            // 通过字段名获取列索引（推荐，避免字段顺序变化导致错误）
            int userIdIndex = cursor.getColumnIndex(dbHelper.COLUMN_USER_ID);
            int nameIndex = cursor.getColumnIndex(dbHelper.COLUMN_NAME);
            int avatarNameIndex = cursor.getColumnIndex(dbHelper.COLUMN_AVATAR_NAME);

            // 根据索引获取对应值
            String id = cursor.getString(userIdIndex);
            String name = cursor.getString(nameIndex);
            String avatarName = cursor.getString(avatarNameIndex);
            // 打印或处理数据（实际场景可封装为对象、更新UI等）
            user = new User(id, name, avatarName);
            Log.d("QueryResult", "ID: " + id + ", Name: " + name + ", AvatarName: " + avatarName);
        } else {
            Log.d("QueryById", "未找到ID为" + userId + "的用户");
        }

        // 5. 关闭Cursor
        if (cursor != null) {
            cursor.close();
        }

        return user;
    }

    public List<UserBean> getUserBeanListByUser(User user) {
        List<UserBean> userBeanList = new ArrayList<>();
        String follower_id = user.getId();
        // 1. 定义SQL查询语句（查询所有字段）
        String sql = "SELECT * FROM " + dbHelper.USER_TABLE_NOTES + " u , " + dbHelper.FOLLOW_RELATION_TABLE_NOTES + " fr "
                + " WHERE " + "u." + dbHelper.COLUMN_USER_ID + " = " + "fr." + dbHelper.COLUMN_FOLLOWED_USER_ID
                + " AND " + "fr." + dbHelper.COLUMN_FOLLOWER_ID + " = ?"
                + " ORDER BY " + "fr." + dbHelper.COLUMN_FOLLOW_TIME + " DESC";

        // 2. 定义查询参数（与占位符一一对应，字符串数组）
        String[] selectionArgs = {follower_id};

        // 3. 使用writableDatabase执行查询（rawQuery返回Cursor结果集）
        Cursor cursor = writableDatabase.rawQuery(sql, selectionArgs); // 第二个参数为查询参数（无参数时传null）

        // 4. 处理查询结果
        if (cursor != null && cursor.moveToFirst()) { // 判断Cursor非空且有数据
            do {
                // 读取User对象
                // 通过字段名获取列索引（推荐，避免字段顺序变化导致错误）
                int userIdIndex = cursor.getColumnIndex("u." + dbHelper.COLUMN_USER_ID);
                int nameIndex = cursor.getColumnIndex("u." + dbHelper.COLUMN_NAME);
                int avatarNameIndex = cursor.getColumnIndex("u." + dbHelper.COLUMN_AVATAR_NAME);

                // 根据索引获取对应值
                String userId = cursor.getString(userIdIndex);
                String name = cursor.getString(nameIndex);
                String avatarName = cursor.getString(avatarNameIndex);

                User followedUser = new User(userId, name, avatarName);

                // 读取FollowRelation对象
                int followRelationIdIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_FOLLOW_RELATION_ID);
                int followerIdIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_FOLLOWER_ID);
                int followedUserIdIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_FOLLOWED_USER_ID);
                int isFollowIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_IS_FOLLOW);
                int isSpecialFollowIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_IS_SPECIAL_FOLLOW);
                int noteIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_NOTE);
                int followTimeIndex = cursor.getColumnIndex("fr." + dbHelper.COLUMN_FOLLOW_TIME);

                int followRelationId = cursor.getInt(followRelationIdIndex);
                String followerId = cursor.getString(followerIdIndex);
                String followedUserId = cursor.getString(followedUserIdIndex);
                int isFollow = cursor.getInt(isFollowIndex);
                int isSpecialFollow = cursor.getInt(isSpecialFollowIndex);
                String note = cursor.getString(noteIndex);
                String followTime = cursor.getString(followTimeIndex);

                FollowRelation followRelation = new FollowRelation(followRelationId, followerId, followedUserId, isFollow, isSpecialFollow, note, followTime);

                // 创建UserBean对象并加入列表
                UserBean userBean = new UserBean(followedUser, followRelation);
                userBeanList.add(userBean);

            } while (cursor.moveToNext()); // 循环遍历所有行
        }

        // 5. 关闭Cursor（必须关闭，避免内存泄漏）
        if (cursor != null) {
            cursor.close();
        }

        return userBeanList;
    }

    public void changeFollow(UserBean userBean) {
        userBean.setFollow(!userBean.isFollow());

        // 修改数据库
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_IS_FOLLOW, userBean.isFollow());
        values.put(dbHelper.COLUMN_IS_SPECIAL_FOLLOW, userBean.isSpecialFollow());
        values.put(dbHelper.COLUMN_FOLLOW_TIME, userBean.getFollowTime());
        writableDatabase.update(dbHelper.FOLLOW_RELATION_TABLE_NOTES, values, dbHelper.COLUMN_FOLLOW_RELATION_ID + " = ?", new String[]{String.valueOf(userBean.getFollowRelationId())});
    }

    public void setSpecialFollow(UserBean userBean, boolean isSpecialFollow) {
        userBean.setSpecialFollow(isSpecialFollow);

        // 修改数据库
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_IS_SPECIAL_FOLLOW, userBean.isSpecialFollow());
        writableDatabase.update(dbHelper.FOLLOW_RELATION_TABLE_NOTES, values, dbHelper.COLUMN_FOLLOW_RELATION_ID + " = ?", new String[]{String.valueOf(userBean.getFollowRelationId())});
    }

    public void setNote(UserBean userBean, String note) {
        userBean.setNote(note);

        // 修改数据库
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_NOTE, userBean.getNote());
        writableDatabase.update(dbHelper.FOLLOW_RELATION_TABLE_NOTES, values, dbHelper.COLUMN_FOLLOW_RELATION_ID + " = ?", new String[]{String.valueOf(userBean.getFollowRelationId())});
    }

    public void deleteFollowRelation(List<UserBean> userBeanList) {
        for (UserBean userBean : userBeanList) {
            if (!userBean.isFollow()) {
                writableDatabase.delete(dbHelper.FOLLOW_RELATION_TABLE_NOTES, dbHelper.COLUMN_FOLLOW_RELATION_ID + " = ?", new String[]{String.valueOf(userBean.getFollowRelationId())});
            }
        }
    }
}
