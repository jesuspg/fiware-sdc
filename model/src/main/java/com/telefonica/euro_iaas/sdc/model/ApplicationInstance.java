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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Defines a concrete application running over a concrete product instance.
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstance extends InstallableInstance {

    public final static String APPLICATION_FIELD = "application";

    @ManyToOne(optional = false)
    private ApplicationRelease application;

    @OneToOne
    // private List<ProductInstance> products;
    private EnvironmentInstance environmentInstance;

    /**
     * Default constructor.
     */
    public ApplicationInstance() {
        super();
    }

    /**
     * <p>
     * Constructor for ApplicationInstance.
     * </p>
     * 
     * @param application
     *            a {@link com.telefonica.euro_iaas.sdc.model.Application} object.
     * @param environmentInstance
     *            a {@link com.telefonica.euro_iaas.sdc.model.EnvironmentInstance} object.
     * @param status
     *            a {@link com.telefonica.euro_iaas.sdc.model.ApplicationInstance.Status} object.
     * @param vm
     *            the vm where the application is (or will be) installed
     */
    public ApplicationInstance(ApplicationRelease application, EnvironmentInstance environmentInstance, Status status,
            VM vm, String vdc) {
        super(status);
        this.application = application;
        this.environmentInstance = environmentInstance;
        setVm(vm);
        setVdc(vdc);
    }

    /**
     * <p>
     * Constructor for ApplicationInstance.
     * </p>
     * 
     * @param id
     *            a {@link java.lang.Long} object.
     */
    public ApplicationInstance(Long id) {
        super(id);
    }

    /**
     * <p>
     * Getter for the field <code>application</code>.
     * </p>
     * 
     * @return the application
     */
    public ApplicationRelease getApplication() {
        return application;
    }

    /**
     * <p>
     * Setter for the field <code>application</code>.
     * </p>
     * 
     * @param application
     *            the application to set
     */
    public void setApplication(ApplicationRelease application) {
        this.application = application;
    }

    /**
     * <p>
     * Getter for the field <code>environmentInstance</code>.
     * </p>
     * 
     * @return the environmentInstance
     */
    public EnvironmentInstance getEnvironmentInstance() {
        return environmentInstance;
    }

    /**
     * <p>
     * Setter for the field <code>environmentInstance</code>.
     * </p>
     * 
     * @param environmentInstance
     *            the environmentInstance to set
     */
    public void setEnvironmentInstance(EnvironmentInstance environmentInstance) {
        this.environmentInstance = environmentInstance;
    }

}
