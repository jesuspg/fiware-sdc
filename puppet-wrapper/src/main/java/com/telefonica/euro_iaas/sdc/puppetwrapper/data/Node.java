package com.telefonica.euro_iaas.sdc.puppetwrapper.data;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Node {

    private String eol = System.getProperty("line.separator");

    private String name;
    private String groupName;
    private ArrayList<Software> softwareList = new ArrayList<Software>();

    public Node() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        sb.append("node '" + this.name + "'{");
        sb.append(eol);
        for (Software soft : softwareList) {
            sb.append(soft.generateFileStr());
            sb.append(eol);
        }
        sb.append("}");
        sb.append(eol);

        return sb.toString();
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        final String TAB = "    ";

        String retValue = "";

        retValue = "Node ( " + super.toString() + TAB + "eol = " + this.eol + TAB + "name = " + this.name + TAB
                + "groupName = " + this.groupName + TAB + "softwareList = " + this.softwareList + TAB + " )";

        return retValue;
    }

}
