package com.example.lcwd.Electronic.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(){
        super("Resource not found!");
    }
    public ResourceNotFound(String mes){
        super(mes);
    }
}
