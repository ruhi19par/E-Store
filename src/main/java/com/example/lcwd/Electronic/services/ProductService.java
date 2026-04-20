package com.example.lcwd.Electronic.services;

import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productDto);

    ProductDto upd(ProductDto productDto, String productId);

    void Del(String productId);

    ProductDto getPro(String ProductId);

    PageableResponse<ProductDto> GetAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto>getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto>SearchByTitle(String title,int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    ProductDto UpdatePrCategory(String productId, String categoryId);

    PageableResponse<ProductDto> giveAllProductsOfCategory(String categoryId,int pageNumber,int pageSize, String sortBy, String sortDir);
}
