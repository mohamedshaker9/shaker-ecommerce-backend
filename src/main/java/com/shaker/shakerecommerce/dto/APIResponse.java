package com.shaker.shakerecommerce.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class APIResponse {

    private String message;
    private boolean status;
}
