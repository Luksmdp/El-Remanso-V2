package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.exception.*;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
                .activePromotion(productDto.getActivePromotion())
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

        Product product = productOptional.get();

        // Ruta de la carpeta que se debe eliminar
        String folderName = String.valueOf(product.getId());
        Path productFolderPath = Paths.get(urlBase + folderName);

        // Eliminar la carpeta y su contenido
        try {
            // Si la carpeta no está vacía, eliminamos su contenido
            if (Files.exists(productFolderPath)) {
                Files.walk(productFolderPath)
                        .sorted(Comparator.reverseOrder())  // Se elimina desde el archivo más profundo
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            throw new DirectoryDeletionException("Directory could not be deleted for path: " + productFolderPath, e);
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

        Product product = productOptional.get();

        if (productDto.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());
            if (categoryOptional.isEmpty()){
                throw new CategoryNotFoundException("The category with id: "+productDto.getCategoryId()+ " was not found");
            }
            Category category = categoryOptional.get();
            product.setCategory(category);
        }



        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getPromotionalPrice() != null) {
            product.setPromotionalPrice(productDto.getPromotionalPrice());
        }
        if (productDto.getVisible() != null) {
            product.setVisible(productDto.getVisible());
        }
        if (productDto.getActivePromotion() != null) {
            product.setActivePromotion(productDto.getActivePromotion());
        }


        return productRepository.save(product);
    }

    @Override
    public List<Product> findByFilters(String name, Long categoryId, Double minPrice, Double maxPrice) {
        if (name == null && categoryId == null && minPrice == null && maxPrice == null) {
            return findAll();
        }

        List<Product> productList = productRepository.findByFilters(name, categoryId, minPrice, maxPrice);

        if (productList.isEmpty()) {
            throw new NoProductsFoundException("No products found with the specified filters");
        }

        return productList;
    }

    @Override
    public List<String> uploadImages(Long id, MultipartFile[] files) {
        // Verifica que el producto existe
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("The product with id: " + id + " was not found");
        }

        // Ruta de la carpeta del producto
        String folderName = String.valueOf(id);
        Path productFolderPath = Paths.get(urlBase + folderName);

        // Crea la carpeta si no existe
        try {
            if (!Files.exists(productFolderPath)) {
                Files.createDirectories(productFolderPath);
            }
        } catch (IOException e) {
            throw new DirectoryCreationException("Directory could not be created for path: " + productFolderPath, e);
        }

        // Lista para almacenar los nombres de los archivos subidos
        List<String> uploadedFileNames = new ArrayList<>();

        // Procesa cada archivo
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("One of the files is empty.");
            }

            // Valida el formato del archivo
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png)$")) {
                throw new InvalidFileFormatException("Invalid file format for file: " + originalFilename);
            }

            // Genera un nuevo nombre de archivo si hay conflicto
            String uniqueFilename = originalFilename + "_" + UUID.randomUUID().toString();

            // Define la ruta del archivo
            Path filePath = productFolderPath.resolve(uniqueFilename);

            try {
                // Guarda el archivo
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Añade el nombre del archivo a la lista
                uploadedFileNames.add(uniqueFilename);

                // Actualiza la lista de imágenes del producto
                Product product = productOptional.get();
                product.getImagesList().add(uniqueFilename);
                productRepository.save(product);
            } catch (IOException e) {
                throw new FileUploadException("Error uploading file: " + originalFilename, e);
            }
        }

        return uploadedFileNames;
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
