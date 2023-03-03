package com.egg.sp.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.egg.sp.entities.Work;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.WorkRepository;
import com.egg.sp.enums.Acceptance;

@Service
public class WorkService {

	@Autowired
	private WorkRepository workRepository;

	@Transactional
	public void createWork(Work work) throws ServicesException {

		work.setCreateDate(new Date());

		workRepository.save(work);
	}
	@Transactional
	public void responseWork(int workId, Acceptance acceptance, double price) throws ServicesException {

		Optional<Work> response = workRepository.findById(workId);
		if (response.isEmpty()) {
			throw new ServicesException("No existe un trabajo con el identificador solicitado");
		}

		Work work = response.get();
		validatePrice(price);
		work.setAcceptance(acceptance);
		work.setPrice(price);
		workRepository.save(work);
		

	}

	public void validateDescription(String description) throws ServicesException {
		if (description == null || description.isEmpty()) {
			throw new ServicesException("La descripcion no puede estar vacia");
		}
	}

	public void validatePrice(double price) throws ServicesException {
		if (price <= 0) {
			throw new ServicesException("Debe indicar un precio para este trabajo");
		}
	}
}
