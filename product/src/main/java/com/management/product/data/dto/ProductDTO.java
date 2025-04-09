package com.management.product.data.dto;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class ProductDTO extends RepresentationModel<ProductDTO> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Double price;
	private Boolean isInStock;
	
	public ProductDTO() {
	}

	public ProductDTO(Long id, String name, Double price, Boolean isInStock) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.isInStock = isInStock;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean getInStock() {
		return isInStock;
	}

	public void setInStock(Boolean inStock) {
		isInStock = inStock;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		ProductDTO that = (ProductDTO) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(isInStock, that.isInStock);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id, name, price, isInStock);
	}
}
