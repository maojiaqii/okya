package com.top.okya.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// 配置文件映射
@Configuration
@PropertySource(value = "classpath:config.properties", encoding = "utf-8")
public class ServerConfigMap {

	@Value("${server.port:#{null}}")
	private int sysPort;
	@Value("${file.staticAccessPath:#{null}}")
	private String staticAccessPath;
	@Value("${file.uploadFolder:#{null}}")
	private String uploadFolder;

	@Bean
	public ServerConfig initConfig() {
		ServerConfig.sysPort = sysPort;
		ServerConfig.staticAccessPath = staticAccessPath;
		ServerConfig.uploadFolder = uploadFolder;
		return null;
	}
}