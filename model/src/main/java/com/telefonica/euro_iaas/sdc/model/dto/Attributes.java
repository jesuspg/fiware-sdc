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

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.telefonica.euro_iaas.sdc.model.Attribute;

/**
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ Attribute.class })
public class Attributes extends ArrayList<Attribute> {

    @XmlElement(name = "attribute")
    public List<Attribute> getAttributes() {
        return this;
    }

}
