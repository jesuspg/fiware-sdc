package com.telefonica.euro_iaas.sdc.installator;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

public interface Installator {

    void callService(VM vm, String vdc, ProductRelease productRelease, String action) throws InstallatorException;

    void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action)
            throws InstallatorException, NodeExecutionException;

    void upgrade(ProductInstance productInstance, VM vm) throws InstallatorException;

    void callService(ProductInstance productInstance, String action) throws InstallatorException, NodeExecutionException;
    
    void validateInstalatorData(VM vm) throws InvalidInstallProductRequestException;
}
