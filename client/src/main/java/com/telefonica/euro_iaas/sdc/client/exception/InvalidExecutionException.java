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

package com.telefonica.euro_iaas.sdc.client.exception;

/**
 * Exception thrown when a task does not ends in a success status.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class InvalidExecutionException extends Exception {

    /**
     *
     */
    public InvalidExecutionException() {
        super();
    }

    /**
     * @param arg0
     * @param arg1
     */
    public InvalidExecutionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public InvalidExecutionException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public InvalidExecutionException(Throwable arg0) {
        super(arg0);
    }

}
