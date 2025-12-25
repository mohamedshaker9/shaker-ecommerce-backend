package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.model.Order;
import com.shaker.shakerecommerce.model.User;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    Optional<Order> findByIdAndUser(Long id, User user);


    @Query("select coalesce(sum(o.totalAmount), 0) from Order o")
    BigDecimal sumTotalRevenue();


}