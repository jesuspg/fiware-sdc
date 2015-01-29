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

package com.telefonica.euro_iaas.sdc.model;

import junit.framework.TestCase;

public class TasksTest extends TestCase {
    private static long ID = 1234;
    private static String MESSAGE = "Error message";
    private static String HREF = "href";

    /**
     * Test Task class
     */

    public void testTask() {

        Task task = new Task();
        task.setDescription("description");
        task.setHref(HREF);
        task.setId(ID);
        task.setVdc("vdc");

        TaskReference taskReference = new TaskReference();
        taskReference.setHref(task.getHref());
        taskReference.setName(task.getDescription());
        taskReference.setType("type");

        TaskError taskerror = new TaskError();
        taskerror.setMessage(MESSAGE);
        task.setError(taskerror);

        assertEquals(task.getError().getMessage(), MESSAGE);
        assertEquals(taskReference.getHref(), HREF);

    }

}
