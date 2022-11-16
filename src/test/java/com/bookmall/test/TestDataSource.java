package com.bookmall.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.bookmall.ssm.config.SpringConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDataSource {

    @Test
    public void testDatasource() throws SQLException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        DataSource bean = context.getBean(DataSource.class);

        Connection connection = bean.getConnection();

        System.out.println(connection);

    }

}
