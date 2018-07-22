package com.zach.quartz.service;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component("taskExample")
public class TaskExample {
	
	public void test(JobExecutionContext context){
		System.out.println("hello,world"+new Date());
	}
}
