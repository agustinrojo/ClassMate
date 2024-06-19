package com.example.storage_service.repository;

import com.example.storage_service.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileRepository extends JpaRepository<File, Long> {
}
