package com.xuecheng.manage_cms.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.IPageService;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
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

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

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
        if (cmsPage == null) {
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
            cmsPage1.setDataUrl(cmsPage.getDataUrl());
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

    /**
     * 根据id查询CMS配置
     *
     * @param id
     * @return
     */
    @Override
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }

    /**
     * 执行页面静态化
     * 静态化程序获取页面的DataUrl
     * 静态化程序远程请求DataUrl获取数据模型
     * 静态化程序获取页面的模板信息
     * 执行页面静态化
     * @param pageId 页面id
     * @return
     */
    @Override
    public String getPageHtml(String pageId) {
        //获取模型数据
        Map model = this.getModelByPageId(pageId);
        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //获取页面模板信息
        String templaterByPageId = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(templaterByPageId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = this.generateHtml(templaterByPageId, model);
        return null;
    }

    /**
     * 根据页面id,获取数据模型
     *
     * @param pageId 页面id
     * @return
     */
    private Map getModelByPageId(String pageId) {
        //获取页面信息，并判断是否为null
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTFOUND);
        }
        //获取dataUrl,并判断是否为null
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     * 根据页面id,获取模板信息
     *
     * @param pageId 页面id
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        //获取页面信息，并判断是否为null
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTFOUND);
        }
        //获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中获取模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            
            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            //从流中获取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 页面静态化
     * @param template 模板信息
     * @param model 数据模型
     * @return
     */
    private String generateHtml(String template, Map model){
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //生成模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", template);
            //向configuration配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            //调用api进行静态化
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
