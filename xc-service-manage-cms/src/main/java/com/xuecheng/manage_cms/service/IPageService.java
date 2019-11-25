package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @author MuYang
 * @date 2019/11/25
 */
public interface IPageService {

    /**
     * 页面查询
     * @param page 当前页
     * @param size 每页显示记录数
     * @param queryPageRequest
     * @return
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
