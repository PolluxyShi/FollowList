package com.example.followlist.data.network;

import com.example.followlist.data.model.UserBean;
import java.util.List;

/**
 * API 分页响应模型
 */
public class ApiResponse {
    private List<UserBean> data;      // 数据列表
    private int total;                // 总数据量
    private int page;                 // 当前页码
    private int pageSize;             // 每页大小
    private boolean hasMore;          // 是否还有更多数据

    public ApiResponse(List<UserBean> data, int total, int page, int pageSize, boolean hasMore) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.hasMore = hasMore;
    }

    public List<UserBean> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}

