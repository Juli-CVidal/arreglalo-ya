package com.egg.sp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "Debe ingresar el contenido del mensaje")
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    private Boolean readed; //default value for boolean is false

    private Boolean deleted;

    //Messages are to be sent from a supplier to a user or vice versa.
    //Supplier is subclass of user
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "reciever_id")
    private Users reciever;

}
