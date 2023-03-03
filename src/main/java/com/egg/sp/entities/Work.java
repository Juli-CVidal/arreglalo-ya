package com.egg.sp.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	private Date createDate;
	@NotNull
	private int customer_id;
	@NotNull
	private int supplier_id;

}
