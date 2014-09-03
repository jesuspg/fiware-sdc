package com.telefonica.euro_iaas.sdc.model.dto;

import com.telefonica.euro_iaas.sdc.model.Product;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

public class ProductAndReleases {

    private Product product;
    private List<ProductRelease> productReleaseList;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductRelease> getProductReleaseList() {
        return productReleaseList;
    }

    public void setProductReleaseList(List<ProductRelease> productReleaseList) {
        this.productReleaseList = productReleaseList;
    }

}
