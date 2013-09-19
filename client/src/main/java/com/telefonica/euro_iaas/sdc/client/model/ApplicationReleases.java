package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ApplicationRelease.class})
public class ApplicationReleases extends ArrayList<ApplicationRelease>{

      @XmlElement(name = "applicationRelease")
      public List<ApplicationRelease> getProductReleases() {
        return this;
      }
}
