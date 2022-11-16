package com.bookmall.ssm.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;
import java.io.File;

// 标明一个配置类
@Configuration
// 包扫描
@ComponentScan("com.bookmall.ssm.bean")
// 开启基于注解的AOP模式
@EnableAspectJAutoProxy
// 引用外部文件
@PropertySource("classpath:jdbc.properties")
// 开启事务注解驱动，在需要的方法上加上事务注解驱动，@Transactional
@EnableTransactionManagement
public class SpringConfig  implements EmbeddedValueResolverAware {

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.url}")
    private String url;

    private StringValueResolver valueResolver;

    // profile标识应用场景 default表示默认使用
    @Profile("default")
    @Bean
    public DruidDataSource dataSource(@Value("${jdbc.password}") String password){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);

        String driver = valueResolver.resolveStringValue("${jdbc.driver}");
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.valueResolver = stringValueResolver;
    }

    // 创建JdbcTemplate对象
    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource){
        // 到IOC容器中根据类型中找到 dataSource 完成注入
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        // 注入dataSource
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    // 创建事务管理器,使用事务管理数据源
    public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * 配置SqlSessionFactoryBean,可以直接在Spring中IOC中获取SqlSessionFactory对象
     */
    @Bean
    public SqlSessionFactoryBean sessionFactoryBean(DataSource dataSource){

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        factoryBean.setDataSource(dataSource);
        // 设置别名
        factoryBean.setTypeAliasesPackage("com.bookmall.ssm.bean");

        return factoryBean;

    }

    /**
     * 配置mapper接口的扫描，可以将指定包下所有mapper接口
     * 通过SqlSession创建代理实现类对象，并将这些对象交给IOC容器管理
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.bookmall.ssm.mapper");
        return configurer;
    }
}
