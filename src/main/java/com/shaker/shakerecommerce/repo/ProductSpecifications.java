package com.shaker.shakerecommerce.repo;

import com.shaker.shakerecommerce.dto.ProductsFilter;
import com.shaker.shakerecommerce.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecifications {

    public static Specification<Product> withFilter(ProductsFilter filter) {
        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.q() != null && !filter.q().isBlank()) {
                String likePattern = "%" + filter.q().toLowerCase() + "%";
                predicates.add(
                        builder.or(
                                builder.like(builder.lower(root.get("name")), likePattern),
                                builder.like(builder.lower(root.get("description")), likePattern)
                        )
                );
            }

            if (filter.category() != null) {
                predicates.add(
                        builder.equal(root.get("category").get("name"), filter.category())
                );
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
