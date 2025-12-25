package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    Category findByName(String name);


}
