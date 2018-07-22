package com.zach;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mx4j.tools.adaptor.http.HttpAdaptor;

public class SpringFactory {
	private final static Log log = LogFactory.getLog(SpringFactory.class);

	private static Lock lock = new ReentrantLock();

	private static SpringFactory instance;

	private final ApplicationContext context;

	private SpringFactory() {
//		context = new ClassPathXmlApplicationContext(new String[] {
//				"beanRefFramework.xml", "beanRefConfigurer.xml",
//				"beanRefJMXServer.xml", "beanRefDataAccess.xml" });
		context = new ClassPathXmlApplicationContext(new String[] {
				"application-context.xml", "application-mvc.xml","application-quartz.xml"});
	}

	public static SpringFactory getInstance() {
		if (instance == null) {
			lock.lock();
			try {
				if (instance == null) {
					instance = new SpringFactory();
				}
			} finally {
				lock.unlock();
			}
		}
		return instance;
	}

	public void startJmxHttpAdaptor() throws IOException {
		HttpAdaptor httpAdaptor = (HttpAdaptor) getBean("httpAdaptor");
		if (!httpAdaptor.isActive()) {
			httpAdaptor.start();

			log.fatal("JMX URL : http://" + httpAdaptor.getHost() + ":"
					+ httpAdaptor.getPort());
		}
	}

	public Object getBean(String beanName) {
		return context.getBean(beanName);
	}
}