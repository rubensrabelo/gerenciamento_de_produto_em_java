package com.management.product.integrationtest.dto.wrappers.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management.product.integrationtest.dto.ProductDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "pagedModelProduct")
public class PagedModelProduct {

    @JsonProperty("content")
    @XmlElement(name = "content")
    private List<ProductDTO> content;

    public PagedModelProduct() {}

    public List<ProductDTO> getContent() {
        return content;
    }

    public void setContent(List<ProductDTO> content) {
        this.content = content;
    }
}
