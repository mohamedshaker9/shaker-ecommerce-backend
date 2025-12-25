package com.shaker.shakerecommerce.model;


import com.shaker.shakerecommerce.enums.AddressType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @NotBlank
    @Size(min = 5)
    private String street;

    @NotBlank
    @Size(min = 5)
    private String buildingNumber;

    @NotBlank
    @Size(min = 5)
    private String city;

    @NotBlank
    @Size(min = 2)
    private String state;

    @NotBlank
    @Size(min = 2)
    private String Country;

    @NotBlank
    @Size(min = 2)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
