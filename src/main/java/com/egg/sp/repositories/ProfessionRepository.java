package com.egg.sp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.sp.entities.Profession;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession,Integer>{
	
	@Query("SELECT p FROM Profession p WHERE p.name = :name")
	Optional<Profession> findByName(@Param("name")String name);
}
