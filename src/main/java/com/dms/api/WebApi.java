package com.dms.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@EnableAsync
@SpringBootApplication
@EnableSpringHttpSession
@PropertySource(value={"classpath:/common.properties"})
public class WebApi extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {
	Logger logger = LoggerFactory.getLogger(WebApi.class);

	public static void main(String[] args) {
		SpringApplication.run(WebApi.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder) {
		return springApplicationBuilder.sources(WebApi.class);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		logger.info("程序启动成功......");
	}
}
