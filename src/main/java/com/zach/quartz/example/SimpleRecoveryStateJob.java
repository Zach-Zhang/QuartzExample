package com.zach.quartz.example;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SimpleRecoveryStateJob extends SimpleRecoveryJob{
	
	public SimpleRecoveryStateJob() {
		super();
	}
}
