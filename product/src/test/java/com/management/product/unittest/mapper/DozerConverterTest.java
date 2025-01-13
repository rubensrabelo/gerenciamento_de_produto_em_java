package com.management.product.unittest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.management.product.data.vo.v1.ProductVO;
import com.management.product.mapper.DozerMapper;
import com.management.product.models.Product;
import com.management.product.unittest.mapper.mocks.MockProduct;

public class DozerConverterTest {

	MockProduct inputObject;

	@BeforeEach
	public void setUp() {
		inputObject = new MockProduct();
	}

	@Test
	public void parseEntityToVOTest() {
		ProductVO outuput = DozerMapper.parseObject(inputObject.mockEntity(), ProductVO.class);
		assertEquals(Long.valueOf(1L), outuput.getId());
		assertEquals("Name 1", outuput.getName());
		assertEquals(1.0, outuput.getPrice());
	}

	@Test
	public void parseEntityListToVOListTest() {
		List<ProductVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(11), ProductVO.class);

		ProductVO outuput = outputList.get(2);
		assertEquals(Long.valueOf(2L), outuput.getId());
		assertEquals("Name 2", outuput.getName());
		assertEquals(2.0, outuput.getPrice());

		outuput = outputList.get(5);
		assertEquals(Long.valueOf(5L), outuput.getId());
		assertEquals("Name 5", outuput.getName());
		assertEquals(5.0, outuput.getPrice());

		outuput = outputList.get(10);
		assertEquals(Long.valueOf(10L), outuput.getId());
		assertEquals("Name 10", outuput.getName());
		assertEquals(10.0, outuput.getPrice());
	}

	@Test
	public void parseVOtoEntityTest() {
		Product outuput = DozerMapper.parseObject(inputObject.mockVO(), Product.class);
		assertEquals(Long.valueOf(1L), outuput.getId());
		assertEquals("Name 1", outuput.getName());
		assertEquals(1.0, outuput.getPrice());
	}

	@Test
	public void parseVOListtoEntityListTest() {
		List<Product> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(11), Product.class);

		Product outuput = outputList.get(2);
		assertEquals(Long.valueOf(2L), outuput.getId());
		assertEquals("Name 2", outuput.getName());
		assertEquals(2.0, outuput.getPrice());

		outuput = outputList.get(5);
		assertEquals(Long.valueOf(5L), outuput.getId());
		assertEquals("Name 5", outuput.getName());
		assertEquals(5.0, outuput.getPrice());

		outuput = outputList.get(10);
		assertEquals(Long.valueOf(10L), outuput.getId());
		assertEquals("Name 10", outuput.getName());
		assertEquals(10.0, outuput.getPrice());
	}
}
