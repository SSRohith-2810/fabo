package com.project.Fabo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Fabo.entity.AdminComments;

public interface AdminCommentsRepository extends JpaRepository<AdminComments, Long>{

	List<String> findNotificationsByStoreCode(String storeCode);

	List<AdminComments> findByStoreCode(String storeCode);

}
