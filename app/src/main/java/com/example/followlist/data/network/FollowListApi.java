package com.example.followlist.data.network;

import com.example.followlist.data.model.UserBean;

/**
 * 关注列表 API 接口
 * 定义网络请求接口
 */
public interface FollowListApi {
    /**
     * 获取关注列表（分页）
     * @param page 页码（从1开始）
     * @return API响应
     */
    ApiResponse getFollowingList(int page);

    /**
     * 获取关注列表总数据量
     * * @return 总数据量
     */
    int getTotal();

    /**
     * 改变关注状态
     * @param userBean 关注/取关的目标
     */
    void setFollow(UserBean userBean);

    /**
     * 设置特别关注
     * @param userBean 设置特别关注的目标
     * @param isSpecialFollow 设置/取消设置
     */
    void setSpecialFollow(UserBean userBean, boolean isSpecialFollow);

    /**
     * 设置备注
     * @param userBean 设置备注的目标
     * @param note 备注内容
     */
    void setNote(UserBean userBean, String note);
}

