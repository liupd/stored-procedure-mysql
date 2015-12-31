package com.wa.edu.utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by liupd on 15-7-28.
 **/
public class DBUtil {

    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    //定义一个连接池对象变量
    private static DruidDataSource ds;

    static{//目的是只执行一次
        try{
            Properties props = new Properties();
            InputStream inStream =DBUtil.class.getResourceAsStream("/resources.properties");
            props.load(inStream);
            DRIVER = props.getProperty("database.driver");
            URL = props.getProperty("database.url");
            USERNAME = props.getProperty("database.username");
            PASSWORD = props.getProperty("database.password");
			/**
             * 注册驱动(只需要注册一次)
			 * Class.forName(driver);
             * 实例化连接池对象
            **/
            ds = new DruidDataSource();
            ds.setDriverClassName(DRIVER);
            ds.setUrl(URL);
            ds.setUsername(USERNAME);
            ds.setPassword(PASSWORD);
            ds.setMaxActive(20);//设置最大连接数
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 获取一个Connection对象
     * @return
     * @throws Exception
     */
    public static Connection getConnection(){
       /**
        * 指定数据库连接参数
		*  Connection con = DriverManager.getConnection(url,username,password);
        **/
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


    public static void closeConnection(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println(getConnection());
    }
}
