package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.enums.OrderStatus;
import com.shaker.shakerecommerce.model.Order;
import com.shaker.shakerecommerce.model.User;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {

    private OrderSpecifications() {
    }

    public static Specification<Order> withOptionalStatus(OrderStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }
}