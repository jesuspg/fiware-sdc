/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.data;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="nodes")
public class Node {

    private String eol = System.getProperty("line.separator");

    private String id;
    private String groupName;
    private List<Software> softwareList = new ArrayList<Software>();
    private boolean manifestGenerated=false;

    public Node() {

    }

    public String getId() {
        return id;
    }

    public void setId(String name) {
        this.id = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Software getSoftware(String softName) {
        Software result = null;
        for (Software soft : softwareList) {
            if (soft.getName().equals(softName)) {
                result = soft;
                break;
            }
        }
        if (result == null) {
            throw new NoSuchElementException(format("The software {0} could not be found", softName));
        }
        return result;
    }

    public void addSoftware(Software soft) {
        this.softwareList.add(soft);
    }

    public String generateFileStr() {
        StringBuffer sb = new StringBuffer();
        sb.append("node '" + this.id + "'{");
        sb.append(eol);
        for (Software soft : softwareList) {
            sb.append(soft.generateFileStr());
            sb.append(eol);
        }
        sb.append("}");
        sb.append(eol);

        return sb.toString();
    }

    public boolean isManifestGenerated() {
        return manifestGenerated;
    }

    public void setManifestGenerated(boolean manifestGenerated) {
        this.manifestGenerated = manifestGenerated;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation 
     * of this object.
     */
    public String toString() {
       StringBuilder sb = new StringBuilder("[[Node]");
       sb.append("[eol = ").append(this.eol).append("]");
       sb.append("[id = ").append(this.id).append("]");
       sb.append("[groupName = ").append(this.groupName).append("]");
       sb.append("[softwareList = ").append(this.softwareList).append("]");
       sb.append("[manifestGenerated = ").append(this.manifestGenerated).append("]");
       sb.append("]");
       return sb.toString();
    }

    

}
