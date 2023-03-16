package com.egg.sp.repositories;

import com.egg.sp.entities.Users;
import com.egg.sp.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u WHERE u.id = :id")
    Optional<Users> findById(@Param("id") Integer id);

    @Query("SELECT u FROM Users u WHERE u.id = :id AND u.rol = com.egg.sp.enums.Rol.SUPPLIER")
    Optional<Users> findSupplierById(@Param("id") Integer id);

    @Query("SELECT u FROM Users u WHERE u.name = :name AND u.rol = com.egg.sp.enums.Rol.SUPPLIER")
    Optional<Users> findSupplierByName(@Param("name") String name);

    @Query("SELECT u FROM Users u WHERE u.name = :name")
    Optional<Users> findByName(@Param("name") String name);

    @Query("SELECT u FROM Users u WHERE u.rol = :rol")
    List<Users> findAllByRol(Rol rol);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    public Users findByEmail(@Param("email") String email);

    public Users findByResetPasswordToken(String token);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM Users u WHERE u.phoneNumber = :phoneNumber")
    Optional<Users> findByNumberPhone(@Param("phoneNumber") String phoneNumber);
}
