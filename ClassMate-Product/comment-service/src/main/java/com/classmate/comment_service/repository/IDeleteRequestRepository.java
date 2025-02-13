package com.classmate.comment_service.repository;

import com.classmate.comment_service.entity.DeleteRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeleteRequestRepository extends JpaRepository<DeleteRequest, Long>{
}
