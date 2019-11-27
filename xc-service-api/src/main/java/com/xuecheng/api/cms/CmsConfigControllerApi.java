package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * CMS配置管理接口
 *
 * @author MuYang
 * @date 2019/11/27
 */
@Api(value = "cms配置管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询CMS配置信息")
    public CmsConfig getModel(String id);
}
