package com.example.finalproject.service;

import com.example.finalproject.dto.request.CategoryRequest;
import com.example.finalproject.entity.Category;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurbolatDjumadilovCategoryService {

    private static final Logger log = LoggerFactory.getLogger(NurbolatDjumadilovCategoryService.class);

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public Category create(CategoryRequest request) {
        if (categoryRepository.existsByTitle(request.getTitle())) {
            throw new BadRequestException("Category with this title already exists");
        }
        Category category = new Category();
        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        log.info("Creating new category: {}", request.getTitle());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category category = getById(id);
        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        log.info("Deleting category: {}", category.getTitle());
        categoryRepository.delete(category);
    }
}
