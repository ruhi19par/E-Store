package com.example.lcwd.Electronic.controllers;

import com.example.lcwd.Electronic.dtos.*;
import com.example.lcwd.Electronic.services.CategoryService;
import com.example.lcwd.Electronic.services.FileService;
import com.example.lcwd.Electronic.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private Logger logger= LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> crea(@Valid @RequestBody CategoryDto categoryDto){

    CategoryDto us=categoryService.creat(categoryDto);
    return new ResponseEntity<>(us, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
@PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>update(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
    CategoryDto cat=categoryService.Update(categoryDto, categoryId);
    return new ResponseEntity<>(cat, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage>dele(@PathVariable String categoryId){
        categoryService.Delete(categoryId);
       ApiResponseMessage ap= ApiResponseMessage.builder().message("Category has been deleted Successfully !!").status(HttpStatus.OK).success(true).build();
       return new ResponseEntity<>(ap,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){

       PageableResponse<CategoryDto>pageableResponse= categoryService.getaLL(pageNumber, pageSize, sortBy, sortDir);
       return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
@GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> Get_single(@PathVariable String categoryId){
        CategoryDto categoryDto=categoryService.get_sing(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse>Uploadfile(@RequestParam("categoryImage") MultipartFile image, @PathVariable String categoryId, HttpServletRequest request ){
//        logger.info("Content-Type: {}", request.getContentType());
//        System.out.println("Controller path: " + imageUploadPath);
        String ImageName= fileService.uploadFile(image, imageUploadPath);

        CategoryDto cat =categoryService.get_sing(categoryId);
        cat.setCoverImage(ImageName);
        CategoryDto catr1=categoryService.Update(cat, categoryId);
        ImageResponse imageResponse=ImageResponse.builder().imageName(ImageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto cat=categoryService.get_sing(categoryId);
//        logger.info("Category Image Name :{}",cat.getImageName());
        InputStream resource=fileService.getResource(imageUploadPath,cat.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    // for product creation and add categories ton them
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createWithCategory(
            @PathVariable("categoryId") String categoryId, @RequestBody ProductDto dto
    ){
       ProductDto pr= productService.createWithCategory(dto, categoryId);
       return new ResponseEntity<>(pr,HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategory(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
        ProductDto productDto=productService.UpdatePrCategory(productId,categoryId);

        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProOfCat(@PathVariable String categoryId,@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        PageableResponse<ProductDto>res=productService.giveAllProductsOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


}
