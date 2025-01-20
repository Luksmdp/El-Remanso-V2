package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.model.dto.ProductDto;
import com.muebleselremanso.elremansov2.model.entity.Product;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product save(ProductDto productDto);
    void delete(Long id);
    List<Product> findAll();
    Product findById(Long id);
    Product update(ProductDto productDto,Long id);
    List<Product> findByCategoryAndPriceBetween(Long categoryId, Double priceMin, Double priceMax);
    void uploadImages(Long id, MultipartFile[] files);
    List<String> listImages(Long productId);
    void deleteImages(Long productId,List<String> imageNames);
    void setMainImage(Long id, String imagePath);
    Resource getImageResource(Long productId, String imageName);
    List<Product> findByFilters(String name, Long categoryId, Double minPrice, Double maxPrice);
}
