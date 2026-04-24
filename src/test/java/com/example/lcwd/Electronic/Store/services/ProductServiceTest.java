package com.example.lcwd.Electronic.Store.services;

import com.example.lcwd.Electronic.dtos.ProductDto;
import com.example.lcwd.Electronic.entities.Product;
import com.example.lcwd.Electronic.repositories.ProductRepository;
import com.example.lcwd.Electronic.services.impl.ProductServiceImpl;

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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ModelMapper modelMapper = new ModelMapper();

    Product product;

    @BeforeEach
    public void init(){

        product = Product.builder()
                .title("Laptop")
                .description("Gaming laptop")
                .price(50000)
                .discountedPrice(45000)
                .quantity(10)
                .live(true)
                .stock(true)
                .build();

        ReflectionTestUtils.setField(productService, "mapper", modelMapper);
    }

    // ================= CREATE TEST =================

    @Test
    public void createProductTest(){

        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(product);

        ProductDto inputDto = modelMapper.map(product, ProductDto.class);

        ProductDto result = productService.create(inputDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getTitle(), result.getTitle());
    }

    // ================= UPDATE TEST =================

    @Test
    public void updateProductTest(){

        String productId = "abc";

        ProductDto updateDto = ProductDto.builder()
                .title("Updated Laptop")
                .description("Updated desc")
                .price(60000)
                .discountedPrice(55000)
                .quantity(5)
                .build();

        Mockito.when(productRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(product));

        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(product);

        ProductDto updated = productService.upd(updateDto, productId);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(updateDto.getTitle(), updated.getTitle());
        Assertions.assertEquals(updateDto.getDescription(), updated.getDescription());
    }
}