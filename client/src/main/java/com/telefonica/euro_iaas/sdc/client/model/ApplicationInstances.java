package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso( { ApplicationInstance.class })
public class ApplicationInstances extends ArrayList<ApplicationInstance> {

	@XmlElement(name = "applicationInstance")
	public List<ApplicationInstance> getApplicationInstances() {
		return this;
	}
}
