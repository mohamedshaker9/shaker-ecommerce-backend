package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.enums.AppRole;
import com.shaker.shakerecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;


interface RoleRepository extends JpaRepository<Role, Integer> {
    Set<Role> findAllByNameIn(Collection<AppRole> name);

    Role findByName(AppRole appRole);
}
