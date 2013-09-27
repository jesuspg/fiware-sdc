package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ EnvironmentInstance.class })
public class EnvironmentInstances extends ArrayList<EnvironmentInstance> {

    @XmlElement(name = "environmentInstance")
    public List<EnvironmentInstance> getEnvironmentInstances() {
        return this;
    }
}
