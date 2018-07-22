package com.zach.quartz.example;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleExample {
	public void run() throws Exception {
		 System.out.println("============Initializing==============");
		 
		 SchedulerFactory sf = new StdSchedulerFactory();
		 Scheduler scheduler = sf.getScheduler();
		 System.out.println("=========Initialization Complete");
		 
		 Date runTime = evenMinuteDate(new Date());
		 JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();
		 Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
		 
		 scheduler.scheduleJob(job, trigger);
		 System.out.println(job.getKey() +"  will run at: "+ runTime);
		 
		 scheduler.start();
		 
		 Thread.sleep(6L*1000L);
		 
		 scheduler.shutdown();
	}
	
	public static void main(String[] args) {
		SimpleExample example = new SimpleExample();
		try {
			example.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
