package com.telefonica.euro_iaas.sdc.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Defines a concrete application running over a concrete product instance.
 *
 * @author Sergio Arroyo
 *
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstance extends InstallableInstance {

    public final static String APPLICATION_FIELD = "application";


    @ManyToOne(optional=false)
    private ApplicationRelease application;

    @ManyToMany
    private List<ProductInstance> products;


    /**
     * Default constructor.
     */
    public ApplicationInstance() {
        super();
    }
    /**
     * <p>Constructor for ApplicationInstance.</p>
     *
     * @param application a {@link com.telefonica.euro_iaas.sdc.model.Application} object.
     * @param products a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance} object.
     * @param status a {@link com.telefonica.euro_iaas.sdc.model.ApplicationInstance.Status} object.
     * @param vm the vm where the application is (or will be) installed
     */
    public ApplicationInstance(ApplicationRelease application,
            List<ProductInstance> products, Status status, VM vm, String vdc) {
        super(status);
        this.application = application;
        this.products = products;
        setVm(vm);
        setVdc(vdc);
    }
    /**
     * <p>Constructor for ApplicationInstance.</p>
     *
     * @param id a {@link java.lang.Long} object.
     */
    public ApplicationInstance(Long id) {
        super(id);
    }

   /**
     * <p>Getter for the field <code>application</code>.</p>
     *
     * @return the application
     */
    public ApplicationRelease getApplication() {
        return application;
    }

    /**
     * <p>Setter for the field <code>application</code>.</p>
     *
     * @param application the application to set
     */
    public void setApplication(ApplicationRelease application) {
        this.application = application;
    }

    /**
     * <p>Getter for the field <code>productInstance</code>.</p>
     *
     * @return the productInstance
     */
    public List<ProductInstance> getProducts() {
        return products;
    }

    /**
     * <p>Setter for the field <code>productInstance</code>.</p>
     *
     * @param productInstance the productInstance to set
     */
    public void setProducts(List<ProductInstance> products) {
        this.products = products;
    }

}
