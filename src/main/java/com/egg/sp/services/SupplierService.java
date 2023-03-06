package com.egg.sp.services;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.egg.sp.entities.Supplier;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.SupplierRepository;

@Service
public class SupplierService {

	@Autowired
	private SupplierRepository supplierRepository;
	
    // ======== CREATE ========

	public void create(Supplier supplier) throws ServicesException {
		if (supplier.getRol() == Rol.SUPPLIER) {
			validateSupplierFields(supplier.getBiography());
		}
		supplierRepository.save(supplier);
	}

	private void validateSupplierFields(String biography) throws ServicesException {
		if (biography.isEmpty() || biography.isBlank() || biography == null) {
			throw new ServicesException("No ha ingresado una biografia");
		}
	}
	
    // ======= READ ========

	@Transactional(readOnly = true)
	public List<Supplier> getAll() {
		return supplierRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Supplier findById(@NotNull Integer id) throws ServicesException {
		return getFromOptional(supplierRepository.findById(id));
	}

	@Transactional(readOnly = true)
	public Supplier findByName(@NotNull String name) throws ServicesException {
		return getFromOptional(supplierRepository.findByName(name));
	}

	private Supplier getFromOptional(Optional<Supplier> supplierOpt) throws ServicesException {
		if (supplierOpt.isPresent()) {
			return supplierOpt.get();
		}
		throw new ServicesException("No se ha encontrado un proveedor");
	}
	
    // ======== UPDATE ========

	@Transactional
	public void update(Supplier supplier) throws ServicesException {
		create(supplier);
	}
	
    // ======== DELETE ========

	@Transactional
    public void delete(@NotNull Integer id) throws ServicesException{
    	Supplier supplier = findById(id);
    	supplier.setState(false);
        supplierRepository.save(supplier);
	}
    
}
