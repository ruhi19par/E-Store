package com.example.lcwd.Electronic.helper;

import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static <U,V>PageableResponse<V> getPageableResponse(Page<U> page, Class<V>type){
        List<U> user = page.getContent();

        List<V>ls1=user.stream().map(use -> new ModelMapper().map(use,type)).collect(Collectors.toUnmodifiableList());

        PageableResponse<V>data=new PageableResponse<>();
        data.setContent(ls1);
        data.setPageNumber(page.getNumber());
        data.setPageSize(page.getSize());
        data.setTotalElements(page.getTotalElements());
        data.setTotalPages(page.getTotalPages());
        data.setLastPage(page.isLast());
        return data;
    }
}
