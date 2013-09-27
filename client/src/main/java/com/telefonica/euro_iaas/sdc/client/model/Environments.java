package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.Environment;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ Environment.class })
public class Environments extends ArrayList<Environment> {

    @XmlElement(name = "environment")
    public List<Environment> getEnvironments() {
        return this;
    }
}
