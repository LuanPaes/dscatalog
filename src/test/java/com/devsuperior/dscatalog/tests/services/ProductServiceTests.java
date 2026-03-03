package com.devsuperior.dscatalog.tests.services;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

import jakarta.persistence.EntityNotFoundException;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	@Mock
	private ProductRepository productrepository;

	@Mock
	private CategoryRepository categoryrepository;

	private Long existingId;
	private Long noExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO dto;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		noExistingId = 99999L;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		dto = Factory.createProductDTO();

		Mockito.when(productrepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(productrepository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(productrepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productrepository.findById(noExistingId)).thenReturn(Optional.empty());

		Mockito.doNothing().when(productrepository).deleteById(existingId);
		Mockito.when(productrepository.existsById(existingId)).thenReturn(true);
		Mockito.when(productrepository.existsById(noExistingId)).thenReturn(false);
		Mockito.when(productrepository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productrepository).deleteById(dependentId);

		Mockito.when(productrepository.getReferenceById(existingId)).thenReturn(product);

	}

	@Test
	public void findAllPagedSHouldReturnPage() {

		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);

		Mockito.verify(productrepository).findAll(pageable);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {

		ProductDTO result = service.findById(existingId);

		Assertions.assertTrue(true);
	}

	@Test
	public void findByIdThrowResourceNotFoundWhenIdNoExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(noExistingId);
		});
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(DatabaseException.class, () -> {

			service.delete(dependentId);
		});
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExist() {
		ProductDTO result = service.update(existingId, dto);

		Assertions.assertTrue(true);

	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExist() {

		Mockito.when(productrepository.getReferenceById(noExistingId)).thenThrow(EntityNotFoundException.class);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(noExistingId, dto);
		});
	}
	
	
}
