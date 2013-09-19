package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.Task;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso( { Task.class })
public class Tasks extends ArrayList<Task> {

	@XmlElement(name = "task")
	public List<Task> getTasks() {
		return this;
	}
}
