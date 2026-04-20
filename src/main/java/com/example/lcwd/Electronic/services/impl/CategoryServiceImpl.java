package com.example.lcwd.Electronic.services.impl;

import com.example.lcwd.Electronic.dtos.CategoryDto;
import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.entities.Category;
import com.example.lcwd.Electronic.exceptions.ResourceNotFound;
import com.example.lcwd.Electronic.helper.Helper;
import com.example.lcwd.Electronic.repositories.CategoryRepository;
import com.example.lcwd.Electronic.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${category.profile.image.path}")
    private String imagePath;
    @Override
    public CategoryDto creat(CategoryDto categoryDto) {

        String catId= UUID.randomUUID().toString();
        categoryDto.setCategoryId(catId);
        Category category=mapper.map(categoryDto, Category.class);
        Category us=categoryRepository.save(category);
        return mapper.map(us,CategoryDto.class);
    }

    @Override
    public CategoryDto Update(CategoryDto categoryDto, String categoryId) {
       Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category with the given id is not found"));
       category.setTitle(categoryDto.getTitle());
       category.setDescription(categoryDto.getDescription());
       category.setCoverImage(categoryDto.getCoverImage());

       Category ct=categoryRepository.save(category);
       return mapper.map(ct,CategoryDto.class);

    }

    @Override
    public void Delete(String categoryId) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category with the given id is not found"));
        String fullPath = System.getProperty("user.dir")
                + File.separator
                + imagePath
                + File.separator
                + category.getCoverImage();

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

        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getaLL(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> page=categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto>pageableResponse  =Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get_sing(String categoryId) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category with the given id is not found"));
        return mapper.map(category, CategoryDto.class);
    }

}
