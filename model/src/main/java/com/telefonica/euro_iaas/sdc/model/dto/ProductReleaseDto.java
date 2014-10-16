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

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
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
    // private List<Attribute> privateAttributes;
    private List<Metadata> metadatas;
    private List<OS> supportedOS;
    private List<ProductRelease> transitableReleases;

    public ProductReleaseDto() {
    }

    /**
     * @param productName
     * @param version
     * @param releaseNotes
     * @param privateAttributes
     * @param metadatas
     * @param supportedOS
     * @param transitableReleases
     */
    /*
     * public ProductReleaseDto(String productName, String productDescription,
     * String version, String releaseNotes, List<Attribute> privateAttributes,
     * List<Metadata> metadatas, List<OS> supportedOS, List<ProductRelease>
     * transitableReleases) {
     */
    public ProductReleaseDto(String productName, String productDescription, String version, String releaseNotes,
            List<Attribute> privateAttributes, List<Metadata> metadatas, List<OS> supportedOS,
            List<ProductRelease> transitableReleases) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.version = version;
        this.releaseNotes = releaseNotes;
        // this.privateAttributes = privateAttributes;
        this.metadatas = metadatas;
        this.supportedOS = supportedOS;
        this.transitableReleases = transitableReleases;
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

    /*
     * public List<Attribute> getPrivateAttributes() { return privateAttributes;
     * }
     * 
     * public void setPrivateAttributes(List<Attribute> privateAttributes) {
     * this.privateAttributes = privateAttributes; }
     */

    public List<Metadata> getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = metadatas;
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

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ProductReleaseDto]");
        sb.append("[productName = ").append(this.productName).append("]");
        sb.append("[productDescription = ").append(this.productDescription).append("]");
        sb.append("[version = ").append(this.version).append("]");
        sb.append("[releaseNotes = ").append(this.releaseNotes).append("]");
        sb.append("[metadatas = ").append(this.metadatas).append("]");
        sb.append("[supportedOS = ").append(this.supportedOS).append("]");
        sb.append("[transitableReleases = ").append(this.transitableReleases).append("]");
        sb.append("]");
        return sb.toString();
    }

}
