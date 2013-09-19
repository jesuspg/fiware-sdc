package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.Attribute;
/**
 *
 * @author Sergio Arroyo
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({Attribute.class})
public class Attributes extends ArrayList<Attribute> {

      @XmlElement(name = "attribute")
      public List<Attribute> getAttributes() {
        return this;
      }

}
