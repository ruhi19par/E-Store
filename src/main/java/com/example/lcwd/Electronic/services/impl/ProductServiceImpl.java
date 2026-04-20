package com.example.lcwd.Electronic.services.impl;

import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.ProductDto;
import com.example.lcwd.Electronic.entities.Category;
import com.example.lcwd.Electronic.exceptions.ResourceNotFound;
import com.example.lcwd.Electronic.helper.Helper;
import com.example.lcwd.Electronic.repositories.CategoryRepository;
import com.example.lcwd.Electronic.repositories.ProductRepository;
import com.example.lcwd.Electronic.services.ProductService;

import com.example.lcwd.Electronic.entities.Product;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
@Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String imagePath;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product pro=mapper.map(productDto, Product.class);
        String id= UUID.randomUUID().toString();
        pro.setProductId(id);   // ✅ correct
        pro.setAddedDate(new Date());
       Product pr1= productRepository.save(pro);
        return mapper.map(pr1, ProductDto.class);
    }

    @Override
    public ProductDto upd(ProductDto productDto, String productId) {
        Product pr=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product with this id is not found"));
        pr.setTitle(productDto.getTitle());
        pr.setDescription(productDto.getDescription());
        pr.setPrice(productDto.getPrice());
        pr.setDiscountedPrice(productDto.getDiscountedPrice());
        pr.setQuantity(productDto.getQuantity());
        pr.setLive(productDto.isLive());
        pr.setStock(productDto.isStock());
        pr.setProductImageName(productDto.getProductImageName());
        Product updated = productRepository.save(pr);

        return mapper.map(updated, ProductDto.class);

    }

    @Override
    public void Del(String productId) {
        Product pr=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product with this id is not found"));

        String fullPath = System.getProperty("user.dir")
                + File.separator
                + imagePath
                + File.separator
                + pr.getProductImageName();

        try {
            Path path = Paths.get(fullPath);

            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("File deleted successfully: {}", fullPath);
            } else {
                logger.warn("File not found: {}", fullPath);
            }

        } catch (IOException ex) {
            logger.error("Error deleting file: {}", fullPath, ex);
        }


        productRepository.delete(pr);
    }

    @Override
    public ProductDto getPro(String ProductId) {
        Product pr=productRepository.findById(ProductId).orElseThrow(()->new RuntimeException("Product with this id is not found"));
        return mapper.map(pr, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> GetAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize, sort);
       Page<Product> page= productRepository.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize, sort);
        Page<Product>page=productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);


    }

    @Override
    public PageableResponse<ProductDto> SearchByTitle(String title,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize, sort);
        Page<Product>page=productRepository.findByTitleContaining(title,pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category with your given id is not found"));
        Product pro=mapper.map(productDto, Product.class);
        String id= UUID.randomUUID().toString();
        pro.setProductId(id);   // ✅ correct
        pro.setAddedDate(new Date());
        pro.setCategory(category);
        Product pr1= productRepository.save(pro);
        return mapper.map(pr1, ProductDto.class);

    }

    @Override
    public ProductDto UpdatePrCategory(String productId, String categoryId) {
        Product pr=productRepository.findById(productId).orElseThrow(()->new ResourceNotFound("product with given id doesn;t exist"));
        Category cat=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFound("Category with given id is not found"));
        pr.setCategory(cat);
        Product sa=productRepository.save(pr);
        return mapper.map(sa,ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> giveAllProductsOfCategory(String categoryId,int pageNumber,int pageSize, String sortBy, String sortDir) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFound("Category with given id is not found"));

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
    Page<Product>p=productRepository.findByCategory(category, pageable);

    return Helper.getPageableResponse(p, ProductDto.class);
    }
}
