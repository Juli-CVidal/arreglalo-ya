package com.egg.sp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.sp.entities.Profession;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.ProfessionRepository;

@Service
public class ProfessionService {

		@Autowired
		private ProfessionRepository professionRepository;
		@Transactional
		public void create(Profession profession) throws ServicesException{
			validateServiceType(profession.getName());
			professionRepository.save(profession);
		}
		
		public void validateServiceType(String name) throws ServicesException {
			if ( name == null || name.isEmpty()) {
				throw new ServicesException("Debe ingresar el tipo de servicio que ofrece");
			}
		}
		@Transactional(readOnly = true)
		public List<Profession> findAll(){
			return professionRepository.findAll();
		}
	
}
