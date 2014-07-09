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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class NodeCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SuppressWarnings("unused")
    @Version
    private Long v;

    @ManyToOne
    private OS os;
    @Column(nullable = false, length = 128)
    private String key;
    @Column(nullable = false, length = 128)
    private String value;

    /**
     * Default constructor
     */
    public NodeCommand() {

    }

    /**
     * <p>
     * Constructor for NodeCommand.
     * </p>
     * 
     * @param os
     *            a {@link com.telefonica.euro_iaas.sdc.model.OS} object.
     * @param key
     *            a {@link java.lang.String} object.
     * @param value
     *            a {@link java.lang.String} object.
     */
    public NodeCommand(OS os, String key, String value) {
        this.os = os;
        this.key = key;
        this.value = value;
    }

    /**
     * <p>
     * Getter for the field <code>os</code>.
     * </p>
     * 
     * @return the os
     */
    public OS getOS() {
        return os;
    }

    /**
     * <p>
     * Setter for the field <code>os</code>.
     * </p>
     * 
     * @param os
     *            the os to set
     */
    public void setOS(OS os) {
        this.os = os;
    }

    /**
     * <p>
     * Getter for the field <code>key</code>.
     * </p>
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>
     * Setter for the field <code>key</code>.
     * </p>
     * 
     * @param name
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * <p>
     * Getter for the field <code>value</code>.
     * </p>
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>
     * Setter for the field <code>value</code>.
     * </p>
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((os == null) ? 0 : os.hashCode());
        result = prime * result + ((v == null) ? 0 : v.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        NodeCommand other = (NodeCommand) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (os == null) {
            if (other.os != null)
                return false;
        } else if (!os.equals(other.os))
            return false;
        if (v == null) {
            if (other.v != null)
                return false;
        } else if (!v.equals(other.v))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[NodeCommand]");
        sb.append("[id = ").append(this.id).append("]");
        sb.append("[v = ").append(this.v).append("]");
        sb.append("[os = ").append(this.os).append("]");
        sb.append("[key = ").append(this.key).append("]");
        sb.append("[value = ").append(this.value).append("]");
        sb.append("]");
        return sb.toString();
    }

}
