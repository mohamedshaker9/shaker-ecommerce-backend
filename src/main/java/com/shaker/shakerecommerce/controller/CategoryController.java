package com.shaker.shakerecommerce.controller;


import com.shaker.shakerecommerce.dto.CategoryDTO;
import com.shaker.shakerecommerce.dto.CategoryResponse;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Category;
import com.shaker.shakerecommerce.service.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {


    private final ICategoryService categoryService;


    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(categoryService.getCategories(pageNumber, pageSize));
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody CategoryDTO categoryDto) {
        return new ResponseEntity<>(
                Map.of("CategoryId", categoryService.create(categoryDto)),
                        HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDto) throws ResourceNotFoundException {
        return ResponseEntity.ok(categoryService.update(id, categoryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(categoryService.getById(id));
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws ResourceNotFoundException {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
