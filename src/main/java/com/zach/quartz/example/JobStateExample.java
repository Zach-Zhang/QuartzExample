package com.zach.quartz.example;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

public class JobStateExample {
	
	public void run() throws SchedulerException{ 
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		Date startTime = nextGivenSecondDate(null, 10);
		
		JobDetail job1 = newJob(ColorJob.class).withIdentity("job1","group1").build();
		SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInHours(10).withRepeatCount(4)).build();
		
		job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Green");
		job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);
		
		Date scheduleTime1 = sched.scheduleJob(job1,trigger1);
		System.out.println(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount()
        + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");
		
		
		JobDetail job2 = newJob(ColorJob.class).withIdentity("job2","group2").build();
		SimpleTrigger trigger2 = newTrigger().withIdentity("trigger2", "group2").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInHours(10).withRepeatCount(4)).build();
		
		job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Red");
		job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);
		
		Date scheduleTime2 = sched.scheduleJob(job2,trigger2);
		System.out.println(job2.getKey() + " will run at: " + scheduleTime2 + " and repeat: " + trigger2.getRepeatCount()
		+ " times, every " + trigger2.getRepeatInterval() / 1000 + " seconds");
		
		sched.start();
		
		try {
			Thread.sleep(60L*1000L);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		SchedulerMetaData metaData = sched.getMetaData();
		System.err.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}
	
	public static void main(String[] args) throws SchedulerException {
		JobStateExample example = new JobStateExample();
		example.run();
	}
}
