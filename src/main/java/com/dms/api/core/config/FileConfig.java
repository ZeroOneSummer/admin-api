package com.dms.api.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @author 40
 * date 2017/11/9
 * time 9:19
 * decription:
 */
@Configuration
public class FileConfig {

    public static String configPath;

    @Value("${tomcatTempDir}")
    private String tomcatTempDir;

    @Value("${key.path}")
    public void setConfigPath(String path){
        configPath = path;
    }

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        //创建目录
        File dir = new File(tomcatTempDir);
        try {
            if (!dir.exists()) dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(tomcatTempDir);
        return factory.createMultipartConfig();
    }

}
