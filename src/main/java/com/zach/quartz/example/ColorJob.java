package com.zach.quartz.example;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

public class ColorJob implements Job {
	
	public static final String FAVORITE_COLOR = "favorite color";
	public static final String EXECUTION_COUNT = "count";
	
	private int counter = 1;
	
	
	public ColorJob() {
	}


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		JobDataMap data = context.getJobDetail().getJobDataMap();
		String favoriteColor = data.getString(FAVORITE_COLOR);
		int count = data.getInt(EXECUTION_COUNT);
		  System.out.println("ColorJob: " + jobKey + " executing at " + new Date() + "\n" +
		            "  favorite color is " + favoriteColor + "\n" + 
		            "  execution count (from job map) is " + count + "\n" + 
		            "  execution count (from job member variable) is " + counter);
		  
		  count++;
		  data.put(EXECUTION_COUNT, count);
		  
		  counter++;
	}

}
