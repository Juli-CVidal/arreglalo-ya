package com.egg.sp.entities;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egg.sp.enums.Acceptance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "La descripcion no puede estar vacia")
    @Size(min = 6, max = 50, message = "La descripcion debe tener entre 6 y 50 caracteres")
    private String description;

    @Enumerated(EnumType.STRING)
    private Acceptance acceptance = Acceptance.ENVIADO;

    private double price;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Users supplier;
}
