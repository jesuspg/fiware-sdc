package com.telefonica.euro_iaas.sdc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.ProductInstance;

@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso( { ProductInstance.class })
public class ProductInstances extends ArrayList<ProductInstance> {

	@XmlElement(name = "productInstance")
	public List<ProductInstance> getProductInstances() {
		return this;
	}
}
