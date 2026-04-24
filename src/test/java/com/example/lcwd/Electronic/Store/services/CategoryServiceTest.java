package com.example.lcwd.Electronic.Store.services;

import com.example.lcwd.Electronic.dtos.CategoryDto;
import com.example.lcwd.Electronic.entities.Category;
import com.example.lcwd.Electronic.repositories.CategoryRepository;
import com.example.lcwd.Electronic.services.impl.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private ModelMapper mapper = new ModelMapper();

    Category category;

    @BeforeEach
    public void init(){

        category = Category.builder()
                .categoryId("c1")
                .title("Electronics")
                .description("All electronic items")
                .coverImage("img.png")
                .build();

        ReflectionTestUtils.setField(categoryService, "mapper", mapper);
    }

    // ================= CREATE =================

    @Test
    public void createCategoryTest(){

        Mockito.when(categoryRepository.save(Mockito.any()))
                .thenReturn(category);

        CategoryDto inputDto = mapper.map(category, CategoryDto.class);

        CategoryDto result = categoryService.creat(inputDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getTitle(), result.getTitle());
    }

    // ================= UPDATE =================

    @Test
    public void updateCategoryTest(){

        String categoryId = "c1";

        CategoryDto updateDto = CategoryDto.builder()
                .title("Updated Electronics")
                .description("Updated description")
                .coverImage("updated.png")
                .build();

        Mockito.when(categoryRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(category));

        Mockito.when(categoryRepository.save(Mockito.any()))
                .thenReturn(category);

        CategoryDto result = categoryService.Update(updateDto, categoryId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateDto.getTitle(), result.getTitle());
        Assertions.assertEquals(updateDto.getDescription(), result.getDescription());
    }
}