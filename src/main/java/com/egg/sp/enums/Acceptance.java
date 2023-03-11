package com.egg.sp.enums;

public enum Acceptance {
	//ENVIADO: el cliente le ha enviado la solicitud al proveedor
	//PRESUPUESTADO: el proveedor ha aceptado la propuesta y le ha asignado un precio
	//ACEPTADO: el cliente aceptó el presupuesto
	//RECHAZADO: ambas partes pueden rechazar
	ACEPTADO, RECHAZADO, ENVIADO, FINALIZADO, PRESUPUESTADO;
}
