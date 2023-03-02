package com.egg.sp.repositories;

import com.egg.sp.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    @Query("SELECT u FROM Users u WHERE u.id = :id")
    Optional<Users> getById(@Param("id") String id);

    @Query("SELECT u FROM Users u WHERE u.name = :name")
    Optional<Users> getByName(@Param("name") String name);
}