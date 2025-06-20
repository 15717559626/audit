package com.snow.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/1 15:35
 **/

@Configuration
@EnableSwagger2
//@Profile("dev")
public class SwaggerConfig {
    /**
     * 创建API应用 apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     */
    @Bean
    public Docket sys() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户中心")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.snow.audit.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("测试接口文档")
                /*.contact(new Contact("TEST","https://www.baidu.com","xxxxxax@163.com"))
                .termsOfServiceUrl("https://www.baidu.com")*/
                .description("测试接口文档")
                .version("1.0")
                .build();
    }



}
