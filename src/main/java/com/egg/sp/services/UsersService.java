package com.egg.sp.services;

import com.egg.sp.entities.Users;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private ReviewService reviewService;

    // ======== CREATE ========

    @Transactional
    public void create(Users user) throws ServicesException {
        validateFields(user);
        user.setState(true);
        usersRepository.save(user);
    }
    // ======= READ ========

    @Transactional(readOnly = true)
    public List<Users> getAll() {
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Users> findAllByRol(Rol rol) {
        return usersRepository.findAllByRol(rol);
    }

    @Transactional(readOnly = true)
    public Users findSupplierById(Integer id) throws ServicesException {
        validateId(id);
        return getFromOptional(usersRepository.findSupplierById(id));
    }

    public Users findSupplierByName(String name) throws ServicesException {
        validateName(name);
        return getFromOptional(usersRepository.findSupplierByName(name));
    }

    @Transactional(readOnly = true)
    public Users findById(@NotNull Integer id) throws ServicesException {
        return getFromOptional(usersRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public Users findByName(@NotNull String name) throws ServicesException {
        return getFromOptional(usersRepository.findByName(name));
    }
    @Transactional(readOnly = true)
    public Double getGeneralScore(Integer idSupplier) {
    	return reviewService.averageRating(idSupplier);
    }


    // ======== UPDATE ========

    @Transactional
    public void update(Users user) throws ServicesException {
        create(user);
    }

    // ======== DELETE ========

    @Transactional
    //soft delete (change state to false)
    public void delete(@NotNull Integer id) throws ServicesException {
        Users user = findById(id);
        user.setState(false);
        usersRepository.save(user);
    }


    //VALIDATIONS
    private void validateFields(Users user) throws ServicesException {
        if (user.getRol() == Rol.CUSTOMER) {
            validateCustomerFields(user.getNeighborhood(), user.getStreet(), user.getHeight());
        } else if (user.getRol() == Rol.SUPPLIER) {
            validateSupplierFields(user.getBiography(), user.getProfession());
        }
    }

    private void validateCustomerFields(String neighborhood, String street, Integer height) throws ServicesException {
        if (null == neighborhood || neighborhood.isBlank()) {
            throw new ServicesException("No ha ingresado su barrio");
        }

        if (null == street || street.isBlank()) {
            throw new ServicesException("No ha ingresado su calle");
        }

        if (null == height || 0 >= height) {
            throw new ServicesException("No ha ingresado una altura válida");
        }
    }

    private void validateSupplierFields(String biography, String principalService) throws ServicesException {
        if (null == biography || biography.isBlank()) {
            throw new ServicesException("Por favor, ingrese una biografía");
        }

        if (null == principalService || principalService.isBlank()) {
            throw new ServicesException("No ha ingresado su profesión");
        }
    }

    private void validateId(Integer id) throws ServicesException {
        if (null == id || id <= 0) {
            throw new ServicesException("No se ha ingresado un id válido");
        }
    }

    private void validateName(String name) throws ServicesException {
        if (null == name || name.isBlank()) {
            throw new ServicesException("No se ha ingresado un nombre válido");
        }
    }

    //Auxiliar method
    private Users getFromOptional(Optional<Users> userOpt) throws ServicesException {
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new ServicesException("No se ha encontrado un usuario");
    }
}
