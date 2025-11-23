package com.example.followlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FollowListDatabaseHelper extends SQLiteOpenHelper {

    // 数据库文件名
    public static final String DATABASE_NAME = "followlist.db";
    // 数据库版本号
    private static final int DATABASE_VERSION = 1;

    // User表
    // 表名
    public static final String USER_TABLE_NOTES = "users";
    // 列名
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AVATAR_NAME = "avatar_name";

    // 创建表的 SQL 语句
    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE_NOTES + " (" +
                    COLUMN_USER_ID + " VARCHAR(16) PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_AVATAR_NAME + " TEXT NOT NULL" +
                    ");";

    // FollowRelation表
    // 表名
    public static final String FOLLOW_RELATION_TABLE_NOTES = "follow_relations";
    // 列名
    public static final String COLUMN_FOLLOW_RELATION_ID = "follow_relation_id";
    public static final String COLUMN_FOLLOWER_ID = "follower_id";
    public static final String COLUMN_FOLLOWED_USER_ID = "followed_user_id";
    public static final String COLUMN_IS_FOLLOW = "is_follow";
    public static final String COLUMN_IS_SPECIAL_FOLLOW = "is_special_follow";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_FOLLOW_TIME = "follow_time";

    // 创建表的 SQL 语句
    private static final String FOLLOW_RELATION_TABLE_CREATE =
            "CREATE TABLE " + FOLLOW_RELATION_TABLE_NOTES + " (" +
                    COLUMN_FOLLOW_RELATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FOLLOWER_ID + " VARCHAR(16), " +
                    COLUMN_FOLLOWED_USER_ID + " VARCHAR(16), " +
                    COLUMN_IS_FOLLOW + " INTEGER, " +
                    COLUMN_IS_SPECIAL_FOLLOW + " INTEGER, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_FOLLOW_TIME + " TEXT NOT NULL" +
                    ");";
    private Context context;
    public FollowListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 首次创建数据库时调用，执行建表语句
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(FOLLOW_RELATION_TABLE_CREATE);

        // 插入用户数据
        insertUserData(db);
        // 插入关注关系数据
        insertFollowRelationData(db);
    }

    /**
     * 插入用户数据
     */
    private void insertUserData(SQLiteDatabase db) {
        String[][] userData = {
                {"1042689609", "西岚", "avatar_1"},
                {"93300807617", "孤独的登", "avatar_2"},
                {"71751220238", "小美省钱路线", "avatar_3"},
                {"dyprzb66l20d", "Vladimir_Lobov钢琴", "avatar_4"},
                {"610521233", "蓝战非", "avatar_5"},
                {"73875880163", "九", "avatar_6"},
                {"caomiyirenta", "糙米薏仁汤女士", "avatar_7"},
                {"soul68235", "海海呀（ASMR助眠解压 努力版）", "avatar_8"},
                {"YaeMiko_627", "八重神子", "avatar_9"},
                {"yuanshen_mihoyo", "原神", "avatar_10"},
                {"Aizhiji3139", "集宁~古云轩老北京布鞋店的霞姐", "avatar_11"},
                {"66013371590", "枸杞泡黄芪", "avatar_12"},
                {"1950495951", "程十安an", "avatar_13"},
                {"1285519547", "时空之子", "avatar_14"}
        };

        for (String[] user : userData) {
            db.execSQL("INSERT INTO " + USER_TABLE_NOTES + " (" +
                    COLUMN_USER_ID + ", " + COLUMN_NAME + ", " + COLUMN_AVATAR_NAME +
                    ") VALUES (?, ?, ?)", user);
        }
    }


    /**
     * 插入关注关系数据
     */
    private void insertFollowRelationData(SQLiteDatabase db) {
        String[][] followRelationData = {
                {"1", "1042689609", "1285519547", "1", "1", "李子禧", "2025-02-28 10:30:21"},
                {"2", "1042689609", "1950495951", "1", "0", "", "2023-08-27 22:08:33"},
                {"3", "1042689609", "66013371590", "1", "0", "", "2025-04-17 15:19:32"},
                {"4", "1042689609", "Aizhiji3139", "1", "0", "", "2022-01-18 09:45:37"},
                {"5", "1042689609", "yuanshen_mihoyo", "1", "0", "", "2025-07-04 08:44:43"},
                {"6", "1042689609", "YaeMiko_627", "1", "0", "", "2024-06-15 03:39:44"},
                {"7", "1042689609", "soul68235", "1", "0", "", "2024-03-29 12:01:27"},
                {"8", "1042689609", "caomiyirenta", "1", "1", "", "2022-08-08 04:16:49"},
                {"9", "1042689609", "73875880163", "1", "0", "", "2021-10-05 14:23:01"},
                {"10", "1042689609", "610521233", "1", "0", "", "2022-04-30 20:15:48"},
                {"11", "1042689609", "dyprzb66l20d", "1", "0", "", "2023-10-10 13:47:05"},
                {"12", "1042689609", "71751220238", "1", "0", "", "2023-12-31 23:59:00"},
                {"13", "caomiyirenta", "93300807617", "1", "0", "", "2023-02-14 11:11:11"},
                {"14", "caomiyirenta", "1042689609", "1", "1", "", "2024-05-20 06:50:10"},
                {"15", "caomiyirenta", "1285519547", "1", "0", "", "2022-11-08 05:30:59"},
                {"16", "caomiyirenta", "1950495951", "1", "0", "", "2021-11-11 17:32:08"},
                {"17", "1285519547", "66013371590", "1", "0", "", "2025-09-11 21:25:54"},
                {"18", "1285519547", "Aizhiji3139", "1", "0", "", "2024-09-08 19:54:50"},
                {"19", "1285519547", "yuanshen_mihoyo", "1", "1", "", "2022-07-22 16:55:12"},
                {"20", "1285519547", "YaeMiko_627", "1", "0", "", "2024-12-25 00:05:15"},
                {"21", "1285519547", "soul68235", "1", "0", "", "2023-05-01 18:40:22"},
                {"22", "1285519547", "caomiyirenta", "1", "0", "", "2025-01-01 00:00:01"},
                {"23", "1285519547", "73875880163", "1", "0", "", "2024-01-03 07:22:16"},
                {"24", "1285519547", "610521233", "1", "1", "", "2025-11-15 14:28:07"}
        };

        for (String[] relation : followRelationData) {
            db.execSQL("INSERT INTO " + FOLLOW_RELATION_TABLE_NOTES + " (" +
                    COLUMN_FOLLOW_RELATION_ID + ", " + COLUMN_FOLLOWER_ID + ", " +
                    COLUMN_FOLLOWED_USER_ID + ", " + COLUMN_IS_FOLLOW + ", " +
                    COLUMN_IS_SPECIAL_FOLLOW + ", " + COLUMN_NOTE + ", " +
                    COLUMN_FOLLOW_TIME + ") VALUES (?, ?, ?, ?, ?, ?, ?)", relation);
        }
    }


    /**
     * - 数据库升级：当应用更新，数据库版本号增加时，自动调用 onUpgrade() 方法，让你可以在此执行数据迁移、修改表结构等操作。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本升级时调用
        Log.w(FollowListDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        // 简单的升级策略：删除旧表，创建新表（会丢失数据）
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + FOLLOW_RELATION_TABLE_NOTES);
        onCreate(db);
    }

    /**
     * 数据库降级处理
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 方式1：删除旧表，重新创建低版本表结构（数据会丢失，适合开发）
        // 注意：需先删除所有表（按实际表名修改）
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + FOLLOW_RELATION_TABLE_NOTES);
        // 调用onCreate创建低版本表结构
        onCreate(db);
        // 最后必须调用setVersion，将数据库版本更新为新版本（避免重复触发降级）
        db.setVersion(newVersion);
    }

}
