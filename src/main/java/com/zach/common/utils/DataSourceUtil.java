package com.zach.common.utils;

import java.sql.Connection;

import com.alibaba.druid.pool.DruidDataSource;

public class DataSourceUtil {

	/**
	 * 数据源的初始化
	 * 
	 * @param fromdatasource
	 *            void
	 *
	 */
	
	private static DruidDataSource dataSource = (DruidDataSource) SpringBeanFactoryUtils.getBean("dataSource");

	// 声明线程共享变量
	public static ThreadLocal<Connection> container = new ThreadLocal<Connection>();
	
	/**
	 * 获取数据连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			System.out.println(Thread.currentThread().getName() + "连接已经开启......");
			container.set(conn);
		} catch (Exception e) {
			System.out.println("连接获取失败");
			e.printStackTrace();
		}
		return conn;
	}

	/*** 获取当前线程上的连接开启事务 */
	public static void startTransaction() {
		Connection conn = container.get();// 首先获取当前线程的连接
		if (conn == null) {// 如果连接为空
			conn = getConnection();// 从连接池中获取连接
			container.set(conn);// 将此连接放在当前线程上
			System.out.println(Thread.currentThread().getName() + "空连接从dataSource获取连接");
		} else {
			System.out.println(Thread.currentThread().getName() + "从缓存中获取连接");
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
				System.out.println(Thread.currentThread().getName() + "事务已经提交......");
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
				System.out.println(Thread.currentThread().getName() + "连接已经关闭......");
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
