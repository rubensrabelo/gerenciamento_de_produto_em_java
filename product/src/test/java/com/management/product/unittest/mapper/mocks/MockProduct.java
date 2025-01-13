package com.management.product.unittest.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import com.management.product.data.vo.v1.ProductVO;
import com.management.product.models.Product;

public class MockProduct {
	
	public Product mockEntity() {
		return mockEntity(1);
	}
	
	public ProductVO mockVO() {
		return mockVO(1);
	}
	
	public List<Product> mockEntityList(int quantity) {
		List<Product> prods = new ArrayList<>();
		for(int i = 0; i < quantity; i++)
			prods.add(mockEntity(i));
		return prods;
	}
	
	public List<ProductVO> mockVOList(int quantity) {
		List<ProductVO> prodsVO = new ArrayList<>();
		for(int i = 0; i < quantity; i++)
			prodsVO.add(mockVO(i));
		return prodsVO;
	}
	
	public Product mockEntity(int number) {
		Product prod = new Product();
		prod.setName("Name " + number);
		prod.setPrice((double) number);
		return prod;
	}
	
	public ProductVO mockVO(int number) {
		ProductVO prodVO = new ProductVO();
		prodVO.setName("Name " + number);
		prodVO.setPrice((double) number);
		return prodVO;
	}
}
