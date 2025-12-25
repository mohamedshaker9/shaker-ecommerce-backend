package com.shaker.shakerecommerce;

import com.shaker.shakerecommerce.enums.AppRole;
import com.shaker.shakerecommerce.model.Role;
import com.shaker.shakerecommerce.repo.RoleRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepo roleRepository;


    public RoleSeeder(RoleRepo roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
//        AppRole[] roleNames = new AppRole[] { AppRole.USER, AppRole.SELLER, AppRole.ADMIN };
//
//
//        Arrays.stream(roleNames).forEach((roleName) -> {
//            Optional<Role> optionalRole = roleRepository.findByName(roleName);
//
//            optionalRole.ifPresentOrElse(System.out::println, () -> {
//                Role roleToCreate = new Role();
//                roleToCreate.setName(roleName);
//                roleRepository.save(roleToCreate);
//            });
//        });
    }
}