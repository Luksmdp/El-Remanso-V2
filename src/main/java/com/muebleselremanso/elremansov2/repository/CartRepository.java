package com.muebleselremanso.elremansov2.repository;

import com.muebleselremanso.elremansov2.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
