package com.meneguello.jira;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RemoteJiraAspect {

	@Around("execution(private * com.meneguello.jira.Jira._jira*(..)) && this(jira)")
	public Object around(final ProceedingJoinPoint thisJoinPoint, Jira jira) throws Throwable {
		try {
			jira.beginRequest();
			return thisJoinPoint.proceed();
		} finally {
			jira.finishRequest();
		}
	}
	
}
