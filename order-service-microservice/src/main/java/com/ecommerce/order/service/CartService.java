package com.ecommerce.order.service;

//import com.ecommerce.order.clients.ProductServiceClient;
//import com.ecommerce.order.clients.UserServiceClient;
//import com.ecommerce.order.dto.ProductResponseDTO;
//import com.ecommerce.order.dto.UserResponseDTO;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
//    private final ProductServiceClient productServiceClient;
//    private final UserServiceClient userServiceClient;
    int attempt = 0;

    //    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
//    @Retry(name = "retryBreaker", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(Long userId, CartItemRequest request) {
        System.out.println("ATTEMPT COUNT: " + ++attempt);
        // Look for product
//        ProductResponseDTO productResponse = productServiceClient.getProductDetails(request.getProductId());
//        if (productResponse == null || productResponse.getStockQuantity() < request.getQuantity())
//            return false;
//
//        UserResponse userResponse = userServiceClient.getUserDetails(userId);
//        if (userResponse == null)
//            return false;

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, Long.valueOf(request.getProductId()));
        if (existingCartItem != null) {
            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(Long.valueOf(request.getProductId()));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean addToCartFallBack(String userId,
                                     CartItemRequest request,
                                     Exception exception) {
        exception.printStackTrace();
        return false;
    }

    public boolean deleteItemFromCart(Long userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(Long.valueOf(userId));
    }
}