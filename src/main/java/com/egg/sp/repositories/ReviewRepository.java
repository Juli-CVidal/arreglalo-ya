package com.egg.sp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.sp.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer>{

	@Query("SELECT r FROM Review r WHERE r.id = :id")
	Optional<Review> findById(@Param("id") Integer id);

	@Query("SELECT r FROM Review r WHERE r.content = :content")
	Optional<Review> findByContent(@Param("content") String content);
	
	@Query("SELECT r FROM Review r ORDER BY r.score DESC")
	List<Review> orderByScore();

	@Query("SELECT r FROM Review r WHERE r.supplier_id = :id")
	List<Review> getFromSupplier(@Param("id") Integer id);

	@Query("SELECT r FROM Review r WHERE r.customer_id = :id")
	List<Review> getFromCustomer(@Param("id") Integer id);
}
