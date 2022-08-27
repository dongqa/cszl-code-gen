package com.cszl.codegen.gen.service.impl;

import cn.hutool.core.compress.Gzip;
import cn.hutool.json.JSONUtil;
import com.cszl.codegen.gen.service.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 代码转换
 *
 * @author wdq
 * @email a13123003060@gmail.com
 * @date 2022/7/4 16:54
 */
@Service
public class TransServiceImpl implements TransService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Object json2JavaBeanHtml(String packageName, String json) {
        if (!JSONUtil.isJson(json)) {
            throw new RuntimeException("请输入正确的json格式数据");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("origin", "https://www.bejson.com");
        headers.add("referer", "https://www.bejson.com/json2javapojo/");
        headers.add("accept", "pplication/json, text/javascript, */*; q=0.01");
        headers.add("accept-language", "zh-CN,zh;q=0.9");
        headers.add("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        headers.add("Accept", "*/*");
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("Cookie", "__yjs_duid=1_8ad3554ea29b1b485c1d167cd415e7051656924414979");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Resource> response = restTemplate.exchange("https://www.bejson.com/Bejson/Api/JsonToJavapojo?json={json}&package={package}",
                HttpMethod.GET,
                httpEntity,
                Resource.class,
                json,
                packageName
        );
        ;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            Gzip.of(response.getBody().getInputStream(), outputStream).unGzip();
            String s = outputStream.toString();
            if (JSONUtil.isJson(s)) {
                s = filterType(s);
                return JSONUtil.parseObj(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("远程接口异常");
    }

    /**
     * 过滤类型
     *
     * @param s
     * @return
     */
    private String filterType(String s) {
        return s
                .replace("private int", "private Integer")
                .replace("(int", "(Integer")
                .replace("private double", "private Double")
                .replace("(double", "(Double")
                .replace("private boolean", "private Boolean")
                .replace("(boolean", "(Boolean")
                ;
    }

}
