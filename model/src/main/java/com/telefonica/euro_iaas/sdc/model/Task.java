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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Defines the object return in an asynchronous request to the system. Provides
 * some information to know the result of the task and how to find the created
 * resource.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Serializable {
    public enum TaskStates {
        QUEUED, PENDING, RUNNING, SUCCESS, ERROR, CANCELLED
    };

    @Id
    @GeneratedValue
    @XmlTransient
    private long id;

    @XmlAttribute
    @Transient
    private String href;

    @XmlElement(required = false)
    @Embedded
    private TaskError error;
    @XmlElement(required = false)
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "href", column = @Column(name = "owner_href")),
            @AttributeOverride(name = "name", column = @Column(name = "owner_name")),
            @AttributeOverride(name = "type", column = @Column(name = "owner_type")) })
    private TaskReference owner;
    @XmlElement(required = false)
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "href", column = @Column(name = "result_href")),
            @AttributeOverride(name = "name", column = @Column(name = "result_name")),
            @AttributeOverride(name = "type", column = @Column(name = "result_type")) })
    private TaskReference result;

    @XmlAttribute(required = true)
    private Date startTime;
    @XmlAttribute(required = false)
    private Date endTime;
    @XmlAttribute(required = false)
    private Long expireTime;

    @XmlAttribute(required = true)
    private TaskStates status;

    @XmlElement(required = false)
    @Column(length = 1024)
    private String description;

    @XmlElement(required = false)
    @Column(length = 1024)
    private String vdc;

    /**
     * @param href
     */
    public Task() {
        this.startTime = new Date();
    }

    /**
     * @param href
     */
    public Task(TaskStates status) {
        this.status = status;
        this.startTime = new Date();
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @return the error
     */
    public TaskError getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(TaskError error) {
        this.error = error;
    }

    /**
     * @return the owner
     */
    public TaskReference getOwner() {
        return owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(TaskReference owner) {
        this.owner = owner;
    }

    /**
     * @return the result
     */
    public TaskReference getResult() {
        return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(TaskReference result) {
        this.result = result;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the expireTime
     */
    public Long getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime
     *            the expireTime to set
     */
    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * @return the status
     */
    public TaskStates getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(TaskStates status) {
        this.status = status;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[Task]");
        sb.append("[id = ").append(this.id).append("]");
        sb.append("[href = ").append(this.href).append("]");
        sb.append("[error = ").append(this.error).append("]");
        sb.append("[owner = ").append(this.owner).append("]");
        sb.append("[result = ").append(this.result).append("]");
        sb.append("[startTime = ").append(this.startTime).append("]");
        sb.append("[endTime = ").append(this.endTime).append("]");
        sb.append("[expireTime = ").append(this.expireTime).append("]");
        sb.append("[status = ").append(this.status).append("]");
        sb.append("[description = ").append(this.description).append("]");
        sb.append("[vdc = ").append(this.vdc).append("]");
        sb.append("]");
        return sb.toString();
    }

}
