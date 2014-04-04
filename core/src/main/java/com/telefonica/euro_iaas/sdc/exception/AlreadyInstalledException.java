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

package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.InstallableInstance;

/**
 * Exception thrown when try to install an installable unit that already exists.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class AlreadyInstalledException extends Exception {

    private InstallableInstance instace;

    public AlreadyInstalledException() {
        super();
    }

    public AlreadyInstalledException(InstallableInstance instace) {
        this.instace = instace;
    }

    public AlreadyInstalledException(String msg) {
        super(msg);
    }

    public AlreadyInstalledException(Throwable e) {
        super(e);
    }

    public AlreadyInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the instace
     */
    public InstallableInstance getInstace() {
        return instace;
    }

    /**
     * @param instace
     *            the instace to set
     */
    public void setInstace(InstallableInstance instace) {
        this.instace = instace;
    }

    @Override
    public String getMessage() {
        return "The " + instace.getClass().getSimpleName() + " " + instace.getId() + " is already installed.";
    }
}
