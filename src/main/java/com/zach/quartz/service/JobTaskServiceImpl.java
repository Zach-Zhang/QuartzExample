package com.zach.quartz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zach.common.utils.JobUtils;
import com.zach.quartz.entity.ScheduleJob;
import com.zach.quartz.job.QuartzJobFactory;
import com.zach.quartz.job.QuartzJobFactoryDisallowConcurrentExecution;
import com.zach.quartz.mapper.ScheduleJobMapper;

@Service
public class JobTaskServiceImpl implements JobTaskService {

	public static final Logger log = LoggerFactory.getLogger("bizDataLogger");

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private ScheduleJobMapper scheduleJobMapper;

	/**
	 * 从数据库中取 区别于getAllJob
	 * 
	 * @return
	 */
	public List<ScheduleJob> getAllTask() {
		return scheduleJobMapper.select(null);
	}

	/**
	 * 添加到数据库中 区别于addJob
	 */
	public void addTask(ScheduleJob job) {
		job.setCreateTime(new Date());
		job.setUpdateTime(new Date());
		scheduleJobMapper.insertSelective(job);
	}

	/**
	 * 从数据库中查询job
	 */
	public ScheduleJob getTaskById(Long jobId) {
		return scheduleJobMapper.selectByPrimaryKey(jobId);
	}

	/**
	 * 更改任务状态
	 * 
	 * @throws Exception
	 */
	public void changeStatus(Long jobId, String cmd) {
		try {
			ScheduleJob job = getTaskById(jobId);
			if (job == null) {
				return;
			}
			if ("stop".equals(cmd)) {
				deleteJob(job);
				job.setJobStatus(JobUtils.STATUS_NOT_RUNNING);
			} else if ("start".equals(cmd)) {
				job.setJobStatus(JobUtils.STATUS_RUNNING);
				addJob(job);
			}
			scheduleJobMapper.updateByPrimaryKeySelective(job);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更改任务 cron表达式
	 * 
	 * @throws Exception
	 */
	public void updateCron(Long jobId, String cron) {
		try {
			ScheduleJob job = getTaskById(jobId);
			if (job == null) {
				return;
			}
			job.setCronExpression(cron);
			if (JobUtils.STATUS_RUNNING.equals(job.getJobStatus())) {
				updateJobCron(job);
			}
			scheduleJobMapper.updateByPrimaryKeySelective(job);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加任务
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addJob(ScheduleJob job) {
		try {
			if (job == null || !JobUtils.STATUS_RUNNING.equals(job.getJobStatus())) {
				return;
			}
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			log.debug(scheduler
					+ ".......................................................................................add");
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 不存在，创建一个
			if (null == trigger) {
				Class clazz = JobUtils.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class
						: QuartzJobFactoryDisallowConcurrentExecution.class;
				JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup())
						.build();

				jobDetail.getJobDataMap().put("scheduleJob", job);

				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
						.withSchedule(scheduleBuilder).build();

				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// Trigger已存在，那么更新相应的定时设置
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*@PostConstruct
	public void init() throws Exception {
		System.out.println("呵呵==================="+new Date());
		// 这里获取任务信息数据
		List<ScheduleJob> jobList = scheduleJobMapper.select(null);

		for (ScheduleJob job : jobList) {
			addJob(job);
		}
	}*/
	

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleJob> getAllJob() {
		List<ScheduleJob> jobList = new ArrayList<>();
		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			jobList = new ArrayList<ScheduleJob>();
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					ScheduleJob job = new ScheduleJob();
					job.setJobName(jobKey.getName());
					job.setJobGroup(jobKey.getGroup());
					job.setDescription("触发器:" + trigger.getKey());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					job.setJobStatus(triggerState.name());
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						job.setCronExpression(cronExpression);
					}
					jobList.add(job);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobList;
	}

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleJob> getRunningJob() {
		List<ScheduleJob> jobList = new ArrayList<>();
		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			jobList = new ArrayList<ScheduleJob>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				ScheduleJob job = new ScheduleJob();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				job.setDescription("触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setJobStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobList;
	}

	/**
	 * 暂停一个job
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void pauseJob(ScheduleJob scheduleJob) {
		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 恢复一个job
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void resumeJob(ScheduleJob scheduleJob) {
		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除一个job
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void deleteJob(ScheduleJob scheduleJob) {
		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void runAJobNow(ScheduleJob scheduleJob) {
		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.triggerJob(jobKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新job时间表达式
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void updateJobCron(ScheduleJob scheduleJob) {

		try {
			//Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// CronScheduleBuilder scheduleBuilder =
		// CronScheduleBuilder.cronSchedule("xxxxx");

		/*
		 * ScheduleJob job = new ScheduleJob();
		 * job.setBeanClass("com.zach.quartz.service.TaskExample");
		 * job.setCreateTime(new Date()); job.setJobName("myTest");
		 * job.setCronExpression("0/10 * * * * ?");
		 * job.setDescription("这只是一个测试啦"); job.setIsConcurrent("0");
		 * job.setJobGroup("testGroup"); job.setJobId(1); job.setJobStatus("1");
		 * job.setMethodName("test"); job.setSpringId(null);
		 * job.setUpdateTime(new Date()); JobTaskService js = new
		 * JobTaskService(); //js.addJob(scheduleJob); ApplicationContext ac =
		 * new ClassPathXmlApplicationContext("spring-quartz.xml"); StdScheduler
		 * scheduler = (StdScheduler) ac.getBean("schedulerFactoryBean");
		 * //Scheduler scheduler = sfb.getScheduler(); Class clazz =
		 * JobUtils.CONCURRENT_IS.equals(job.getIsConcurrent()) ?
		 * QuartzJobFactory.class :
		 * QuartzJobFactoryDisallowConcurrentExecution.class; JobDetail
		 * jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(),
		 * job.getJobGroup()).build();
		 * 
		 * jobDetail.getJobDataMap().put("scheduleJob", job);
		 * 
		 * CronScheduleBuilder scheduleBuilder =
		 * CronScheduleBuilder.cronSchedule(job.getCronExpression());
		 * 
		 * CronTrigger trigger =
		 * TriggerBuilder.newTrigger().withIdentity(job.getJobName(),
		 * job.getJobGroup()) .withSchedule(scheduleBuilder).build();
		 */

		// scheduler.scheduleJob(jobDetail, trigger);
		// JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());

		// scheduler.triggerJob(jobKey);
		// scheduler.start();
		// js.runAJobNow(scheduleJob);

	}

}
