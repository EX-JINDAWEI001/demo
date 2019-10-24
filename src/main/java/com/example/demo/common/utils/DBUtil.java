package com.example.demo.common.utils;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 使用连接池来管理数据库连接
 *
 * @author jindawei
 */
public class DBUtil {
    private static ThreadLocal<Connection> t = new ThreadLocal<Connection>();
    private static BasicDataSource ds = new BasicDataSource();

    static {
        //加载配置文件
        Properties p = new Properties();
        try {
            p.load(DBUtil.class.getClassLoader().getResourceAsStream("application.properties"));
            String driver = p.getProperty("driver");
            String url = p.getProperty("url");
            String username = p.getProperty("username");
            String password = p.getProperty("password");

            //连接池的最大连接数
            int maxActive = Integer.parseInt(p.getProperty("maxactive"));
            //当没有可用连接时的最大等待时间
            int maxWait = Integer.parseInt(p.getProperty("maxwait"));

            //初始化连接池
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setMaxActive(maxActive);
            ds.setMaxWait(maxWait);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个数据库连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        /*
         * 向连接池要连接，若连接池没有可用连接时，该方法会进入阻塞状态，阻塞时间由maxWait设置的最大时间决定
         * 在等待过程中一旦有可用连接，那么连接池会立刻返回。若等待时间超时，会抛出超时异常。
         */
        Connection conn = ds.getConnection();
        t.set(conn);
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        Connection conn = t.get();
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        DBUtil.getConnection();
        System.out.println("获取成功");
        System.out.println("caonima");
        DBUtil.closeConnection();
    }

}
