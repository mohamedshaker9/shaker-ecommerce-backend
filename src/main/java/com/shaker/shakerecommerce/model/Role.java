package com.shaker.shakerecommerce.model;


import com.shaker.shakerecommerce.enums.AppRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private AppRole name;


}
