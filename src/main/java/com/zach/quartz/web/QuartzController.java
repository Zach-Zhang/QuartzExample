package com.zach.quartz.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zach.common.utils.AppResponse;
import com.zach.quartz.entity.ScheduleJob;
import com.zach.quartz.service.JobTaskService;

@Controller
@RequestMapping("/QuartzExample")
public class QuartzController {
	
	@Autowired
	private JobTaskService jobTaskService;
	
	@RequestMapping("/addJob")
	@ResponseBody
	public void addJob(ScheduleJob scheduleJob){
		jobTaskService.addJob(scheduleJob);
	}
	
	@RequestMapping("/addTask")
	@ResponseBody
	public void addTask(ScheduleJob job){
		jobTaskService.addTask(job);
	}
	
	@RequestMapping("/runAJobNow")
	@ResponseBody
	public AppResponse runAJobNow(ScheduleJob scheduleJob){
		AppResponse appResponse = new AppResponse();
		try {
			jobTaskService.resumeJob(scheduleJob);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			appResponse.setFail("开启任务失败");
		}
		
		return appResponse;
	}
}
