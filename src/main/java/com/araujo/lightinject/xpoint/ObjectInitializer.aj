package com.araujo.lightinject.xpoint;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ObjectInitializer {

	@Pointcut("initialization(*.new(..)) && !within(ObjectInitializer)")
	public void event() {
	}

	@Before("event()")
	public void doEvent(JoinPoint joinPoint) throws Throwable {
		com.araujo.lightinject.di.BindingConfiguration.inject(joinPoint.getTarget());
	}
}
