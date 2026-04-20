package com.example.lcwd.Electronic.exceptions;

import com.example.lcwd.Electronic.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponseMessage>resourceNotFoundHandler(ResourceNotFound re){
        logger.info("Exception Hnadler invoked !!");
        ApiResponseMessage res= ApiResponseMessage.builder()
                        .message(re.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true).build();

            return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map<String,Object>mp=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            mp.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage>HandleBadApiRequest(BadApiRequest ex){
        logger.info("Exception Hnadler invoked !!");
        ApiResponseMessage res= ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false).build();

        return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
    }
}
