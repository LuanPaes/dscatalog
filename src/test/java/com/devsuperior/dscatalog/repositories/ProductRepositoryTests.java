package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	ProductRepository repository;


	private long existingId;
	private long idNoExisting;
	private long countTotalProducts;
	private long depedentId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		idNoExisting = 1000L;
		countTotalProducts = 25L;
	
	}

	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}

	@Test
	public void deleteShouldDoNothingWhenIdDoesNotExist() {
		Assertions.assertDoesNotThrow(() ->{
			repository.deleteById(idNoExisting);
		});
	}

	@Test
	public void deleteShouldObjectWhenIdExists() {


		repository.deleteById(existingId);

		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void retornarOptionalNaoVazioQuandoOIdExistir() {

		Optional<Product> result = repository.findById(existingId);

		Assertions.assertTrue(result.isPresent());

	}

	@Test
	public void retornarOptionalVazioQuandoOIdNaoExistir() {

		Optional <Product> result = repository.findById(idNoExisting);
		Assertions.assertTrue(result.isEmpty());
	}

}

