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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;

/**
 * Models a chef node.
 * 
 * @author Sergio Arroyo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChefNode {

    private final static String RECIPE_ITEM_TEMPLATE = "recipe[{0}]";

    private String name;
    private String ohai_time;
    private Map<String, Object> automaticAttributes;
    private Map<String, Object> attributes;
    private Map<String, Object> overrides;
    private Map<String, Object> defaults;
    private List<String> runlList;
    private String chefType = "node";
    private String jsonClass = "Chef::Node";

    /**
     *
     */
    public ChefNode() {
        automaticAttributes = new HashMap<String, Object>();
        attributes = new HashMap<String, Object>();
        overrides = new HashMap<String, Object>();
        defaults = new HashMap<String, Object>();
        runlList = new ArrayList<String>();
    }

    public void addRecipe(String recipe) {
        String formatedRecipe = MessageFormat.format(RECIPE_ITEM_TEMPLATE, recipe);
        if (!runlList.contains(formatedRecipe)) {
            runlList.add(MessageFormat.format(RECIPE_ITEM_TEMPLATE, recipe));
        }
    }

    public void removeRecipe(String recipe) {
        runlList.remove(MessageFormat.format(RECIPE_ITEM_TEMPLATE, recipe));
    }

    public boolean hasRecipe(String recipe) {
        return runlList.contains(MessageFormat.format(RECIPE_ITEM_TEMPLATE, recipe));
    }

    public void addOverride(String key, String value) {
        overrides.put(key, value);
    }

    public void addOverride(String process, String key, String value) {
        overrides = addNewAttribute(overrides, process, key, value);
    }

    public void addDefault(String process, String key, String value) {
        defaults = addNewAttribute(defaults, process, key, value);
    }

    public void addAttribute(String process, String key, String value) {
        attributes = addNewAttribute(attributes, process, key, value);
    }

    public void addAutomaticAttribute(String process, String key, String value) {
        automaticAttributes = addNewAttribute(automaticAttributes, process, key, value);
    }

    private Map<String, Object> addNewAttribute(Map<String, Object> map, String process, String key, String value) {
        JSONObject attr;
        if (map.containsKey(process)) {
            attr = (JSONObject) map.get(process);
        } else {
            attr = new JSONObject();
        }
        attr.put(key, value);
        map.put(process, attr);
        return map;
    }

    public void removeOverride(String key) {
        overrides.remove(key);
    }

    public void addDefault(String key, String value) {
        defaults.put(key, value);
    }

    public void removeDefault(String key) {
        defaults.remove(key);
    }

    public void addAttritube(String key, String value) {
        attributes.put(key, value);
    }

    public void removeAttritube(String key) {
        attributes.remove(key);
    }

    public void addAutomaticAttritube(String key, String value) {
        automaticAttributes.put(key, value);
    }

    public void removeAutomaticAttritube(String key) {
        automaticAttributes.remove(key);
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
     * @return the ohai_time
     */
    public String getOhai_time() {
        return ohai_time;
    }

    /**
     * @param ohai_time
     *            the ohai_time to set
     */
    public void setOhai_time(String ohai_time) {
        this.ohai_time = ohai_time;
    }

    /**
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the automaticAttributes
     */
    public Map<String, Object> getAutomaticAttributes() {
        return automaticAttributes;
    }

    /**
     * @param automaticAttributes
     *            the automaticAttributes to set
     */
    public void setAutomaticAttributes(Map<String, Object> automaticAttributes) {
        this.automaticAttributes = automaticAttributes;
    }

    /**
     * @return the overrides
     */
    public Map<String, Object> getOverrides() {
        return overrides;
    }

    /**
     * @param overrides
     *            the overrides to set
     */
    public void setOverrides(Map<String, Object> overrides) {
        this.overrides = overrides;
    }

    /**
     * @return the defaults
     */
    public Map<String, Object> getDefaults() {
        return defaults;
    }

    /**
     * @param defaults
     *            the defaults to set
     */
    public void setDefaults(Map<String, Object> defaults) {
        this.defaults = defaults;
    }

    /**
     * @return the runlList
     */
    public List<String> getRunlList() {
        return runlList;
    }

    /**
     * @param runlList
     *            the runlList to set
     */
    public void setRunlList(List<String> runlList) {
        this.runlList = runlList;
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
     * JSon serializer
     */
    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("name", name);
        jsonObject.accumulate("json_class", "Chef::Node");
        jsonObject.accumulate("normal", attributes);
        jsonObject.accumulate("automatic", automaticAttributes);
        jsonObject.accumulate("default", defaults);
        jsonObject.accumulate("override", overrides);
        jsonObject.accumulate("run_list", runlList);
        return jsonObject.toString();
    }

    @SuppressWarnings("unchecked")
    public void fromJson(JSONObject jsonNode) {
        name = jsonNode.getString("name");
        runlList = jsonNode.getJSONArray("run_list");
        overrides = jsonNode.getJSONObject("override");
        defaults = jsonNode.getJSONObject("default");
        attributes = jsonNode.getJSONObject("normal");
        automaticAttributes = jsonNode.getJSONObject("automatic");
    }

    @SuppressWarnings("unchecked")
    public String getChefNodeName(String stringChefNodes, String hostname) {

        String[] output = stringChefNodes.split("\"" + hostname);
        String name = "";
        for (int i = 1; i < output.length; i++) {
            if (output[i].startsWith("\"") && name.isEmpty()) {
                String url = output[i].split("\"")[2];
                String nameAux = url.split("nodes")[1];
                if (nameAux.startsWith("\\"))
                    name = nameAux.substring(2, nameAux.length());
                else
                    name = nameAux.substring(1, nameAux.length());
            }
            if (output[i].startsWith(".") && name.isEmpty()) {
                String url = output[i].split("\"")[2];
                String nameAux = url.split("nodes")[1];
                if (nameAux.startsWith("\\"))
                    name = nameAux.substring(2, nameAux.length());
                else
                    name = nameAux.substring(1, nameAux.length());
            }
        }

        return name;

        /*
         * if (output[1].startsWith("\\.")){ String url = output[1].split("\"")[2]; String nameAux =
         * url.split("nodes")[1]; String name = nameAux.substring(1, nameAux.length()); return name; } else if
         * (output[1].startsWith("\"")) { String url = output[1].split("\"")[2]; String nameAux = url.split("nodes")[1];
         * String name = nameAux.substring(1, nameAux.length()); return name; } else { String url =
         * output[1].split("\"")[2]; String nameAux = url.split("nodes")[1]; String name = nameAux.substring(1,
         * nameAux.length()); return name; }
         */
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ChefNode]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[ohai_time = ").append(this.ohai_time).append("]");
        sb.append("[automaticAttributes = ").append(this.automaticAttributes).append("]");
        sb.append("[attributes = ").append(this.attributes).append("]");
        sb.append("[overrides = ").append(this.overrides).append("]");
        sb.append("[defaults = ").append(this.defaults).append("]");
        sb.append("[runlList = ").append(this.runlList).append("]");
        sb.append("[chefType = ").append(this.chefType).append("]");
        sb.append("[jsonClass = ").append(this.jsonClass).append("]");
        sb.append("]");
        return sb.toString();
    }

}
