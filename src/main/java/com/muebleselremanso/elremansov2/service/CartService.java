package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.model.dto.CartDto;
import com.muebleselremanso.elremansov2.model.entity.Cart;

import java.util.List;

public interface CartService {

    public Cart createCart(CartDto cartDto);
    public void deleteCart(Long cartId);
    public List<Cart> findAll();
    public Cart findById(Long id);
}
