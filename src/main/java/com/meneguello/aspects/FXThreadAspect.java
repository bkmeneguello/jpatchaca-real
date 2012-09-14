package com.meneguello.aspects;

import javafx.application.Platform;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class FXThreadAspect {
	
	@Pointcut("execution(@com.meneguello.RunLater * *(..))")
	public void runLaterAnnotatedMethod() {};
	
	@Pointcut("execution(void *(..))")
	public void runLaterMethodCandidate() {};
	
	@DeclareError("runLaterAnnotatedMethod() && !runLaterMethodCandidate()")
	static final String invalidRunLaterMethod = "Invalid run later method";

	@Around("runLaterAnnotatedMethod() && runLaterMethodCandidate()")
	public void around(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
		if (Platform.isFxApplicationThread()) {
			thisJoinPoint.proceed();
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						thisJoinPoint.proceed();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
}
