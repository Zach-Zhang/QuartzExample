package com.zach.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 脱离Spring框架,手动管理连接,用于项目初始化时的插库操作
 * 
 * @author  章生志
 * @date 2018年7月3日
 * @title DBUtil
 */
public class DBUtil {
	
	
	public static ThreadLocal<Connection> container = new ThreadLocal<Connection>();
	
	public static final Logger log = LoggerFactory.getLogger("bizDataLogger");

	public static Connection getInitConection() {
		Properties properties = null;
		InputStream is = null;
		Connection connection = null;
		DruidDataSource ds = new DruidDataSource();
		try {
			properties = new Properties();
			is = DBUtil.class.getClassLoader().getResourceAsStream("datasource.properties");
			properties.load(is);
			String driverClassName = properties.getProperty("default.jdbc.driver");
			String url = properties.getProperty("default.jdbc.url");
			String username = properties.getProperty("default.jdbc.username");
			String password = properties.getProperty("default.jdbc.password");
			String maxActive = properties.getProperty("default.jdbc.maxActive");
			String maxWait = properties.getProperty("default.jdbc.maxWait");
			String initialSize = properties.getProperty("default.jdbc.initialSize");
			
			ds.setDriverClassName(driverClassName);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setUrl(url);
			ds.setMaxActive(Integer.parseInt(maxActive));
			ds.setMaxWait(Integer.parseInt(maxWait));
			ds.setInitialSize(Integer.parseInt(initialSize));
			ds.setMaxActive(Integer.parseInt(maxActive));
			ds.setTimeBetweenEvictionRunsMillis(6000);
			ds.setMinIdle(1);
			ds.setValidationQuery("SELECT 'x'");
			ds.setRemoveAbandoned(true);
			ds.setRemoveAbandonedTimeout(60);  //60秒
			connection = ds.getConnection();
			container.set(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(is !=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return connection;
	}
	
	/*** 获取当前线程上的连接开启事务 */
	public static void startTransaction() {
		Connection conn = container.get();// 首先获取当前线程的连接
		if (conn == null) {// 如果连接为空
			conn = getInitConection();// 从连接池中获取连接
			container.set(conn);// 将此连接放在当前线程上
			log.info(Thread.currentThread().getName() + "空连接从dataSource获取连接");
		} else {
			log.info(Thread.currentThread().getName() + "从缓存中获取连接");
		}
		try {
			conn.setAutoCommit(false);// 开启事务
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 提交事务
	public static void commit() {
		try {
			Connection conn = container.get();// 从当前线程上获取连接if(conn!=null){//如果连接为空，则不做处理
			if (null != conn) {
				conn.commit();// 提交事务
				log.info(Thread.currentThread().getName() + "事务已经提交......");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** 回滚事务 */
	public static void rollback() {
		try {
			Connection conn = container.get();// 检查当前线程是否存在连接
			if (conn != null) {
				conn.rollback();// 回滚事务
				container.remove();// 如果回滚了，就移除这个连接
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** 关闭连接 */
	public static void close() {
		try {

			while (container.get() != null && !container.get().isClosed()) {

				container.get().close();
				log.info(Thread.currentThread().getName() + "连接已经关闭......");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				container.remove();// 从当前线程移除连接切记
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}
