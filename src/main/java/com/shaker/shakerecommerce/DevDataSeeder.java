package com.shaker.shakerecommerce;

import com.shaker.shakerecommerce.enums.AddressType;
import com.shaker.shakerecommerce.enums.AppRole;
import com.shaker.shakerecommerce.model.*;
import com.shaker.shakerecommerce.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSeeder implements CommandLineRunner {

    private final CategoryRepo categoryRepository;
    private final IProductRepo productRepository;
    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepo addressRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedUsers();
        Map<String, Category> categories = seedCategories();
        seedProducts(categories);

        System.out.println("🌱 Dev data seeded successfully!");
    }

    // -----------------------------------------------------------
    // ROLES
    // -----------------------------------------------------------
    private void seedRoles() {
        if (roleRepository.count() > 0) return;

        roleRepository.save(new Role(null, AppRole.ADMIN));
        roleRepository.save(new Role(null, AppRole.USER));
        roleRepository.save(new Role(null, AppRole.SELLER));

        System.out.println("✔ Roles seeded");
    }

    // -----------------------------------------------------------
    // USERS
    // -----------------------------------------------------------
    private void seedUsers() {
        if (userRepository.existsByEmail("user@example.com")) return;


        // Admin user
        User admin = new User();
        admin.setFullName("Admin Account");
        admin.setUsername("Admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(Set.of(
                roleRepository.findByName(AppRole.USER).get(),
                roleRepository.findByName(AppRole.SELLER).get(),
                roleRepository.findByName(AppRole.ADMIN).get()
        ));
        userRepository.save(admin);

        System.out.println("✔ admin seeded");

        // Normal user
        User user = new User();
        user.setFullName("Test User");
        user.setUsername("testuser");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(Set.of(
                roleRepository.findByName(AppRole.USER).get()
        ));
        userRepository.save(user);

        // Address 1 - Home
        Address userAddress1 = new Address();
        userAddress1.setStreet("123 Main Street");
        userAddress1.setBuildingNumber("Building 5A");
        userAddress1.setCity("New York");
        userAddress1.setState("NY");
        userAddress1.setCountry("USA");
        userAddress1.setZipCode("10001");
        userAddress1.setUser(user);
        userAddress1.setAddressType(AddressType.HOME);
        addressRepository.save(userAddress1);

        // Address 2 - Work
        Address userAddress2 = new Address();
        userAddress2.setStreet("456 Business Avenue");
        userAddress2.setBuildingNumber("Tower B, Floor 12");
        userAddress2.setCity("Los Angeles");
        userAddress2.setState("CA");
        userAddress2.setCountry("USA");
        userAddress2.setZipCode("90001");
        userAddress2.setUser(user);
        userAddress2.setAddressType(AddressType.WORK);
        addressRepository.save(userAddress2);

        // Address 3 - Parents
        Address userAddress3 = new Address();
        userAddress3.setStreet("789 Oak Lane");
        userAddress3.setBuildingNumber("House 23");
        userAddress3.setCity("Chicago");
        userAddress3.setState("IL");
        userAddress3.setCountry("USA");
        userAddress3.setZipCode("60601");
        userAddress3.setUser(user);
        userAddress3.setAddressType(AddressType.OTHER);
        addressRepository.save(userAddress3);


        System.out.println("✔ User address seeded");


        // Seller user
        User seller = new User();
        seller.setFullName("Seller Account");
        seller.setUsername("seller1");
        seller.setEmail("seller@example.com");
        seller.setPassword(passwordEncoder.encode("seller123"));
        seller.setRoles(Set.of(
                roleRepository.findByName(AppRole.USER).get(),
                roleRepository.findByName(AppRole.SELLER).get()
        ));
        userRepository.save(seller);

        System.out.println("✔ Users seeded");
    }

    // -----------------------------------------------------------
    // CATEGORIES
    // -----------------------------------------------------------
    private Map<String, Category> seedCategories() {

        if (categoryRepository.count() > 0) {
            List<Category> existing = categoryRepository.findAll();
            return mapByName(existing);
        }

        List<Category> list = List.of(
                new Category(null, "Electronics"),
                new Category(null, "Books"),
                new Category(null, "Clothing"),
                new Category(null, "Home Appliances"),
                new Category(null, "Beauty & Personal Care")
        );

        List<Category> saved = categoryRepository.saveAll(list);

        System.out.println("✔ Categories seeded");
        return mapByName(saved);
    }

    private Map<String, Category> mapByName(List<Category> categories) {
        Map<String, Category> map = new HashMap<>();
        for (Category c : categories) {
            map.put(c.getName(), c);
        }
        return map;
    }

    // -----------------------------------------------------------
    // PRODUCTS
    // -----------------------------------------------------------
    private void seedProducts(Map<String, Category> categories) {

        if (productRepository.count() > 0) return;

        List<Product> products = List.of(
                new Product(null, "Wireless Mouse",
                        "Ergonomic wireless mouse with adjustable DPI.",
                        120, 79.99, 10.0, 69.99,
                        "mouse.jpg",
                        categories.get("Electronics")),

                new Product(null, "Mechanical Keyboard",
                        "RGB backlit mechanical keyboard with blue switches.",
                        50, 299.99, 15.0, 249.99,
                        "keyboard.jpg",
                        categories.get("Electronics")),

                new Product(null, "Noise Cancelling Headphones",
                        "Active noise cancelling headphones with long battery life.",
                        30, 499.99, 20.0, 399.99,
                        "headphones.jpg",
                        categories.get("Electronics")),

                new Product(null, "Java Programming Book",
                        "Comprehensive guide to Java, Spring Boot & microservices.",
                        200, 129.99, 0.0, 129.99,
                        "java-book.jpg",
                        categories.get("Books")),

                new Product(null, "Clean Code",
                        "A Handbook of Agile Software Craftsmanship by Robert C. Martin.",
                        100, 99.99, 5.0, 94.99,
                        "clean-code.jpg",
                        categories.get("Books")),

                new Product(null, "Men's Cotton T-Shirt",
                        "100% cotton premium t-shirt.",
                        300, 39.99, 10.0, 35.99,
                        "tshirt.jpg",
                        categories.get("Clothing")),

                new Product(null, "Women's Hoodie",
                        "Warm soft hoodie with front pockets.",
                        80, 129.99, 0.0, 129.99,
                        "hoodie.jpg",
                        categories.get("Clothing")),

                new Product(null, "Air Fryer 4L",
                        "Healthy cooking air fryer with digital controls.",
                        25, 399.99, 20.0, 319.99,
                        "airfryer.jpg",
                        categories.get("Home Appliances")),

                new Product(null, "Electric Kettle",
                        "Stainless steel electric kettle 1.7 L.",
                        60, 89.99, 0.0, 89.99,
                        "kettle.jpg",
                        categories.get("Home Appliances")),

                new Product(null, "Face Serum",
                        "Vitamin C face serum for glowing skin.",
                        40, 59.99, 10.0, 53.99,
                        "serum.jpg",
                        categories.get("Beauty & Personal Care")),

                new Product(null, "Hair Dryer",
                        "Professional ionic hair dryer 2000W.",
                        45, 149.99, 5.0, 142.99,
                        "hairdryer.jpg",
                        categories.get("Beauty & Personal Care"))
        );

        productRepository.saveAll(products);

        System.out.println("✔ Products seeded");
    }
}