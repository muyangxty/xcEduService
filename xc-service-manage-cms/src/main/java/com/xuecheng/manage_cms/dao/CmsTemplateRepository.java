package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author MuYang
 * @date 2019/11/25
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {

}
