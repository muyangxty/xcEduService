package com.xuecheng.test.freemarker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author MuYang
 * @date 2019/11/27
 */
@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/banner")
    public String index_banner(Map<String, Object> map){
        //使用restTemplate请求轮播图的模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "index_banner";
    }
}
