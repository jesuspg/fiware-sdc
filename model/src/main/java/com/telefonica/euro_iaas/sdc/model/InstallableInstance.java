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

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * InstallableInstance represents an instance of an execution unit that is (or
 * has been) installed.
 * 
 * @author Sergio Arroyo
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InstallableInstance {

    public final static String VM_FIELD = "vm";
    public final static String STATUS_FIELD = "status";
    public final static String VDC_FIELD = "vdc";

    /**
     * Defines the value of the different status the Application could be. See
     * the diagram bellow to know the relations between the different states.
     * <img src=
     * "http://plantuml.com:80/plantuml/img/YzQALT3LjLFmp2ikISp9oSnBv-L2i96bKbFWCgafO8dGl4nCNJ2vWlIYn1Gi4ixvUMcPwQL5O2cuAdIBa5IXIo7RYkeC55ceTT5QiRnS65voBIhABq9t6LGGrKrGGNJtmDIYkmLT7DLeC0Lp5W00"
     * >
     */
    public enum Status {
        INSTALLING, INSTALLED, ERROR, UNINSTALLING, UNINSTALLED, CONFIGURING, UPGRADING, DEPLOYING_ARTEFACT, ARTIFACT_DEPLOYED, UNDEPLOYING_ARTIFACT, ARTIFACT_UNDEPLOYED
    };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private VM vm;

    private String vdc;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    /**
     * Constructor
     * 
     * @param status
     */
    public InstallableInstance() {
        this.date = new Date();
        this.status = Status.INSTALLING;
    }

    /**
     * Constructor
     * 
     * @param status
     */
    public InstallableInstance(Long id) {
        this();
        this.id = id;
    }

    /**
     * Constructor
     * 
     * @param status
     */
    public InstallableInstance(Status status) {
        this();
        this.status = status;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the vm
     */
    public VM getVm() {
        return vm;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InstallableInstance other = (InstallableInstance) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[InstallableInstance]");
        sb.append("[id = ").append(this.id).append("]");
        sb.append("[date = ").append(this.date).append("]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[status = ").append(this.status).append("]");
        sb.append("[vm = ").append(this.vm).append("]");
        sb.append("[vdc = ").append(this.vdc).append("]");
        sb.append("[attributes = ").append(this.attributes).append("]");
        sb.append("]");
        return sb.toString();
    }

}
