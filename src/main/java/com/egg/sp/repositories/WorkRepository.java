package com.egg.sp.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.egg.sp.entities.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work,Integer>{
	
	@Query("SELECT w FROM Work w WHERE w.id = :id")
    Optional<Work> findById(@Param("id") Integer id);
	
	@Query("SELECT w FROM Work w WHERE w.date = date")
	Optional<Work> getByDate(@Param("date")String date);
}
