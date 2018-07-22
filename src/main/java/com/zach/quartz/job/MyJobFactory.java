package com.zach.quartz.job;

import javax.annotation.Resource;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;
/**
 * 
 * @author  章生志
 * @date 2018年4月9日
 * @title MyJobFactory
 */

/**
	 * "0/10 * * * * ?" 每10秒触发 
	 * "0 0/5 * * * ?" 每5分钟触发
	 * "0 0/10 * * * ?" 每10分钟触发
	 * "0 0 12 * * ?" 每天中午12点触发
	 * "0 15 10 ? * *" 每天上午10:15触发 
	 * "0 15 10 * * ?" 每天上午10:15触发 
	 * "0 15 10 * * ? *" 每天上午10:15触发 
	 * "0 15 10 * * ? 2005" 2005年的每天上午10:15触发 
	 * "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 
	 * "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发 
	 * "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 
	 * "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发 
	 * "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发 
	 * "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发 
	 * "0 15 10 15 * ?" 每月15日上午10:15触发 
	 * "0 15 10 L * ?" 每月最后一日的上午10:15触发 
	 * "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发 
	 * "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发 
	 * "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发 
	 */
/*@Component
public class MyJobFactory extends AdaptableJobFactory {
	@Resource
	private AutowireCapableBeanFactory factory;
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		//调用父类的原有的方法创建JobDetail对象
		Object jobDetail = super.createJobInstance(bundle);
		//把JobDetail放入SpringIOC容器中管理
		factory.autowireBean(jobDetail);
		return jobDetail;
	}

}*/
