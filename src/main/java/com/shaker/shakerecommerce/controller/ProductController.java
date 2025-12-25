package com.shaker.shakerecommerce.controller;

import com.shaker.shakerecommerce.dto.ProductDTO;
import com.shaker.shakerecommerce.dto.ProductResponse;
import com.shaker.shakerecommerce.dto.ProductsFilter;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/category/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                  @PathVariable Long categoryId) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.add(productDTO, categoryId), HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
             ProductsFilter productsFilter
            ) throws ResourceNotFoundException {
            return new ResponseEntity<>(productService.get(productsFilter), HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getByCategory(@PathVariable Long categoryId) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.getByCategory(categoryId), HttpStatus.FOUND);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> searchByKeyword(@PathVariable String keyword) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.getByKeyword(keyword), HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO,
                                             @PathVariable Long productId) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.update(productDTO, productId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) throws ResourceNotFoundException {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<String> uploadProductImage(@RequestParam("image") MultipartFile multipartFile,
                                                     @PathVariable Long productId
    ) throws IOException, ResourceNotFoundException {
            return ResponseEntity.ok(productService.uploadImage(productId, multipartFile));
    }



}
