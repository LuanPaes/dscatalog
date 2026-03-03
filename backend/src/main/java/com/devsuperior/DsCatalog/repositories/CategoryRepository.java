package com.devsuperior.DsCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.DsCatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {



}
