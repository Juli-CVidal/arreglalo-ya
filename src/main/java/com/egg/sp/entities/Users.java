package com.egg.sp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Inserte su nombre")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Inserte su apellido")
    @Size(min = 3, max = 30, message = "El apellido debe tener entre 3 y 30 caracteres")
    private String lastname;

    @Column(nullable = false)
    @NotNull(message = "Inserte su número de teléfono")
    @Size(min = 7, max = 20, message = "El número debe tener entre 7 y 20 digitos")
    private String phoneNumber;

    @Column(nullable = false)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotNull(message = "Inserte un mail válido")
    private String email;

    @Column(nullable = false, columnDefinition="MEDIUMTEXT")
    private String imagen;

    @Column(nullable = false)
    @NotNull(message = "Inserte una contraseña")
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Inserte un rol")
    private Rol rol;
    
    @Column(nullable = false)
    @NotNull(message = "Inserte su barrio")
    private String neighborhood;
    
    @Column(nullable = false)
    @NotNull(message = "Inserte su calle")
    private String street;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Inserte un número válido")
    @NotNull(message = "Ingrese su altura")
    private Integer height;
    
    @Column(nullable = false)
    private Boolean state;
    
}
