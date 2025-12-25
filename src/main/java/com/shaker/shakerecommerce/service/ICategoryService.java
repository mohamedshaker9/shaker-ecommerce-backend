package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.CategoryDTO;
import com.shaker.shakerecommerce.dto.CategoryResponse;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Category;

public interface ICategoryService {

   CategoryResponse getCategories(int pageNumber, int pageSize) throws ResourceNotFoundException;
   Long create(CategoryDTO categoryDto);
   Category getById(Long id) throws ResourceNotFoundException;
   CategoryDTO update(Long id,  CategoryDTO categoryDto) throws ResourceNotFoundException;
   void delete(Long id) throws ResourceNotFoundException;
}
