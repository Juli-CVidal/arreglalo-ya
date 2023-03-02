package com.egg.sp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egg.sp.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "The name cannot be empty")
    @Size(min = 3, max = 30, message = "The name must have between 3 and 30 characters")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "The lastname cannot be empty")
    @Size(min = 3, max = 30, message = "The lastname must have between 3 and 30 characters")
    private String lastname;

    @Column(nullable = false)
    @NotNull(message = "The phoneNumber cannot be empty")
    @Size(min = 7, max = 20, message = "The phoneNumber must have between 7 and 20 characters")
    private String phoneNumber;

    @Column(nullable = false)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotNull(message = "The email cannot be empty")
    private String email;

    @Column(nullable = false, columnDefinition="MEDIUMTEXT")
    private String imagen;

    @Column(nullable = false)
    @NotNull(message = "The password cannot be empty")
    private String password;

    @Column(nullable = false)
    @NotNull(message = "The rol cannot be empty")
    private Rol rol;
    
    @Column(nullable = false)
    @NotNull(message = "The neighborhood cannot be empty")
    private String neighborhood;
    
    @Column(nullable = false)
    @NotNull(message = "The street cannot be empty")
    private String street;
    
    @Column(nullable = false)
    @NotNull(message = "The height cannot be empty")
    private Integer height;
    
    @Column(nullable = false)
    private Boolean state;
    
}
