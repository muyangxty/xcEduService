package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.IPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author MuYang
 * @date 2019/11/27
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private IPageService pageService;

    /**
     * 页面预览
     * @param pageId 页面id
     */
    @RequestMapping(value = "/cms/preview/{pageId}", method = RequestMethod.GET)
    public void preview(@PathVariable("pageId")String pageId){
        //执行静态化
        String pageHtml = pageService.getPageHtml(pageId);
        try {
            //通过response对象将内容输出
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pageHtml.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
