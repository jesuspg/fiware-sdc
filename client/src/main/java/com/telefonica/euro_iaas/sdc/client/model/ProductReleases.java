package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ProductRelease.class})
public class ProductReleases extends ArrayList<ProductRelease>{

      @XmlElement(name = "productRelease")
      public List<ProductRelease> getProductReleases() {
        return this;
      }
}
