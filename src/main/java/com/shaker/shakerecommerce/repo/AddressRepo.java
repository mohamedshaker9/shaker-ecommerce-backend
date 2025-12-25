package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address,Long> {

    List<Address> findByUserId(Long userId);

    Optional<Address> findByIdAndUserId(Long id, Long userId);
}
