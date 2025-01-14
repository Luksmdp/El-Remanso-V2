package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.model.dto.CategoryDto;
import com.muebleselremanso.elremansov2.model.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    Category save(CategoryDto categoryDto);
    void delete(Long id);
    List<Category> findAll();
    Category findById(Long id);
    Category update(CategoryDto categoryDto,Long id);
}
