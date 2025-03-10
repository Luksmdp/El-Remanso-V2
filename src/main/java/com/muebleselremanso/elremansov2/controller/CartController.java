package com.muebleselremanso.elremansov2.controller;

import com.muebleselremanso.elremansov2.model.dto.ApiResponse;
import com.muebleselremanso.elremansov2.model.dto.CartDto;
import com.muebleselremanso.elremansov2.model.entity.Cart;
import com.muebleselremanso.elremansov2.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v2/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Cart>>> findAllCarts() {
        List<Cart> cartList = cartService.findAll();
        ApiResponse<List<Cart>> apiResponse = new ApiResponse<>("Carts found", cartList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cart>> findCartById(@PathVariable Long id) {
        Cart cart = cartService.findById(id);
        ApiResponse<Cart> apiResponse = new ApiResponse<>("Cart found", cart);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Cart>> createCart(@Valid @RequestBody CartDto cartDto) {
        Cart cart = cartService.createCart(cartDto);
        ApiResponse<Cart> apiResponse = new ApiResponse<>("Cart created", cart);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Cart deleted", null);
        return ResponseEntity.ok(apiResponse);
    }

}

