package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

/**
 * Provides some commons methods to work with installable instances.
 * 
 * @author Sergio Arroyo
 */
public abstract class AbstractInstallableService extends AbstractBaseService {

    private String upgradePath;
    private String configPath;
    private String uninstallPath;
    private SDCWebResourceFactory sdcWebResourceFactory = new SDCWebResourceFactory();

    public SDCWebResourceFactory getSdcWebResourceFactory() {
        return sdcWebResourceFactory;
    }

    public void setSdcWebResourceFactory(SDCWebResourceFactory sdcWebResourceFactory) {
        this.sdcWebResourceFactory = sdcWebResourceFactory;
    }

    /**
     * See {@link ApplicationInstanceService#upgrade(String, Long, String, String)} or
     * {@link ProductInstanceService#upgrade(String, Long, String, String)}
     */
    public Task upgrade(String vdc, String name, String version, String callback) {
        String url = getBaseHost() + MessageFormat.format(upgradePath, vdc, name, version);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType());
        builder = addCallback(builder, callback);
        return builder.put(Task.class);
    }

    /**
     * See {@link ApplicationInstanceService#configure(String, Long, String, Attributes)} or
     * {@link ProductInstanceService#configure(String, Long, String, Attributes)}
     */
    public Task configure(String vdc, String name, String callback, List<Attribute> arguments) {
        String url = getBaseHost() + MessageFormat.format(configPath, vdc, name);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType());
        builder = addCallback(builder, callback);
        Attributes attributes = new Attributes();
        attributes.addAll(arguments);
        // create Attributes object because Jersey can not write List<Attribute>
        builder.entity(attributes);
        return builder.put(Task.class);
    }

    /**
     * See {@link ApplicationInstanceService#uninstall(String, Long, String)} or
     * {@link ProductInstanceService#uninstall(String, Long, String)}
     */
    public Task uninstall(String vdc, String name, String callback) {
        String url = getBaseHost() + MessageFormat.format(uninstallPath, vdc, name);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType());
        builder = addCallback(builder, callback);
        return builder.delete(Task.class);
    }

    protected Builder addCallback(Builder resource, String callback) {
        if (!StringUtils.isEmpty(callback)) {
            resource = resource.header("callback", callback);
        }
        return resource;
    }

    /**
     * @return the upgradePath
     */
    public String getUpgradePath() {
        return upgradePath;
    }

    /**
     * @param upgradePath
     *            the upgradePath to set
     */
    public void setUpgradePath(String upgradePath) {
        this.upgradePath = upgradePath;
    }

    /**
     * @return the configPath
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * @param configPath
     *            the configPath to set
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * @return the uninstallPath
     */
    public String getUninstallPath() {
        return uninstallPath;
    }

    /**
     * @param uninstallPath
     *            the uninstallPath to set
     */
    public void setUninstallPath(String uninstallPath) {
        this.uninstallPath = uninstallPath;
    }
}
