package com.example.finalproject.service;

import com.example.finalproject.dto.request.CategoryRequest;
import com.example.finalproject.entity.Category;
import com.example.finalproject.exception.BadRequestException;
import com.example.finalproject.exception.ResourceNotFoundException;
import com.example.finalproject.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NurbolatDjumadilovCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private NurbolatDjumadilovCategoryService categoryService;

    @Test
    void getAll_shouldReturnAllCategories() {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setTitle("Programming");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setTitle("Design");

        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Category> result = categoryService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Programming");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void create_shouldThrow_whenTitleExists() {
        CategoryRequest request = new CategoryRequest();
        request.setTitle("Programming");

        when(categoryRepository.existsByTitle("Programming")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void create_shouldReturnCategory_whenValid() {
        CategoryRequest request = new CategoryRequest();
        request.setTitle("Data Science");
        request.setDescription("ML and analytics");

        Category saved = new Category();
        saved.setId(1L);
        saved.setTitle("Data Science");

        when(categoryRepository.existsByTitle("Data Science")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryService.create(request);

        assertThat(result.getTitle()).isEqualTo("Data Science");
    }
}
