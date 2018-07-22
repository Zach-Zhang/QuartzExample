package com.zach.quartz.example;

import static org.quartz.DateBuilder.nextGivenSecondDate;

import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExceptionExample {
	
	public void run() throws Exception {
	    Logger log = LoggerFactory.getLogger(JobExceptionExample.class);
	    
	    log.info("------- Initializing ----------------------");
	    
	    SchedulerFactory sf = new StdSchedulerFactory();
	    Scheduler sched = sf.getScheduler();
	    
	    log.info("------- Initialization Complete ------------");

	    log.info("------- Scheduling Jobs -------------------");
	    
	    Date startTime = nextGivenSecondDate(null,15);
	    
	    
	}
}
