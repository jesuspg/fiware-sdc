package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;

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
