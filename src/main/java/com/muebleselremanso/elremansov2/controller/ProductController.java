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
import org.springframework.web.multipart.MultipartFile;

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
        productService.delete(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Product removed",null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        Product product = productService.update(productDto,id);
        ApiResponse<Product> apiResponse = new ApiResponse<>("Product updated",product);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/products/filter")
    public ResponseEntity<ApiResponse<List<Product>>> findProductsByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<Product> filteredProducts = productService.findByFilters(name, categoryId, minPrice, maxPrice);

        ApiResponse<List<Product>> apiResponse = new ApiResponse<>("Filtered products found", filteredProducts);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/products/images/{id}")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files) {

        List<String> filesList = productService.uploadImages(id, files);

        ApiResponse<List<String>> apiResponse = new ApiResponse<>("Images uploaded", filesList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/products/images/{id}")
    public ResponseEntity<ApiResponse<List<String>>> listImages(@PathVariable Long id){
        List<String> imagesList = productService.listImages(id);

        ApiResponse<List<String>> apiResponse = new ApiResponse<>("Images listed",imagesList);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/products/images/{id}")
    public ResponseEntity<ApiResponse<String>> deleteImages(
            @PathVariable Long id,
            @RequestParam List<String> imageNames) {

        productService.deleteImages(id, imageNames);

        ApiResponse<String> apiResponse = new ApiResponse<>("Images deleted successfully", null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/products/images/main/{id}")
    public ResponseEntity<ApiResponse<String>> setMainImage(
            @PathVariable Long id,
            @RequestParam String imageName) {

        productService.setMainImage(id, imageName);

        ApiResponse<String> apiResponse = new ApiResponse<>("Main image set successfully", null);
        return ResponseEntity.ok(apiResponse);
    }

}
