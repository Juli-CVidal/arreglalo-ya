package com.egg.sp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

import com.egg.sp.enums.Provider;
import com.egg.sp.enums.Rol;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    protected String name;

    @Size(min = 3, max = 30, message = "El apellido debe tener entre 3 y 30 caracteres")
    protected String lastname;

    @Size(min = 7, max = 20, message = "El número debe tener entre 7 y 20 digitos")
    protected String phoneNumber;

    @Column(unique = true)
    protected String email;

    @Column(columnDefinition = "MEDIUMTEXT")
    protected String image;

    protected String password;

    @Enumerated(EnumType.STRING)
    protected Rol rol;
    
    @Enumerated(EnumType.STRING)
    @Column(name="auth_provider")
    protected Provider provider;

    private Boolean state;

    // ======= ROL.CUSTOMER ========

    protected String neighborhood;

    protected String street;

    protected Integer height;

    // ======= ROL.SUPPLIER ========

    protected String biography;

    private String profession;

    private Double generalScore;

}
