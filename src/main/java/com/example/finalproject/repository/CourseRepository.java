package com.example.finalproject.repository;

import com.example.finalproject.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByTeacherUsername(String username);

    List<Course> findByCategoryId(Long categoryId);

    Page<Course> findAll(Pageable pageable);

    @Query("SELECT c FROM Course c WHERE " +
           "(:search IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:categoryId IS NULL OR c.category.id = :categoryId)")
    Page<Course> findWithFilters(@Param("search") String search,
                                  @Param("categoryId") Long categoryId,
                                  Pageable pageable);
}
