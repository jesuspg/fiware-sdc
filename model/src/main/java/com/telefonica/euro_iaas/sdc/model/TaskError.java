/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
