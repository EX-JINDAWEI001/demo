package com.example.demo.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 多个数据源配置
 */
@Configuration
@MapperScan(basePackages = "com.example.demo.a.**.dao.mapper.**", sqlSessionFactoryRef = "aSqlSessionTemplate")
public class DataSourceForMultiA {

    @Autowired
    private Environment env;

    @Value("${mybatis.mapper-locations}")
    private String aMapperLocations;

    @Bean(name = "aDataSource")
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.a.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.datasource.a.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.a.username"));
        dataSource.setPassword(env.getProperty("spring .datasource.a.password"));
        return dataSource;
    }

    @Bean(name = "aSqlSessionFactory")
    public SqlSessionFactory aSqlSessionFactory(@Qualifier("aDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(aMapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "aSqlSessionTemplate")
    public SqlSessionTemplate aSqlSessionTemplate(@Qualifier("aSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "aTransactionManager")
    public DataSourceTransactionManager aTransactionManager(@Qualifier("aDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
