package com.porkerspicks.ggs.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;

@Aspect
@Component
public class RestControllerAspect {
	
	private final Logger logger = LoggerFactory.getLogger("RestControllerAspect");
	
	@Autowired
	Counter createdBetCounter;

	@Before("execution(public * com.porkerspicks.ggs.web.*Controller.*(..))")
	public void generalAllMethodASpect() {
		logger.info("All Method Calls invoke this general aspect method");

	}

	@AfterReturning("execution(public * com.porkerspicks.ggs.web.*Controller.addBet(..))")
	public void getsCalledOnAddBet() {
		logger.info("This aspect is fired when the addBet method of the controller is called");
		createdBetCounter.increment();
	}
}
