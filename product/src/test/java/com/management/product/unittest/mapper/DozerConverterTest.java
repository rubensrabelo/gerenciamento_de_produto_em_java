package com.management.product.unittest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.management.product.data.dto.ProductDTO;
import com.management.product.models.Product;
import com.management.product.unittest.mapper.mocks.MockProduct;

public class DozerConverterTest {
	
	MockProduct inputObject;
	
	@BeforeEach
	public void setUp() {
		inputObject = new MockProduct();
	}

	@Test
	public void parseEntityToDTOTest() {
		ProductDTO output = DozerMapper.parseObject(inputObject.mockEntity(), ProductDTO.class);
		assertEquals(Long.valueOf(1L), output.getId());
		assertEquals("Name 1", output.getName());
		assertEquals(1.0, output.getPrice());
	}

	@Test
	public void parseEntityListToDTOListTest() {
		List<ProductDTO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(11), ProductDTO.class);

		ProductDTO output = outputList.get(2);
		assertEquals(Long.valueOf(2L), output.getId());
		assertEquals("Name 2", output.getName());
		assertEquals(2.0, output.getPrice());

		output = outputList.get(5);
		assertEquals(Long.valueOf(5L), output.getId());
		assertEquals("Name 5", output.getName());
		assertEquals(5.0, output.getPrice());

		output = outputList.get(10);
		assertEquals(Long.valueOf(10L), output.getId());
		assertEquals("Name 10", output.getName());
		assertEquals(10.0, output.getPrice());
	}

	@Test
	public void parseDTOtoEntityTest() {
		Product output = DozerMapper.parseObject(inputObject.mockDTO(), Product.class);
		assertEquals(Long.valueOf(1L), output.getId());
		assertEquals("Name 1", output.getName());
		assertEquals(1.0, output.getPrice());
	}

	@Test
	public void parseDTOListToEntityListTest() {
		List<Product> outputList = DozerMapper.parseListObjects(inputObject.mockDTOList(11), Product.class);

		Product output = outputList.get(2);
		assertEquals(Long.valueOf(2L), output.getId());
		assertEquals("Name 2", output.getName());
		assertEquals(2.0, output.getPrice());

		output = outputList.get(5);
		assertEquals(Long.valueOf(5L), output.getId());
		assertEquals("Name 5", output.getName());
		assertEquals(5.0, output.getPrice());

		output = outputList.get(10);
		assertEquals(Long.valueOf(10L), output.getId());
		assertEquals("Name 10", output.getName());
		assertEquals(10.0, output.getPrice());
	}
}
