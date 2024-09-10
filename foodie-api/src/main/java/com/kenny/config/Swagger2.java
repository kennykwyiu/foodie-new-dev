package com.kenny.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
    // http://localhost:8088/swagger-ui.html
    // http://localhost:8088/doc.html
    @Bean
    public Docket createRestApi()  {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kenny.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("Foodie - e-commerce shop")
                .contact(new Contact("kenny", "https://www.kenny.com", "kenny@gmail.com"))
                .description("Foodie dev api docs")
                .version("1.0.1")
                .termsOfServiceUrl("https://www.kenny.com")
                .build();
    }
}
