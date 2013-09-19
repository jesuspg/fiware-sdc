package com.telefonica.euro_iaas.sdc.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Represents a set of ProductReleases over which an application release can
 * can be installed
 * @author Jesus M. Movilla
 *
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Environment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	 
	@ManyToMany
	private List<ProductRelease> productReleases;
	 
	@Column(unique=true, nullable=false, length=64)
	private String name;
	 
	@Column(length=64)
	private String description;

	/**
     * Constructor of the class.
     */
	public Environment() {
	}
	
	/**
     * Constructor of the class.
     * @param name
     * @param productReleases
     */
	public Environment(List<ProductRelease> productReleases) {
		this.name = getNameFromProductReleaseList(productReleases);
		this.productReleases = productReleases;
	}

	 /**
	  * @return the productReleases
	 */
	 public List<ProductRelease> getProductReleases() {
		 return productReleases;
	 }

	 /**
	 * @param the productReleases to set
	 */
	 public void setProductReleases(List<ProductRelease> productReleases) {
		 this.productReleases = productReleases;
	 }

	 /**
	  * @return the name
	 */
	 public String getName() {
		 return name;
	 }

	 /**
	 * @param the name to set
	 */
	 public void setName(String name) {
		 this.name = name;
	 }

	 /**
	  * @return the description
	 */
	 public String getDescription() {
		 return description;
	 }

	 /**
	 * @param the description to set
	 */
	 public void setDescription(String description) {
		 this.description = description;
	 }

	 
	 @Override
	 public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	 }

	 @Override
	 public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Environment other = (Environment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	 }
	 
	 private String getNameFromProductReleaseList(List<ProductRelease> productReleases){
		 String name = "";
		 for (ProductRelease productRelease: productReleases){
			 name = name + productRelease.getProduct().getName() + "-"
					 + productRelease.getVersion() + "_";
		 }
		 return name;
	 }
 
}
