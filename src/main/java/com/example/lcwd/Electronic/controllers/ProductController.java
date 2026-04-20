package com.example.lcwd.Electronic.controllers;

import com.example.lcwd.Electronic.dtos.*;
import com.example.lcwd.Electronic.services.FileService;
import com.example.lcwd.Electronic.services.ProductService;
import com.example.lcwd.Electronic.repositories.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;
    @Value("images/products")
    private String imageUploadPath;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto){
        ProductDto createdpro=productService.create(productDto);
        return new ResponseEntity<>(createdpro, HttpStatus.CREATED);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updat(@PathVariable String productId, @RequestBody ProductDto productDto){
        ProductDto createdpro=productService.upd(productDto, productId);
        return new ResponseEntity<>(createdpro, HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage>dele(@PathVariable String productId){
        productService.Del(productId);
        ApiResponseMessage mess= ApiResponseMessage.builder()
                .message("Product has been deleted successfully !!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(mess,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId){
        ProductDto createdpro=productService.getPro(productId);
        return new ResponseEntity<>(createdpro, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            Sort sort){

       PageableResponse<ProductDto>res= productService.GetAll(pageNumber, pageSize, sortBy, sortDir);
       return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAlllive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            Sort sort){

        PageableResponse<ProductDto>res= productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>>SearchBy(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            Sort sort){

        PageableResponse<ProductDto>res= productService.SearchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse>Uploadfile(@RequestParam("productImage") MultipartFile image, @PathVariable String productId, HttpServletRequest request ){
//        logger.info("Content-Type: {}", request.getContentType());
//        System.out.println("Controller path: " + imageUploadPath);
        String ImageName= fileService.uploadFile(image, imageUploadPath);

        ProductDto pro =productService.getPro(productId);
        pro.setProductImageName(ImageName);
        ProductDto catr1=productService.upd(pro, productId);
        ImageResponse imageResponse=ImageResponse.builder().imageName(ImageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto pro=productService.getPro(productId);
//        logger.info("Category Image Name :{}",cat.getImageName());
        InputStream resource=fileService.getResource(imageUploadPath,pro.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
