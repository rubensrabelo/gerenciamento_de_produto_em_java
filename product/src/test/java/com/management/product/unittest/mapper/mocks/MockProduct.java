package com.management.product.unittest.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import com.management.product.data.dto.ProductDTO;
import com.management.product.models.Product;

public class MockProduct {
	
	public Product mockEntity() {
		return mockEntity(1);
	}
	
	public ProductDTO mockDTO() {
		return mockDTO(1);
	}
	
	public List<Product> mockEntityList(int quantity) {
		List<Product> prods = new ArrayList<>();
		for(int i = 0; i < quantity; i++)
			prods.add(mockEntity(i));
		return prods;
	}
	
	public List<ProductDTO> mockDTOList(int quantity) {
		List<ProductDTO> prodsDTO = new ArrayList<>();
		for(int i = 0; i < quantity; i++)
			prodsDTO.add(mockDTO(i));
		return prodsDTO;
	}
	
	public Product mockEntity(int number) {
		Product prod = new Product();
		prod.setId((long) number);
		prod.setName("Name " + number);
		prod.setPrice((double) number);
		return prod;
	}
	
	public ProductDTO mockDTO(int number) {
		ProductDTO prodDTO = new ProductDTO();
		prodDTO.setId((long) number);
		prodDTO.setName("Name " + number);
		prodDTO.setPrice((double) number);
		return prodDTO;
	}
}
