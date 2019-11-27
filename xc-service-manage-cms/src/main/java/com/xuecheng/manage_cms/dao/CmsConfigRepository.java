package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CMS配置管理dao
 *
 * @author MuYang
 * @date 2019/11/25
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {


}
