/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.rest.aspects;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertTrue;

/**
 * Created by fla on 21/11/14.
 */
public class TraceInterceptorTest {

    /**
     * Check the aspect when a exception type info is launched.
     * @throws Exception
     */
    @org.junit.Test
    public void writeToLogwithENTERMessage() throws Exception {
        TraceInterceptor ti = new TraceInterceptor();
        Log log = mock(Log.class);
        log.info(anyString());

        ti.writeToLog(log, "is ENTER msg", null);

        verify(log).info("is ENTER msg");
    }

    /**
     * Check the aspect when a exception type debug is launched.
     * @throws Exception
     */
    @org.junit.Test
    public void writeToLogwithEXITMessage() throws Exception {
        TraceInterceptor ti = new TraceInterceptor();
        Log log = mock(Log.class);
        log.debug(anyString());

        ti.writeToLog(log, "is EXIT msg", null);

        verify(log).debug("is EXIT msg");
    }

    /**
     * Check the aspect when a exception type error is launched.
     * @throws Exception
     */
    @org.junit.Test
    public void writeToLogwithNullException() throws Exception {
        TraceInterceptor ti = new TraceInterceptor();
        Log log = mock(Log.class);
        log.error(anyString());
        Throwable ex = mock(Throwable.class);

        ti.writeToLog(log, "is any msg", ex);

        verify(log).error("");
    }

    /**
     * Check that the interceptor is enabled.
     * @throws Exception
     */
    @org.junit.Test
    public void testIsInterceptorEnabled() throws Exception {
        TraceInterceptor ti = new TraceInterceptor();
        Log log = mock(Log.class);
        MethodInvocation in = mock(MethodInvocation.class);

        assertTrue(ti.isInterceptorEnabled(in, log));
    }

}
