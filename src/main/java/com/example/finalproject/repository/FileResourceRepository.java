package com.example.finalproject.repository;

import com.example.finalproject.entity.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileResourceRepository extends JpaRepository<FileResource, Long> {

    List<FileResource> findByCourseId(Long courseId);
}
