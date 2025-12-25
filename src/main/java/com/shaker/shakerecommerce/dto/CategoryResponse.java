package com.shaker.shakerecommerce.dto;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class CategoryResponse {

    private List<CategoryDTO> categories;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long total;
    private boolean isLastPage;

}
