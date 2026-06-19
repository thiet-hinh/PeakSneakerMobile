package com.example.shoestore.service;


import com.example.shoestore.entity.Cart;
import com.example.shoestore.entity.User;
import com.example.shoestore.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart findByUserId(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
    }


    @Transactional
    public Cart getOrCreate(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = Cart.builder().user(user).build();
                    user.setCart(cart);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public void deleteById(Integer id) {
        cartRepository.deleteById(id);
    }
}

