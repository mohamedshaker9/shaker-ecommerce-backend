package com.shaker.shakerecommerce.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
