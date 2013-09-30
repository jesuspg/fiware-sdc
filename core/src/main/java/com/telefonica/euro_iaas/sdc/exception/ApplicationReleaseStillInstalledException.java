package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Exception thrown when try to delete a ApplicationRelease which is still installed
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ApplicationReleaseStillInstalledException extends Exception {

    private ApplicationRelease applicationRelease;
    private List<ApplicationInstance> applicationInstances;

    public ApplicationReleaseStillInstalledException() {
        super();
    }

    public ApplicationReleaseStillInstalledException(List<ApplicationInstance> applicationInstances) {
        this.applicationInstances = applicationInstances;
    }

    public ApplicationReleaseStillInstalledException(ApplicationRelease applicationRelease,
            List<ApplicationInstance> applicationInstances) {
        this.applicationRelease = applicationRelease;
        this.applicationInstances = applicationInstances;
    }

    public ApplicationReleaseStillInstalledException(String msg) {
        super(msg);
    }

    public ApplicationReleaseStillInstalledException(Throwable e) {
        super(e);
    }

    public ApplicationReleaseStillInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productRelease
     */
    public ApplicationRelease getApplicationRelease() {
        return applicationRelease;
    }

    /**
     * @param applicationRelease
     *            the applicationRelease to set
     */
    public void setPApplicationRelease(ApplicationRelease applicationRelease) {
        this.applicationRelease = applicationRelease;
    }

    /**
     * @return the List<ApplicationInstances>
     */
    public List<ApplicationInstance> getApplicationIntances() {
        return applicationInstances;
    }

    /**
     * @param productInstances
     *            the productInstances to set
     */
    public void setApplicationIntances(List<ApplicationInstance> applicationInstances) {
        this.applicationInstances = applicationInstances;
    }

}
