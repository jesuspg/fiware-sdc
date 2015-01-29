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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.InheritanceType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Inheritance;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.EntityNotFoundException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Installable means executable units that can be installed, uninstalled, configured or updated in a VM.
 * 
 * @author Sergio Arroyo
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;

    // commented out due to problems in updateProductRelease in Catalog
    // Management
    /*
     * @SuppressWarnings("unused")
     * @Version
     * @XmlTransient private Long v;
     */

    @Column(unique = true, nullable = false, length = 256)
    private String name;
    @Column(length = 2048)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Metadata> metadatas;

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

    /**
     * @return the metadatas
     */
    public List<Metadata> getMetadatas() {
        return metadatas;
    }

    /**
     * @param metadatas
     *            the metadatas to set
     */
    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = metadatas;
    }

    /**
     * Add a new attribute.
     * 
     * @param attribute
     *            the attribute
     */
    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(attribute);
    }

    /**
     * Add a new metadata.
     * @param meta
     *           the metadata
     */
    public void addMetadata(Metadata meta) {
        if (metadatas == null) {
            metadatas = new ArrayList<Metadata>();
        }
        metadatas.add(meta);
    }

    /**
     * It gets the metadata selected.
     * @param metadataName: The metadata name
     * @return the metadata
     */
    public Metadata getMetadata(String metadataName) throws EntityNotFoundException {
        Metadata aux = null;
        for (Metadata meta: this.metadatas) {
            if (meta.getKey().equals(metadataName)) {
                aux = meta;
            }
        }
        if (aux == null) {
            throw new EntityNotFoundException("Not metadata found: " + metadataName);
        }
        return aux;
    }

    /**
     * It get the attribute selected.
     * @param attributeName: the attribute name
     * @return the attribute
     */
    public Attribute getAttribute(String attributeName) throws EntityNotFoundException {
        Attribute aux = null;
        for (Attribute att: this.attributes) {
            if (att.getKey().equals(attributeName)) {
                aux = att;
            }
        }
        if (aux == null) {
            throw new EntityNotFoundException("Not attribute found: " + attributeName);
        }
        return aux;
    }

    /**
     * It deletes the metadataName in the product.
     * @param metadataName: the metadata to be deleted
     */
    public void deleteMetadata(String metadataName) throws EntityNotFoundException {
        metadatas.remove(getMetadata(metadataName));
    }

    /**
     * It deletes the attributeName in the product.
     * @param attributeName: the attribute to be deleted
     */
    public void deleteAttribute(String attributeName) throws EntityNotFoundException  {
        attributes.remove(getAttribute(attributeName));
    }

    /**
     * It update a metadata value in a metadata.
     * @param metadata: the metadata.
     * @return
     */
    public void updateMetadata(Metadata metadata) throws EntityNotFoundException {

        if (metadata.getDescription() != null) {
            this.getMetadata(metadata.getKey()).setDescription(metadata.getDescription());
        }
        this.getMetadata(metadata.getKey()).setValue(metadata.getValue());

    }

    /**
     * It update a attribute value in a metadata.
     * @param attribute: the attribute to be uploaded.
     */
    public void updateAttribute(Attribute attribute) {
        if (this.getAttribute(attribute.getKey()) == null) {
            return;
        }
        if (attribute.getDescription() != null) {
            this.getAttribute(attribute.getKey()).setDescription(attribute.getDescription());
        }
        if (attribute.getType() != null) {
            this.getAttribute(attribute.getKey()).setValue(attribute.getValue());
        }
        this.getAttribute(attribute.getKey()).setValue(attribute.getValue());
    }


    /**
     * It obtains the attributes map.
     * @return the attributes as a Map
     */
    public Map<String, String> getMapAttributes() {
        Map<String, String> atts = new HashMap<String, String>();
        for (Attribute att : attributes) {
            atts.put(att.getKey(), att.getValue());
        }
        return atts;
    }

    /**
     * It obtains the metadata map.
     * @return the metadatas as a Map
     */
    public Map<String, String> getMapMetadata() {
        Map<String, String> mets = new HashMap<String, String>();
        for (Metadata metadata : metadatas) {
            mets.put(metadata.getKey(), metadata.getValue());
        }
        return mets;
    }

    /**
     * Constructor.
     */
    public Product() {
        attributes = new ArrayList<Attribute>();
        metadatas = new ArrayList<Metadata>();
    }

    /**
     * Constructor.
     * 
     * @param name: the product name
     * @param description: the description.
     */
    public Product(String name, String description) {
        this.name = name;
        this.description = description;
        attributes = new ArrayList<Attribute>();
        metadatas = new ArrayList<Metadata>();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
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
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[Product]");
        sb.append("[id = ").append(this.id).append("]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[description = ").append(this.description).append("]");
        sb.append("[attributes = ").append(this.attributes).append("]");
        sb.append("[metadatas = ").append(this.metadatas).append("]");
        sb.append("]");
        return sb.toString();
    }

}
