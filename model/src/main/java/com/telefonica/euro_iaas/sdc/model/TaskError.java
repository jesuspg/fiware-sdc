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

package com.telefonica.euro_iaas.sdc.model;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines a message in a task
 * 
 * @author Sergio Arroyo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskError {

    @XmlAttribute(required = true)
    @Column(length = 1024)
    private String message;
    @XmlAttribute(required = false)
    @Column(length = 1024)
    private String majorErrorCode;
    @XmlAttribute(required = false)
    private String minorErrorCode;
    @XmlAttribute(required = false)
    private String venodrSpecificErrorCode;

    /**
     */
    public TaskError() {
    }

    /**
     * @param message
     */
    public TaskError(String message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the majorErrorCode
     */
    public String getMajorErrorCode() {
        return majorErrorCode;
    }

    /**
     * @param majorErrorCode
     *            the majorErrorCode to set
     */
    public void setMajorErrorCode(String majorErrorCode) {
        this.majorErrorCode = majorErrorCode;
    }

    /**
     * @return the minorErrorCode
     */
    public String getMinorErrorCode() {
        return minorErrorCode;
    }

    /**
     * @param minorErrorCode
     *            the minorErrorCode to set
     */
    public void setMinorErrorCode(String minorErrorCode) {
        this.minorErrorCode = minorErrorCode;
    }

    /**
     * @return the venodrSpecificErrorCode
     */
    public String getVenodrSpecificErrorCode() {
        return venodrSpecificErrorCode;
    }

    /**
     * @param venodrSpecificErrorCode
     *            the venodrSpecificErrorCode to set
     */
    public void setVenodrSpecificErrorCode(String venodrSpecificErrorCode) {
        this.venodrSpecificErrorCode = venodrSpecificErrorCode;
    }

}
