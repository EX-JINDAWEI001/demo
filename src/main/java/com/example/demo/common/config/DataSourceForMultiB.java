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

/**
 * 多个数据源配置
 */
@Configuration
@MapperScan(basePackages = "com.example.demo.b.**.dao.mapper.**", sqlSessionFactoryRef = "bSqlSessionTemplate")
public class DataSourceForMultiB {

    @Autowired
    private Environment env;

    @Value("${mybatis.mapper-locations}")
    private String bMapperLocations;

    @Bean(name = "bDataSource")
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.b.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.datasource.b.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.b.username"));
        dataSource.setPassword(env.getProperty("spring .datasource.b.password"));
        return dataSource;
    }

    @Bean(name = "bSqlSessionFactory")
    public SqlSessionFactory aSqlSessionFactory(@Qualifier("bDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(bMapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "bSqlSessionTemplate")
    public SqlSessionTemplate aSqlSessionTemplate(@Qualifier("bSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "bTransactionManager")
    public DataSourceTransactionManager aTransactionManager(@Qualifier("bDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
