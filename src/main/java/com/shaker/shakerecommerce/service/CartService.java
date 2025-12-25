
package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.*;
import com.shaker.shakerecommerce.exceptions.BusinessException;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Cart;
import com.shaker.shakerecommerce.model.CartItem;
import com.shaker.shakerecommerce.model.Product;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.repo.ICartItemRepo;
import com.shaker.shakerecommerce.repo.CartRepo;
import com.shaker.shakerecommerce.repo.IProductRepo;
import com.shaker.shakerecommerce.repo.UserRepo;
import com.shaker.shakerecommerce.shared.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepo userRepo;
    private final IProductRepo productRepo;
    private final CartRepo cartRepo;
    private final ICartItemRepo cartItemRepo;
    private final AuthUtils authUtils;

    @Transactional
    public Long save(List<CartItemRequestDto> cartItemRequests) throws ResourceNotFoundException {

        if (cartItemRequests == null || cartItemRequests.isEmpty()) {
            throw new BusinessException("Cart cannot be empty");
        }

        User user = authUtils.getLoggedInUser();


        Cart cart = cartRepo.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return newCart;
                });


        if (cart.getId() != null) {
            cart.getItems().clear();
        }

        double subtotal = 0.0;
        double totalDiscount = 0.0;


        for (CartItemRequestDto itemRequest : cartItemRequests) {

            Product product = productRepo.findById(itemRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemRequest.getId()));

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getQuantity() + ", Requested: " + itemRequest.getQuantity());
            }

            double itemPrice = product.getSpecialPrice() != null ? product.getSpecialPrice() : product.getPrice();
            double itemDiscount = product.getDiscount() != null ? product.getDiscount() : 0.0;
            double itemTotal = itemPrice * itemRequest.getQuantity();
            double itemDiscountAmount = (product.getPrice() - itemPrice) * itemRequest.getQuantity();


            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setPrice(itemPrice);
            cartItem.setQuantity(itemRequest.getQuantity());
            cartItem.setDiscount(itemDiscount);
            cart.getItems().add(cartItem);

            subtotal += itemTotal;
            totalDiscount += itemDiscountAmount;
        }

        cart.setTotal(subtotal);
        cart.setDiscount(totalDiscount);

        Cart savedCart = cartRepo.save(cart);

        return savedCart.getId();
    }

    public CartDto getCartById(Long cartId) throws ResourceNotFoundException {
        Cart cart = cartRepo.findById(cartId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        return mapCartToDto(cart);
    }

    public CartDto getCartForCurrentUser() throws ResourceNotFoundException {
        User user = authUtils.getLoggedInUser();

        Cart cart = cartRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "user", user.getId()));

        return mapCartToDto(cart);
    }

    private CartDto mapCartToDto(Cart cart) {
        List<CartItemDto> cartItemDtos = cart.getItems().stream()
                .map(this::mapCartItemToDto)
                .collect(Collectors.toList());

        return new CartDto(
                cart.getId(),
                cartItemDtos,
                cart.getTotal(),
                cart.getDiscount()
        );
    }

    private CartItemDto mapCartItemToDto(CartItem cartItem) {
        Product product = cartItem.getProduct();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setProductName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setSpecialPrice(product.getSpecialPrice());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setImage(product.getImage());
        productDTO.setQuantity(product.getQuantity());

        double totalPrice = cartItem.getPrice() * cartItem.getQuantity();

        return new CartItemDto(
                cartItem.getId(),
                productDTO,
                cartItem.getQuantity(),
                cartItem.getDiscount(),
                cartItem.getPrice(),
                totalPrice
        );
    }
}