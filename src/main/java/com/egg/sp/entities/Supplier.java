package com.egg.sp.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "supplier")
public class Supplier extends Users{

    @NotBlank
    @Size(min=20, message= "Please enter a longer description")
    @Size(max=255, message= "Please enter a shorter description")
    private String biography;

}
