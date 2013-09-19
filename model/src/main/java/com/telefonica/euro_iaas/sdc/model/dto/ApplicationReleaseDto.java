package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * DTO to receive rest request with the application releases objects.
 *
 * @author Jesus M. Movilla *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationReleaseDto {

	private String appName;
	private String appDescription;
	private String appType;
	private String version;
	private String releaseNotes;
	private List<Attribute> privateAttributes;
	private List<ProductRelease> supportedProducts;
	private List<ApplicationRelease> transitableReleases; 
	
	
	/**
     */
    public ApplicationReleaseDto() {
    }
    /**
     * @param applicationName
     * @param applicationDescription
     * @param applicationType
     * @param version
     * @param releaseNotes
     * @param privateAttributes
     * @param supportedProducts
     * @param transitableReleases
     */
    public ApplicationReleaseDto(String appName, String appDescription,
    		String appType,
    		String version, 
    		String releaseNotes,
    		List<Attribute> privateAttributes,
            List<ProductRelease> supportedProducts,
            List<ApplicationRelease> transitableReleases) {
    	this.appName = appName;
    	this.appDescription = appDescription;
    	this.appType = appType;
    	this.version = version;
    	this.releaseNotes = releaseNotes;
    	this.privateAttributes = privateAttributes;
    	this.supportedProducts = supportedProducts;
        this.transitableReleases = transitableReleases;
    }
	
    
    public String getApplicationName() {
		return appName;
	}
	public void setApplicationName(String appName) {
		this.appName = appName;
	}
	public String getApplicationDescription() {
		return appDescription;
	}
	public void setApplicationDescription(String appType) {
		this.appType = appType;
	}
	public String getApplicationType() {
		return appType;
	}
	public void setApplicationType(String appDescription) {
		this.appDescription = appDescription;
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
	public List<ProductRelease> getSupportedProducts() {
		return supportedProducts;
	}
	public void setSupportedProducts(List<ProductRelease> supportedProducts) {
		this.supportedProducts = supportedProducts;
	}
	public List<ApplicationRelease> getTransitableReleases() {
		return transitableReleases;
	}
	public void setTransitableReleases(List<ApplicationRelease> transitableReleases) {
		this.transitableReleases = transitableReleases;
	}
	
	
}
