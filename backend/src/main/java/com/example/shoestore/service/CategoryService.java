package com.example.shoestore.service;

import com.example.shoestore.entity.Category;
import com.example.shoestore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found with slug: " + slug));
    }

    @Transactional
    public Category save(Category category) {
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new RuntimeException("Slug already exists: " + category.getSlug());
        }
        if (category.getParent() != null && category.getParent().getId() != null) {
            Category parent = findById(category.getParent().getId());
            category.setParent(parent);
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Integer id, Category updated) {
        Category existing = findById(id);
        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setDescription(updated.getDescription());
        if (updated.getParent() != null && updated.getParent().getId() != null) {
            Category parent = findById(updated.getParent().getId());
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }
        return categoryRepository.save(existing);
    }
}
