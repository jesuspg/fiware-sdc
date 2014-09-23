package com.telefonica.euro_iaas.sdc.rest.aspects;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.Before;
import org.junit.Test;

public class TraceInterceptorTest {
	
	private TraceInterceptor traceInterceptor;
	
	
	
	@Before 
	public void setUp () {
		traceInterceptor = new TraceInterceptor();
	}

	@Test
	public void testWriteToLogLogStringThrowable() {
		Log log = LogFactory.getLog (TraceInterceptorTest.class);
		traceInterceptor.writeToLog(log, "message", new Exception ());
	}

	@Test
	public void testIsInterceptorEnabledMethodInvocationLog() {
		boolean result = traceInterceptor.isInterceptorEnabled(null, null);
		assertTrue (result);
	}

}
