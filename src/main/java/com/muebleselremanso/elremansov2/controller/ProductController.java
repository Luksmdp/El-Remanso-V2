package com.muebleselremanso.elremansov2.controller;

import com.muebleselremanso.elremansov2.model.dto.ApiResponse;
import com.muebleselremanso.elremansov2.model.dto.ProductDto;
import com.muebleselremanso.elremansov2.model.entity.Product;
import com.muebleselremanso.elremansov2.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> findAllProducts(){
        List<Product> productList = productService.findAll();
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>("Products found",productList);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> saveProduct(@Valid @RequestBody ProductDto productDto){
        Product productSaved = productService.save(productDto);
        ApiResponse<Product> apiResponse = new ApiResponse<>("Product saved",productSaved);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> findProductById(@PathVariable Long id){
        Product product = productService.findById(id);
        ApiResponse<Product> apiResponse = new ApiResponse<>("Product found",product);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id){
        ApiResponse<String> apiResponse = new ApiResponse<>("Product removed",null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @Valid ProductDto productDto){
        Product product = productService.update(productDto,id);
        ApiResponse<Product> apiResponse = new ApiResponse<>("Product updated",product);
        return ResponseEntity.ok(apiResponse);
    }
}
