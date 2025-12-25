package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.ProductDTO;
import com.shaker.shakerecommerce.dto.ProductResponse;
import com.shaker.shakerecommerce.dto.ProductsFilter;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {

    ProductDTO add(ProductDTO productDTO, Long categoryId) throws ResourceNotFoundException;

    ProductResponse get(ProductsFilter productsFilter) throws ResourceNotFoundException;

    ProductResponse getByCategory(Long categoryId) throws ResourceNotFoundException;

    ProductResponse getByKeyword(String keyword) throws ResourceNotFoundException;

    ProductDTO update(ProductDTO productDTO, Long productId) throws ResourceNotFoundException;

    void delete(Long productId) throws ResourceNotFoundException;

    String uploadImage(Long productId, MultipartFile file) throws IOException, ResourceNotFoundException;
}
