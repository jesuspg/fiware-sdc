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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Defines a product that is installed in the system.
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "ProductInstance")
public class ProductInstance extends InstallableInstance implements Comparable<ProductInstance> {

    public final static String PRODUCT_FIELD = "productRelease";
    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // @XmlTransient
    // private Long id;

    @ManyToOne(optional = false)
    private ProductRelease productRelease;

    // @OneToMany(cascade=CascadeType.ALL)
    // @OneToMany(targetEntity = ProductInstance.class, cascade=CascadeType.ALL)
    // private List<Artifact> artifacts;

    /*
     * @Id
     * @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
     * @ManyToOne(optional=false) private ProductRelease productRelease;
     */
    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "productInstance",
    // fetch = FetchType.LAZY)
    @OneToMany(targetEntity = Artifact.class, mappedBy = "productInstance", fetch = FetchType.LAZY)
    // cascade = CascadeType.ALL
    private Set<Artifact> artifact;

    /**
     * <p>
     * Constructor for ProductInstance.
     * </p>
     */
    public ProductInstance() {
        super();
        this.artifact = new HashSet<Artifact>();
    }

    /**
     * <p>
     * Constructor for ProductInstance.
     * </p>
     * 
     * @param id
     *            the id
     */
    public ProductInstance(Long id) {
        super(id);
    }

    /**
     * <p>
     * Constructor for ProductInstance.
     * </p>
     * 
     * @param application
     *            a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
     * @param status
     *            a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance.Status} object.
     */
    public ProductInstance(ProductRelease productRelease, Status status, VM vm, String vdc) {
        super(status);
        this.productRelease = productRelease;
        setVm(vm);
        setVdc(vdc);
        this.artifact = new HashSet<Artifact>();
    }

    /**
     * <p>
     * Constructor for ProductInstance.
     * </p>
     * 
     * @param application
     *            a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
     * @param status
     *            a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance.Status} object.
     */
    public ProductInstance(ProductRelease productRelease, Set<Artifact> artifacts, Status status, VM vm, String vdc) {
        super(status);
        this.productRelease = productRelease;
        this.artifact = artifacts;
        setVm(vm);
        setVdc(vdc);
        this.artifact = new HashSet<Artifact>();
    }

    /**
     * <p>
     * Getter for the field <code>product</code>.
     * </p>
     * 
     * @return the application
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * <p>
     * Setter for the field <code>product</code>.
     * </p>
     * 
     * @param prodcut
     *            the product to set
     */
    public void setProductRelease(ProductRelease product) {
        this.productRelease = product;
    }

    /**
     * <p>
     * Getter for the field <code>artifacts</code>.
     * </p>
     * 
     * @return the list of artifact
     */

    public Set<Artifact> getArtifacts() {
        return artifact;
    }

    /**
     * <p>
     * Setter for the field <code>artifacts</code>.
     * </p>
     * 
     * @param the
     *            list of artifact
     */
    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifact = artifacts;
    }

    /**
     * <p>
     * Setter for the field <code>artifacts</code>.
     * </p>
     * 
     * @param the
     *            list of artifact
     */
    public void addArtifact(Artifact artifact) {
        if (this.artifact == null) {
            this.artifact = new HashSet<Artifact>();
        }

        this.artifact.add(artifact);
    }

    /**
     * <p>
     * Setter for the field <code>artifacts</code>.
     * </p>
     * 
     * @param the
     *            list of artifact
     */
    public void deleteArtifact(Artifact artifact) {
        this.artifact.remove(artifact);
    }

    public int compareTo(ProductInstance o) {
        return this.getProductRelease().getProduct().getName().compareTo(o.getProductRelease().getProduct().getName());
    }

}
