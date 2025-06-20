package com.snow.audit.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/***********************************************************************************************************************
 *                注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 * @author 云岚
 * @date 2020-12-11 10:58
 * @version V1.0
 **********************************************************************************************************************/
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private HttpMessageConverters httpMessageConverters;

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(new JacksonMapper());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(httpMessageConverters.getConverters());
    }
}
