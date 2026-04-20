package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
