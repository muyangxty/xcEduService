package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author MuYang
 * @date 2019/11/25
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.err.println(all);
    }

    //分页查询
    @Test
    public void testFindPage(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.err.println(all);
    }

    //根据页面名称查询
    @Test
    public void findByPageName(){
        CmsPage pageName = cmsPageRepository.findByPageName("index.html");
        System.err.println(pageName);
    }

    //自定义条件查询测试
    @Test
    public void testFindAllByExample(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //条件匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        //定义Example
        Example<CmsPage> example = Example.of(cmsPage, matching);

        Page<CmsPage> content = cmsPageRepository.findAll(example, pageable);
        System.err.println(content);
    }


}
