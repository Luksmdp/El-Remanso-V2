package com.muebleselremanso.elremansov2.service;

import com.muebleselremanso.elremansov2.configuration.RabbitMQConfig;
import com.muebleselremanso.elremansov2.exception.CartNotFoundException;
import com.muebleselremanso.elremansov2.exception.CategoryNotFoundException;
import com.muebleselremanso.elremansov2.exception.NoCategoriesFoundException;
import com.muebleselremanso.elremansov2.exception.ProductNotFoundException;
import com.muebleselremanso.elremansov2.model.dto.CartDto;
import com.muebleselremanso.elremansov2.model.entity.Cart;
import com.muebleselremanso.elremansov2.model.entity.CartItem;
import com.muebleselremanso.elremansov2.model.entity.Product;
import com.muebleselremanso.elremansov2.repository.CartRepository;
import com.muebleselremanso.elremansov2.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final RabbitTemplate rabbitTemplate;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart createCart(CartDto cartDto) {
        // Convertir el DTO a entidad
        Cart cart = convertToCart(cartDto);

        // Guardar el carrito en la base de datos
        Cart savedCart = cartRepository.save(cart);

        // Publicar el evento a RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_CART,
                RabbitMQConfig.ROUTING_KEY_CART,
                savedCart
        );

        return savedCart;
    }

    @Override
    public void deleteCart(Long cartId){
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        }else {
            throw new CartNotFoundException("The Cart with id: " +cartId+ " was not found");
        }
    }

    @Override
    public List<Cart> findAll() {
        List<Cart> cartList = cartRepository.findAll();
        if (cartList.isEmpty()){
            throw new NoCategoriesFoundException("No carts found in the database");
        }
        return cartList;
    }

    @Override
    public Cart findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isEmpty()){
            throw new CategoryNotFoundException("The Cart with id: "+id+" was not found");
        }
        return cartOptional.get();
    }

    private Cart convertToCart(CartDto cartDto) {
        List<CartItem> items = cartDto.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("No se encontró el producto con id: " + itemDto.getProductId()));
                    return CartItem.builder()
                            .product(product)
                            .quantity(itemDto.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());
        return Cart.builder().items(items).build();
    }
}

