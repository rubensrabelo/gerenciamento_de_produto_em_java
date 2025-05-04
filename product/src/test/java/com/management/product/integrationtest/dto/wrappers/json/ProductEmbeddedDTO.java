package com.management.product.integrationtest.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management.product.integrationtest.dto.ProductDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ProductEmbeddedDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("productDTOList")
    private List<ProductDTO> products;

    public ProductEmbeddedDTO() {}

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
