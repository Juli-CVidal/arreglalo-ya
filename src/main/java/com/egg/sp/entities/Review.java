package com.egg.sp.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "review")
public class Review {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String content;
	
	//@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String image;
	
	@NotNull(message = "Inserte su calificacion")
	@Min(value = 1, message = "Por favor ingrese una calificación válida")
	@Max(value = 5, message = "Por favor ingrese una calificación válida")
	private Double score;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@ManyToOne
	@JoinColumn(name = "users_id")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Users supplier;
}
