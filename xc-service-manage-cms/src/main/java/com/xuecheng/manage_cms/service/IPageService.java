package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

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

    /**
     * 新增
     * @param cmsPage
     * @return
     */
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据页面id查询页面
     * @param id 页面id
     * @return
     */
    CmsPage getById(String id);

    /**
     * 修改页面
     * @param id 页面id
     * @param cmsPage 页面数据
     * @return
     */
    CmsPageResult modify(String id, CmsPage cmsPage);

    /**
     * 根据id,删除页面
     * @param id 页面id
     * @return
     */
    ResponseResult delete(String id);
}
