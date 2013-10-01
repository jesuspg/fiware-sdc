/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.model;

import junit.framework.TestCase;

public class TasksTest extends TestCase {
    public static long ID = 1234;
    public static String MESSAGE = "Error message";
    public static String HREF = "href";

    /**
     * Test Task class
     * 
     * @return
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
