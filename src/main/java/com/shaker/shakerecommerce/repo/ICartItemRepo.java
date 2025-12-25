package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepo extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCartId(Long cartId);

    void deleteByCartId(Long cartId);
}