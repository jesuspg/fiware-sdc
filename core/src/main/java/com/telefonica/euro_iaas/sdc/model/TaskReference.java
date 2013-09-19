package com.telefonica.euro_iaas.sdc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskReference {
    @XmlAttribute(required=true)
    private String href;
    @XmlAttribute(required=false)
    private String name;
    @XmlAttribute(required=false, name="type")
    private String type;

    /**
     */
    public TaskReference() {
    }

    /**
     * @param href
     */
    public TaskReference(String href) {
        this.href = href;
    }
    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }
    /**
     * @param href the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }


}
