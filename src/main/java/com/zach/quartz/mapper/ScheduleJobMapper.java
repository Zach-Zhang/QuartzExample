package com.zach.quartz.mapper;

import com.zach.quartz.entity.ScheduleJob;

import tk.mybatis.mapper.common.Mapper;

public interface ScheduleJobMapper extends Mapper<ScheduleJob>{
	/**
	 * 添加定时任务到数据库中
	 * 
	 * void
	 *
	 */
	void addScheduleJob();

}
