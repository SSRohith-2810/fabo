package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.Fabo.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
	@Query("SELECT i.imageData FROM ImageEntity i WHERE i.category = :category")
    List<byte[]> findImagesByCategory(@Param("category") String category);
}
