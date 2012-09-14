package com.meneguello.aspects;

import java.util.concurrent.Future;

import javafx.concurrent.Task;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.meneguello.Async;
import com.meneguello.TaskExecutor;

@Aspect
public class AsyncAspect {

	private final TaskExecutor taskExecutor = TaskExecutor.getInstance();
	
	@Pointcut("@annotation(async) && execution(* *(..))")
	public void asyncAnnotatedMethod(Async async) {};
	
	@Pointcut("execution((void||java.util.concurrent.Future) *(..))")
	public void asyncMethodCandidate() {};
	
	@DeclareError("asyncAnnotatedMethod(com.meneguello.Async) && !asyncMethodCandidate()")
	static final String invalidAsyncMethod = "Invalid asynchronous method";
	
	@Around("asyncAnnotatedMethod(async) && asyncMethodCandidate()")
	public Object around(final ProceedingJoinPoint thisJoinPoint, Async async) throws Throwable {
		final String queue = async.queue();
		final Task<Object> task = taskExecutor.execute(new Task<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					final Object result = thisJoinPoint.proceed();
					if (result instanceof Future) {
						return result;
					}
					return null;
				} catch (Throwable e) {
					throw new Exception(e);
				}
			}
		}, queue);
		
		if (Future.class.isAssignableFrom(((MethodSignature) thisJoinPoint.getSignature()).getReturnType())) {
			return task;
		}
		return null;
	}
	
}
