package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.Category;
import com.example.lcwd.Electronic.entities.Product; // ✅ CORRECT
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product>findByTitleContaining(String Title, Pageable pageable);
   Page<Product>findByLiveTrue(Pageable pageable);
    Page<Product>findByCategory(Category category, Pageable pageable);
}
