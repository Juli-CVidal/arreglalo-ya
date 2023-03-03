package com.egg.sp.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = true)
    private String content;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    private Boolean readed; //default value for boolean is false


    private Boolean deleted;


    //Messages are to be sent from a supplier to a user or vice versa.
    //Supplier is subclass of user

    /*
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users reciever;
    */
}
