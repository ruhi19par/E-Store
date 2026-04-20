package com.example.lcwd.Electronic.services;

import com.example.lcwd.Electronic.dtos.CategoryDto;
import com.example.lcwd.Electronic.dtos.PageableResponse;

public interface CategoryService {
    CategoryDto creat(CategoryDto categoryDto);

    CategoryDto Update(CategoryDto categoryDto, String categoryId);

    void Delete(String CategoryId);

    PageableResponse<CategoryDto>getaLL(int pageNumber, int pageSize, String sortBy, String sortDir);

    CategoryDto get_sing(String CategoryId);
}
