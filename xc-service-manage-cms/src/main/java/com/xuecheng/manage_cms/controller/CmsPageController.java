package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.IPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 页面相关的控制器层
 *
 * @author MuYang
 * @date 2019/11/25
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private IPageService pageService;

    /**
     * 分页查询
     *
     * @param page             页码
     * @param size             每页显示记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findList(page, size, queryPageRequest);
    }

    /**
     * 新增页面
     *
     * @param cmsPage 页面数据
     * @return
     */
    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    /**
     * 根据页面id查询页面
     *
     * @param id 页面id
     * @return
     */
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findByid(@PathVariable("id") String id) {
        return pageService.getById(id);
    }

    /**
     * 根据页面id,修改页面
     *
     * @param id      页面id
     * @param cmsPage 需要修改的页面数据
     * @return
     */
    @Override
    @PutMapping("/update/{id}")
    public CmsPageResult update(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.modify(id, cmsPage);
    }

    /**
     * 根据页面id,删除页面
     *
     * @param id 页面id
     * @return
     */
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }

}
