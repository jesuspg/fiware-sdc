package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * DTO to receive rest request with the product releases objects.
 * 
 * @author Jesus M. Movilla *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductReleaseDto {

	private String productName;
	private String productDescription;
	private String version;
	private String releaseNotes;
	private List<Attribute> privateAttributes;
	private List<OS> supportedOS;
	private List<ProductRelease> transitableReleases;

	public ProductReleaseDto() {
	}

	/**
	 * @param productName
	 * @param version
	 * @param releaseNotes
	 * @param privateAttributes
	 * @param supportedOS
	 * @param transitableReleases
	 */
	public ProductReleaseDto(String productName, String productDescription,
			String version, String releaseNotes,
			List<Attribute> privateAttributes, List<OS> supportedOS,
			List<ProductRelease> transitableReleases) {
		this.productName = productName;
		this.productDescription = productDescription;
		this.version = version;
		this.releaseNotes = releaseNotes;
		this.privateAttributes = privateAttributes;
		this.supportedOS = supportedOS;
		this.transitableReleases = transitableReleases;
	}

	public ProductReleaseDto(String product, String description,
			String version, Object object, Object object2,
			List<OS> supportedOOSS, Object object3) {
		// TODO Auto-generated constructor stub
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getReleaseNotes() {
		return releaseNotes;
	}

	public void setReleaseNotes(String releaseNotes) {
		this.releaseNotes = releaseNotes;
	}

	public List<Attribute> getPrivateAttributes() {
		return privateAttributes;
	}

	public void setPrivateAttributes(List<Attribute> privateAttributes) {
		this.privateAttributes = privateAttributes;
	}

	public List<OS> getSupportedOS() {
		return supportedOS;
	}

	public void setSupportedOS(List<OS> supportedOS) {
		this.supportedOS = supportedOS;
	}

	public List<ProductRelease> getTransitableReleases() {
		return transitableReleases;
	}

	public void setTransitableReleases(List<ProductRelease> transitableReleases) {
		this.transitableReleases = transitableReleases;
	}

}
