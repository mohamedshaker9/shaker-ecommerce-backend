package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.CategoryDTO;
import com.shaker.shakerecommerce.dto.CategoryResponse;
import com.shaker.shakerecommerce.exceptions.APIException;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Category;
import com.shaker.shakerecommerce.repo.CategoryRepo;
import com.shaker.shakerecommerce.repo.IProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepo categoryRepo;

    private final IProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryService(CategoryRepo categoryRepo, IProductRepo productRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getCategories(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

       Page<Category> categoriesPage = categoryRepo.findAll(pageable);


        List<CategoryDTO> categoryDTOS = categoriesPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

         return CategoryResponse.builder()
                 .categories(categoryDTOS)
                 .pageNumber(pageNumber)
                 .pageSize(pageSize)
                 .totalPages(categoriesPage.getTotalPages())
                 .total(categoriesPage.getTotalElements())
                 .isLastPage(categoriesPage.isLast())
                 .build();
    }

    @Override
    public Long create(CategoryDTO categoryDto) {
        Category existingCategoryByName = categoryRepo.findByName(categoryDto.getName());
        if (existingCategoryByName != null) {
           throw new APIException("Category already exists with name "  + categoryDto.getName());
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        Category newCategory = categoryRepo.save(category);

        return newCategory.getId();
    }

    @Override
    public Category getById(Long id) throws ResourceNotFoundException {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", id));
    }



    @Override
    public CategoryDTO update(Long id, CategoryDTO categoryDto) throws ResourceNotFoundException {
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", id));
        existingCategory.setName(categoryDto.getName());
        categoryRepo.save(existingCategory);
        return modelMapper.map(existingCategory, CategoryDTO.class);

    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", id));
        if(productRepo.existsProductByCategory(existingCategory)){
            throw new APIException("Category Can't be deleted as it has products related to it");
        }
        categoryRepo.deleteById(id);
    }
}
