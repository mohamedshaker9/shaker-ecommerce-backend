package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.model.Category;
import com.shaker.shakerecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    List<Product> findAllByCategory_Id(Long id);

    List<Product> findByNameContainsIgnoreCase(String name);

    boolean existsProductByCategory(Category category);

}
