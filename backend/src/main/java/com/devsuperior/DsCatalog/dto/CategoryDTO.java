package com.devsuperior.DsCatalog.dto;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.DsCatalog.entities.Category;

public class CategoryDTO implements Serializable {

	private static final long serialversionUID = 1L;
	private Long id;
	private String name;

	public CategoryDTO() {

	}

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CategoryDTO(Category entity) {
		id = entity.getId();
		name = entity.getName();
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
}
