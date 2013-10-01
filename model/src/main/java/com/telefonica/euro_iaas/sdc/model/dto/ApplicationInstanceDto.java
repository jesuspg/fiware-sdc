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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.Attribute;

/**
 * DTO for application Instance to receive rest request
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstanceDto {

    private String applicationName;
    private String version;
    private VM vm;
    // private List<ReleaseDto> products;
    private EnvironmentInstanceDto environmentInstanceDto;
    private List<Attribute> attributes;

    /**
     */
    public ApplicationInstanceDto() {
    }

    /**
     * @param applicationName
     * @param vm
     * @param environmentInstace
     */
    public ApplicationInstanceDto(String applicationName, String version, VM vm,
            EnvironmentInstanceDto environmentInstanceDto) {
        this.version = version;
        this.applicationName = applicationName;
        this.vm = vm;
        this.environmentInstanceDto = environmentInstanceDto;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName
     *            the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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
     * @return the environmentInstanceDto
     */
    public EnvironmentInstanceDto getEnvironmentInstanceDto() {
        return environmentInstanceDto;
    }

    /**
     * @param environmentInstanceDto
     *            the environmentInstanceDto to set
     */
    public void setEnvironmentInstance(EnvironmentInstanceDto environmentInstanceDto) {
        this.environmentInstanceDto = environmentInstanceDto;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
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

}
