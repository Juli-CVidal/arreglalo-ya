package com.egg.sp.repositories;

import com.egg.sp.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
	
	@Query("SELECT s FROM Supplier s WHERE s.id = :id")
    Optional<Supplier> findById(@Param("id") Integer id);

    @Query("SELECT s FROM Supplier s WHERE s.name = :name")
    Optional<Supplier> findByName(@Param("name") String name);

}
