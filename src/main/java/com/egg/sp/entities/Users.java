package com.egg.sp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

import com.egg.sp.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Inserte su nombre")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    protected String name;

    @Column(nullable = false)
    @NotBlank(message = "Inserte su apellido")
    @Size(min = 3, max = 30, message = "El apellido debe tener entre 3 y 30 caracteres")
    protected String lastname;

    @Column(nullable = false)
    @NotBlank(message = "Inserte su número de teléfono")
    @Size(min = 7, max = 20, message = "El número debe tener entre 7 y 20 digitos")
    protected String phoneNumber;

    @Column(nullable = false, unique = true)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "Inserte un mail válido")
    protected String email;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    protected String image;

    @Column(nullable = false)
    @NotBlank(message = "Inserte una contraseña")
    protected String password;

    protected Rol rol;

    //Proper to the Rol.CUSTOMER
    protected String neighborhood;

    //Proper to the Rol.CUSTOMER
    protected String street;

    //Proper to the Rol.CUSTOMER
    protected Integer height;

    //Proper to the Rol.SUPPLIER
    protected String biography;

    //Proper to the Rol.SUPPLIER
    private String principalService;

    @Column(nullable = false)
    protected Boolean state;
}
