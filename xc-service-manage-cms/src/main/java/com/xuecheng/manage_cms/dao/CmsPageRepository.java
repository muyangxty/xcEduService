package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author MuYang
 * @date 2019/11/25
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    //根据页面名称查询
    CmsPage findByPageName(String pageName);

    //根据页面名称、站点Id、页面webpath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}
