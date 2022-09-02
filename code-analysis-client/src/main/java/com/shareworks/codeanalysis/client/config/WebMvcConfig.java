package com.shareworks.codeanalysis.client.config;

import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:27
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private EnumConvertFactory enumConvertFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConvertFactory);
    }
}
