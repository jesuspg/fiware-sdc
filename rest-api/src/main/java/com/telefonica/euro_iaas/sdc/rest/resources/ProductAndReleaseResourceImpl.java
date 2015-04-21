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

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.model.dto.ProductAndReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.auth.OpenStackAuthenticationProvider;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

@Path("/catalog/productandrelease")
@Component
@Scope("request")
public class ProductAndReleaseResourceImpl implements ProductAndReleaseResource {

    private ProductManager productManager;

    public static String PUBLIC_METADATA = "public";
    public static String TENANT_METADATA = "tenant_id";

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger("ProductAndReleaseResourceImpl");

    @Override
    public List<ProductAndReleaseDto> findAllProductAndRelease(Integer page, Integer pageSize, String orderBy,
            String orderType) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();

        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return filterProductsAndReleases(productManager.findProductAndReleaseByCriteria(criteria));
    }

    private List<ProductAndReleaseDto> filterProductsAndReleases(List<ProductAndReleaseDto> productAndReleases) {
        List<ProductAndReleaseDto> filterProducts = new ArrayList<ProductAndReleaseDto>();

        for (ProductAndReleaseDto productAndRelease : productAndReleases) {
            if (productAndRelease.getProduct().getMapMetadata().get(PUBLIC_METADATA) != null
                    && productAndRelease.getProduct().getMapMetadata().get(PUBLIC_METADATA).equals("no")) {
                if (checkProduct(productAndRelease.getProduct())) {
                    filterProducts.add(productAndRelease);
                }
            } else {
                filterProducts.add(productAndRelease);
            }
        }

        return filterProducts;

    }

    private boolean checkProduct(Product product) {
        PaasManagerUser credentials = OpenStackAuthenticationProvider.getCredentials();
        if (product.getMapMetadata().get(TENANT_METADATA) != null
                && product.getMapMetadata().get(TENANT_METADATA).equals(credentials.getTenantId())) {
            return true;
        }
        return false;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
