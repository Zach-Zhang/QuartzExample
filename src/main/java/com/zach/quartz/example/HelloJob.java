package com.zach.quartz.example;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job{
	
	private static final Logger bizDataLogger = LoggerFactory.getLogger("bizDataLogger");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		bizDataLogger.info("Hello World--"+(new Date()));
	}
	
}
