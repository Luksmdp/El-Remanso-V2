package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.exception.CategoryNotFoundException;
import com.muebleselremanso.elremansov2.exception.NoCategoriesFoundException;
import com.muebleselremanso.elremansov2.model.dto.CategoryDto;
import com.muebleselremanso.elremansov2.model.entity.Category;
import com.muebleselremanso.elremansov2.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category save(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()){
            throw new CategoryNotFoundException("The category with id: "+id+" was not found");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.isEmpty()){
            throw new NoCategoriesFoundException("No categories found in the database");
        }
        return categoryList;
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()){
            throw new CategoryNotFoundException("The category with id: "+id+" was not found");
        }
        return categoryOptional.get();
    }

    @Override
    public Category update(CategoryDto categoryDto, Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()){
            throw new CategoryNotFoundException("The category with id: "+id+" was not found");
        }
        Category category = categoryOptional.get();
        if (categoryDto.getName() != null){
            category.setName(categoryDto.getName());
        }
        if (categoryDto.getDescription() != null){
            category.setDescription(categoryDto.getDescription());
        }
        return categoryRepository.save(category);
    }
}
