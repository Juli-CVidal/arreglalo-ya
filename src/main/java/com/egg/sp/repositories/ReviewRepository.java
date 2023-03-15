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

	@Query("SELECT r FROM Review r WHERE r.supplier.id = :id")
	List<Review> getFromSupplier(@Param("id") Integer id);

	@Query("SELECT r FROM Review r WHERE r.user.id = :id")
	List<Review> getFromCustomer(@Param("id") Integer id);



	//The COALESCE(value1,value2,...,valueN) function returns the first NonNull value on the argument's list
	//In this case, the AVG(r.score) returned null if no reviews were found, therefore COALESCE(null,0) returns 0
	@Query("SELECT COALESCE(AVG(r.score), 0) FROM Review r WHERE r.supplier.id = :supplierId")
	Double getGeneralScore(@Param("supplierId") Integer supplierId);
}