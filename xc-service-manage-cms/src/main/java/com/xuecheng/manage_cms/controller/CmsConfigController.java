package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.IPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CMS配置相关的控制器层
 *
 * @author MuYang
 * @date 2019/11/27
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    private IPageService pageService;

    /**
     * 根据id查询CMS相关配置信息
     *
     * @param id
     * @return
     */
    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        CmsConfig cmsConfig = pageService.getConfigById(id);
        return cmsConfig;
    }

}
