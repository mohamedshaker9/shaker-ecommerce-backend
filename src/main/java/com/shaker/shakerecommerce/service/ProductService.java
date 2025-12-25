package com.shaker.shakerecommerce.service;


import com.shaker.shakerecommerce.dto.ProductDTO;
import com.shaker.shakerecommerce.dto.ProductResponse;
import com.shaker.shakerecommerce.dto.ProductsFilter;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Category;
import com.shaker.shakerecommerce.model.Product;
import com.shaker.shakerecommerce.repo.CategoryRepo;
import com.shaker.shakerecommerce.repo.IProductRepo;
import com.shaker.shakerecommerce.repo.ProductSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.util.List;


@Service
public class ProductService implements IProductService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private IProductRepo IProductRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${products.images.path}")
    private String productImagesPath;

    @Value("${base.front.image.url}")
    private String baseFrontierUrl;


    @Override
    public ProductDTO add(ProductDTO productDTO, Long categoryId) throws ResourceNotFoundException {
        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Product newProduct = modelMapper.map(productDTO, Product.class);

        newProduct.setCategory(existingCategory);
        newProduct.setSpecialPrice(productDTO.getPrice() - (productDTO.getDiscount() * 0.01 * productDTO.getPrice()));
        Product savedProduct = IProductRepo.save(newProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse get(ProductsFilter filter) throws ResourceNotFoundException {
        int pageSize = (filter.pageSize() == null || filter.pageSize() <= 0) ? 5 : filter.pageSize();
        int pageNumber = (filter.pageNumber() == null || filter.pageNumber() < 0) ? 0 : filter.pageNumber();

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                getSort(filter.sortOrder(), filter.sortBy())
        );

        Page<Product> products = IProductRepo.findAll(
                ProductSpecifications.withFilter(filter),
                pageable);

        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(product -> {
                            ProductDTO productDto = modelMapper.map(product, ProductDTO.class);
                            productDto.setCategory(product.getCategory().getName());
                            productDto.setCategoryId(product.getCategory().getId());
                            productDto.setImage(baseFrontierUrl + product.getImage());
                            return productDto;
                        }
                )
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setTotal(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setProducts(productDTOS);

        return productResponse;
    }


    private Sort getSort(String sortOrder, String sortBy) {

        //TODO validations
        if (sortOrder == null || sortOrder.isBlank()) return Sort.unsorted();

        return switch (sortOrder) {
            case "asc" -> Sort.by(sortBy).ascending();
            case "desc" -> Sort.by(sortBy).descending();

            default -> Sort.unsorted();
        };
    }
    @Override
    public ProductResponse getByCategory(Long categoryId) throws ResourceNotFoundException {
       categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        List<Product> products = IProductRepo.findAllByCategory_Id(categoryId);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOS);

        return productResponse;

    }

    @Override
    public ProductResponse getByKeyword(String keyword){
        List<Product> products = IProductRepo.findByNameContainsIgnoreCase(keyword);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOS);

        return productResponse;

    }

    @Override
    public ProductDTO update(ProductDTO productDTO, Long productId) throws ResourceNotFoundException {
        Product existingProduct = IProductRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

       existingProduct.setName(productDTO.getProductName());
       existingProduct.setPrice(productDTO.getPrice());
       existingProduct.setDiscount(productDTO.getDiscount());
       existingProduct.setSpecialPrice(productDTO.getPrice() - (productDTO.getDiscount() * 0.01 * productDTO.getPrice()));

        Product updatedProduct = IProductRepo.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public void delete(Long productId) throws ResourceNotFoundException {
        Product existingProduct = IProductRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        IProductRepo.delete(existingProduct);

    }

    @Override
    public String uploadImage(Long productId, MultipartFile image) throws IOException, ResourceNotFoundException {
        Product existingProduct = IProductRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String newFileName = fileUploadService.uploadProductImage(productImagesPath, image);

        existingProduct.setImage(newFileName);
        IProductRepo.save(existingProduct);

        return newFileName;
    }


}
