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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;

/**
 * Models a Chef Client.
 * 
 * @author jesus.movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChefClient {

    private String name;
    private String chefType = "node";
    private String jsonClass = "Chef::Node";
    private String publickey;
    private String _rev;
    private String admin;

    /**
    *
    */
    public ChefClient() {
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
     * @return the chefType
     */
    public String getChefType() {
        return chefType;
    }

    /**
     * @param chefType
     *            the chefType to set
     */
    public void setChefType(String chefType) {
        this.chefType = chefType;
    }

    /**
     * @return the jsonClass
     */
    public String getJsonClass() {
        return jsonClass;
    }

    /**
     * @param jsonClass
     *            the jsonClass to set
     */
    public void setJsonClass(String jsonClass) {
        this.jsonClass = jsonClass;
    }

    /**
     * @return the publickey
     */
    public String getPublickey() {
        return publickey;
    }

    /**
     * @param publickey
     *            the publickey to set
     */
    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    /**
     * @return the _rev
     */
    public String get_rev() {
        return _rev;
    }

    /**
     * @param _rev
     *            the _rev to set
     */
    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    /**
     * @return the admin
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * @param admin
     *            the admin to set
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * JSon serializer
     */
    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("name", name);
        jsonObject.accumulate("json_class", "Chef::Node");
        jsonObject.accumulate("chef_type", "node");
        jsonObject.accumulate("public_key", publickey);
        jsonObject.accumulate("_rev", _rev);
        jsonObject.accumulate("admin", admin);
        return jsonObject.toString();
    }

    @SuppressWarnings("unchecked")
    public void fromJson(JSONObject jsonNode) {
        name = jsonNode.getString("name");
        publickey = jsonNode.getString("public_key");
        // _rev = jsonNode.getString("_rev");
        admin = jsonNode.getString("admin");
    }

    @SuppressWarnings("unchecked")
    public void fromKnifeCommand(String output) {

        String[] claveValor = output.split("\n");
        // String[] claveValor = output.split(": ");
        name = getValue(claveValor, "name");
        publickey = getValue(claveValor, "public_key");
        _rev = getValue(claveValor, "_rev");
        admin = getValue(claveValor, "admin");
    }

    // Hay un error con publickey
    private String getValue(String[] claveValor, String clave) {
        String valor = null;
        for (int i = 0; i < claveValor.length; i++) {
            String clavevalor = (String) claveValor[i];
            if (clavevalor.contains(clave)) {
                valor = (clavevalor.replace(clave + ":", " ")).trim();
            }

        }
        return valor;
    }

    @SuppressWarnings("unchecked")
    public String getChefClientName(String stringChefClients, String hostname) {

        String[] output = stringChefClients.split("\"" + hostname);
        if (output.length == 1) {
            throw new NoSuchElementException("The hostname " + hostname + " not found in json");
        }
        String url = output[1].split("\"")[2];
        System.out.println("url:" + url);
        String nameAux = url.split("clients")[1];
        System.out.println("nameAux:" + nameAux);
        if (nameAux.startsWith("\\"))
            // String name = nameAux.substring(1, nameAux.length());
            return nameAux.substring(2, nameAux.length());
        else
            // String name = nameAux.substring(1, nameAux.length());
            return nameAux.substring(1, nameAux.length());
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ChefClient]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[chefType = ").append(this.chefType).append("]");
        sb.append("[jsonClass = ").append(this.jsonClass).append("]");
        sb.append("[publickey = ").append(this.publickey).append("]");
        sb.append("[_rev = ").append(this._rev).append("]");
        sb.append("[admin = ").append(this.admin).append("]");
        sb.append("]");
        return sb.toString();
    }

    public NodeDto toNodeDto() {
        NodeDto node = new NodeDto ();
        node.setSoftwareName(this.getName());
        return node;
    }

}
