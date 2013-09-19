package com.telefonica.euro_iaas.sdc.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines an environment installed in the system.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(optional = false)
	private Environment environment;

	@OneToMany
	private List<ProductInstance> productInstances;

	/**
	 * <p>
	 * Constructor for EnvironmentInstance.
	 * </p>
	 */
	public EnvironmentInstance() {
		super();
	}

	/**
	 * <p>
	 * Constructor for EnvironmentInstance.
	 * </p>
	 * 
	 * @param application
	 *            a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
	 * @param productInstances
	 *            a {@link List
	 *            <com.telefonica.euro_iaas.sdc.model.ProductInstance>} object.
	 */
	public EnvironmentInstance(Environment environment,
			List<ProductInstance> productInstances) {
		this.environment = environment;
		this.productInstances = productInstances;
	}

	/**
	 * <p>
	 * Getter for the field <code>environment</code>.
	 * </p>
	 * 
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * <p>
	 * Setter for the field <code>environment</code>.
	 * </p>
	 * 
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * <p>
	 * Getter for the field <code>productInstances</code>.
	 * </p>
	 * 
	 * @return the productInstances
	 */
	public List<ProductInstance> getProductInstances() {
		return productInstances;
	}

	/**
	 * <p>
	 * Setter for the field <code>productReleases</code>.
	 * </p>
	 * 
	 * @param productReleases
	 *            the productReleases to set
	 */

	public void setProductInstances(List<ProductInstance> productInstances) {
		this.productInstances = productInstances;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

}
