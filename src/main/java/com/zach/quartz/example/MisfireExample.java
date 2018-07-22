package com.zach.quartz.example;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MisfireExample {
	
	public void run() throws Exception {
	    Logger log = LoggerFactory.getLogger(MisfireExample.class);
	    log.info("------- Initializing -------------------");
	    
	    SchedulerFactory sf = new StdSchedulerFactory();
	    Scheduler sched = sf.getScheduler();
	    
	    log.info("------- Initialization Complete -----------");

	    log.info("------- Scheduling Jobs -----------");
	    
	    Date startTime = nextGivenSecondDate(null, 15);
	    
	    JobDetail job = newJob(StatefulDumbJob.class).withIdentity("statefulJob1","group1")
	    	.usingJobData(StatefulDumbJob.EXECUTION_DELAY,10000L).build();
	    
	    SimpleTrigger trigger = newTrigger().withIdentity("trigger1","group1").startAt(startTime)
	    	.withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();
	    
	    Date ft = sched.scheduleJob(job,trigger);
	    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    job = newJob(StatefulDumbJob.class).withIdentity("statefulJob2", "group1")
	            .usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L).build();
	    trigger = newTrigger()
	    		.withIdentity("trigger2", "group1")
	    		.startAt(startTime)
	    		.withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()
	    				.withMisfireHandlingInstructionFireNow()).build();
	    
	    ft = sched.scheduleJob(job, trigger);
	    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    log.info("------- Starting Scheduler ----------------");
	    // jobs don't start firing until start() has been called...
	    sched.start();
	    log.info("------- Started Scheduler -----------------");
	    try {
	    	// sleep for ten minutes for triggers to file....
	        Thread.sleep(300L * 1000L);
		} catch (Exception e) {
			// TODO: handle exception
		}
	    

	    log.info("------- Shutting Down ---------------------");

	    sched.shutdown(true);

	    log.info("------- Shutdown Complete -----------------");
	    
	    SchedulerMetaData metaData = sched.getMetaData();
	    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	    
	}
	
	public static void main(String[] args) throws Exception {
		
		MisfireExample example = new MisfireExample();
		example.run();
	}
}
