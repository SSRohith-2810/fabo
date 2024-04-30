package com.project.Fabo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Fabo.entity.InvoiceFile;

@Repository
public interface InvoiceFileRepository extends JpaRepository<InvoiceFile, Long> {
    // You can define custom query methods if needed
}