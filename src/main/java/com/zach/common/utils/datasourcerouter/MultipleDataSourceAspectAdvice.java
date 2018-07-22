package com.zach.common.utils.datasourcerouter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/*import com.ydy.prescription.service.IRecipeWesternService;
import com.ydy.prescription.service.ISqlServerService;*/

@Component
@Aspect
public class MultipleDataSourceAspectAdvice {

    @Around("execution(* com.ydy.prescription.service..*.*(..))")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        /*if (jp.getTarget() instanceof IRecipeWesternService || jp.getTarget() instanceof ISqlServerService) {
            MultipleDataSource.setDataSourceKey("dataSourceOfOnlineHospital");
        }else {
            MultipleDataSource.setDataSourceKey("dataSource");
        }*/
    	MultipleDataSource.setDataSourceKey("dataSource");
        return jp.proceed();
    }
}