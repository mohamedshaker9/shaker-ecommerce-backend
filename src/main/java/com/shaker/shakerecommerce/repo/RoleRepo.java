package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.enums.AppRole;
import com.shaker.shakerecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Integer> {

        Optional<Role> findByName(AppRole name);
}
