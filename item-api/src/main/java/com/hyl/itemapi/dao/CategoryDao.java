package com.hyl.itemapi.dao;

import com.hyl.itemapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CategoryDao extends JpaRepository<Category, Long> {
}
