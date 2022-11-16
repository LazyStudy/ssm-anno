package com.bookmall.ssm.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

// 配置datasource到ioc容器里面
@Configuration
// 配置mybatis mapper的扫描路径
@MapperScan("com.bookmall.ssm.mapper")
public class MapperConfiguration {



}
