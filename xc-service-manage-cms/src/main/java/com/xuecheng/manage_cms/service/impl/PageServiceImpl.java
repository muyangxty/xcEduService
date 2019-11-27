package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.IPageService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 页面相关的业务层实现类
 *
 * @author MuYang
 * @date 2019/11/25
 */
@Service
public class PageServiceImpl implements IPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 页面查询
     *
     * @param page             页码,从1开始计数
     * @param size             每页显示记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //判断queryPageRequest是否为null
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //设置条件值
        if (StringUtils.isNoneEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNoneEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNoneEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        //实现自定义条件查询，分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        //数据列表
        queryResult.setList(all.getContent());
        //数据总记录数
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 新增
     *
     * @param cmsPage
     * @return
     */
    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //获取页面名称、站点Id、页面webpath
        String pageName = cmsPage.getPageName();
        String siteId = cmsPage.getSiteId();
        String pageWebPath = cmsPage.getPageWebPath();
        CmsPage result = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);
        if (result != null) {
            //添加失败,抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //校验页面名称、站点Id、页面webpath
        cmsPage.setPageId(null);
        //执行新增
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);

    }

    /**
     * 根据页面id查询页面
     *
     * @param id 页面id
     * @return
     */
    @Override
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    /**
     * 修改页面
     *
     * @param id      页面id
     * @param cmsPage 页面数据
     * @return
     */
    @Override
    public CmsPageResult modify(String id, CmsPage cmsPage) {
        //根据id查询页面
        CmsPage cmsPage1 = this.getById(id);
        //判断查询结果是否不为null，不为null，执行修改
        if (cmsPage1 != null) {
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            cmsPage1.setPageName(cmsPage.getPageName());
            cmsPage1.setSiteId(cmsPage.getSiteId());
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //执行修改
            CmsPage page = cmsPageRepository.save(cmsPage1);
            //判断是否修改成功
            if (page != null) {
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, page);
                return cmsPageResult;
            }
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 根据id,删除页面
     *
     * @param id 页面id
     * @return
     */
    @Override
    public ResponseResult delete(String id) {
        //根据id查询页面
        CmsPage page = this.getById(id);
        if (page != null) {
            //执行删除
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

}
