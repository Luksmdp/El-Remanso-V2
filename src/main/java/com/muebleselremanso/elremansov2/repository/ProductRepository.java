package com.muebleselremanso.elremansov2.repository;

import com.muebleselremanso.elremansov2.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.category c " +
            "WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findByFilters(@Param("name") String name,
                                @Param("categoryId") Long categoryId,
                                @Param("minPrice") Double minPrice,
                                @Param("maxPrice") Double maxPrice);

}
