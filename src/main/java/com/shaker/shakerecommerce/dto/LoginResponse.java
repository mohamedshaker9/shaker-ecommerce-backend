package com.shaker.shakerecommerce.dto;

import com.shaker.shakerecommerce.enums.AppRole;

import java.util.List;

public record LoginResponse(List<AppRole> roles) {
}
