package com.zach.quartz.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zach.quartz.entity.ScheduleJob;
import com.zach.quartz.mapper.ScheduleJobMapper;

/**
 * 
 * 项目启动进行任务初始化
 * 
 * @author 章生志
 * @date 2018年7月3日
 * @title ScheduleJobInit
 */
@Component
public class ScheduleJobInit {

	@Autowired
	private ScheduleJobMapper scheduleJobMapper;

	@Autowired
	private JobTaskService jobTaskService;

	@Value("${initJob}")
	private String initJob;
	public static final Logger log = LoggerFactory.getLogger("bizDataLogger");

	/**
	 * 初始化方法
	 * 
	 * void
	 *
	 */
	@PostConstruct
	public void init() {
		log.info("开始任务初始化了=============================");

		// 这里获取任务信息数据
		List<ScheduleJob> jobList = scheduleJobMapper.select(null);
		
		if (jobList == null || jobList.size() == 0) {
			scheduleJobMapper.addScheduleJob();
			jobList = scheduleJobMapper.select(null);
		}
		
		/*Connection con = null;
		if (jobList == null || jobList.size() == 0) {
			log.info("数据库中任务数量为零,开始向任务表task_schedule_job中添加任务=============================");
			PreparedStatement prepareStatement = null;
			try {
				con = DBUtil.getInitConection();
				con.setAutoCommit(false);
				prepareStatement = con.prepareStatement(initJob);
				prepareStatement.executeUpdate();
				con.commit();

			} catch (Exception e) {
				log.info("初始化失败,开始回滚");
				DBUtil.rollback();
			} finally {
				// 关闭资源

				try {
					if (prepareStatement != null && !prepareStatement.isClosed()) {
						prepareStatement.close();
					}
				} catch (SQLException e) {
					log.error(e.getMessage());
				}

				// 关闭连接
				DBUtil.close();
			}
		}
*/
		// 添加任务到quartz中
		for (ScheduleJob job : jobList) {
			jobTaskService.addJob(job);
		}

		log.info("任务初始化结束了=============================");
	}

	
}
