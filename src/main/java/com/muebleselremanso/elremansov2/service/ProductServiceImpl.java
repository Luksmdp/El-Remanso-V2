package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.exception.CategoryNotFoundException;
import com.muebleselremanso.elremansov2.exception.DirectoryCreationException;
import com.muebleselremanso.elremansov2.exception.NoProductsFoundException;
import com.muebleselremanso.elremansov2.exception.ProductNotFoundException;
import com.muebleselremanso.elremansov2.model.dto.ProductDto;
import com.muebleselremanso.elremansov2.model.entity.Category;
import com.muebleselremanso.elremansov2.model.entity.Product;
import com.muebleselremanso.elremansov2.repository.CategoryRepository;
import com.muebleselremanso.elremansov2.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Value("${file.upload-dir}")
    private String urlBase;

    @Override
    public Product save(ProductDto productDto) {
        Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());
        if (categoryOptional.isEmpty()){
            throw new CategoryNotFoundException("The category with id: "+productDto.getCategoryId()+ " was not found");
        }

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .promotionalPrice(productDto.getPromotionalPrice())
                .visible(productDto.getVisible())
                .category(categoryOptional.get())
                .build();

        product = productRepository.save(product);

        // Crear la carpeta usando el ID del producto
        String folderName = String.valueOf(product.getId());
        Path productFolderPath = Paths.get(urlBase + folderName);

        try {
            Files.createDirectories(productFolderPath);
        } catch (IOException e) {
            throw new DirectoryCreationException("Directory could not be created for path: " + productFolderPath,e);
        }

        return product;
    }

    @Override
    public void delete(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new ProductNotFoundException("The product with id: " + id + " was not found");
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()){
            throw new NoProductsFoundException("No products found in the database");
        }

        return productList;
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new ProductNotFoundException("The product with id: "+ id + " was not found");
        }

        return productOptional.get();
    }

    @Override
    public Product update(ProductDto productDto, Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new ProductNotFoundException("The product with id: "+ id + " was not found");
        }

        Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());
        if (categoryOptional.isEmpty()){
            throw new CategoryNotFoundException("The category with id: "+productDto.getCategoryId()+ " was not found");
        }

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .promotionalPrice(productDto.getPromotionalPrice())
                .visible(productDto.getVisible())
                .category(categoryOptional.get())
                .id(id)
                .build();

        return productRepository.save(product);
    }

    @Override
    public List<Product> findByCategoryAndPriceBetween(Long categoryId, Double priceMin, Double priceMax) {
        return null;
    }

    @Override
    public void uploadImages(Long id, MultipartFile[] files) {

    }

    @Override
    public List<String> listImages(Long productId) {
        return null;
    }

    @Override
    public void deleteImages(Long productId, List<String> imageNames) {

    }

    @Override
    public void setMainImage(Long id, String imagePath) {

    }

    @Override
    public Resource getImageResource(Long productId, String imageName) {
        return null;
    }
}