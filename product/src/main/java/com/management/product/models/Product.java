package com.management.product.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	private Double price;

	@Column(nullable = false)
	private Boolean isInStock;
	
	public Product() {
	}

	public Product(Long id, String name, Double price, Boolean isInStock) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.isInStock = isInStock;
	}

	@PrePersist
	public void prePersist() {
		if(isInStock == null) isInStock = true;
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
		Product product = (Product) o;
		return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(isInStock, product.isInStock);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price, isInStock);
	}
}
