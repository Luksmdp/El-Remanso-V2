package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.model.dto.CategoryDto;
import com.muebleselremanso.elremansov2.model.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Override
    public Category save(CategoryDto categoryDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public Category findById(Long id) {
        return null;
    }

    @Override
    public Category update(CategoryDto categoryDto, Long id) {
        return null;
    }
}
