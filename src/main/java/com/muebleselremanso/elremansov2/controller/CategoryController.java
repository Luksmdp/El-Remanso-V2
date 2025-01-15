package com.muebleselremanso.elremansov2.controller;

import com.muebleselremanso.elremansov2.model.dto.ApiResponse;
import com.muebleselremanso.elremansov2.model.dto.CategoryDto;
import com.muebleselremanso.elremansov2.model.dto.ProductDto;
import com.muebleselremanso.elremansov2.model.entity.Category;
import com.muebleselremanso.elremansov2.model.entity.Product;
import com.muebleselremanso.elremansov2.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> findAllCategories(){
        List<Category> categoryList = categoryService.findAll();
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>("Categories Found",categoryList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> findCategoryById(@PathVariable Long id){
        Category category = categoryService.findById(id);
        ApiResponse<Category> apiResponse = new ApiResponse<>("Category Found",category);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Category>> saveCategory(@Valid @RequestBody CategoryDto categoryDto){
        Category category = categoryService.save(categoryDto);
        ApiResponse<Category> apiResponse = new ApiResponse<>("Category saved",category);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Category removed",null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto){
        Category category = categoryService.update(categoryDto,id);
        ApiResponse<Category> apiResponse = new ApiResponse<>("Category updated",category);
        return ResponseEntity.ok(apiResponse);
    }
}
