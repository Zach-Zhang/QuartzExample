package com.zach.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Bean工具类
 *         普通类调用注解Service方法
 * @author zg
 * @date 2017年1月9日
 */
public class SpringBeanFactoryUtils implements ApplicationContextAware {

	private static ApplicationContext appCtx;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appCtx = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
        return appCtx;
    }
	
	/**
     * 根据 Bean Name 快速得到一个BEAN
     * @param beanName bean的名字
     * @return 返回一个bean对象
     */
	public static Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }


}
