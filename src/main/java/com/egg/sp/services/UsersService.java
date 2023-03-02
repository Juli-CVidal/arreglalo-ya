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

    // ======== CREATE ========

    @Transactional
    public void create(Users user) throws ServicesException {
        // If a user is created with Rol.SUPPLIER, possibly don't want to put this info
        if (user.getRol() == Rol.CUSTOMER){
            validateCustomerFields(user.getNeighborhood(), user.getStreet(), user.getHeight());
        }
        usersRepository.save(user);
    }


    private void validateCustomerFields(String neighborhood, String street, Integer height) throws ServicesException {
        if (null == neighborhood || neighborhood.isBlank()){
            throw new ServicesException("No ha ingresado su barrio");
        }

        if (null == street || street.isBlank()){
            throw new ServicesException("No ha ingresado su calle");
        }

        if (null == height || 0 >= height){
            throw new ServicesException("No ha ingresado una altura válida");
        }
    }

    // ======= READ ========

    @Transactional(readOnly = true)
    public List<Users> getAll(){
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Users findById(@NotNull Integer id) throws  ServicesException{
       return getFromOptional(usersRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public Users findByName(@NotNull String name) throws  ServicesException{
        return getFromOptional(usersRepository.findByName(name));
    }

    private Users getFromOptional(Optional<Users> userOpt) throws ServicesException{
        if (userOpt.isPresent()){
            return userOpt.get();
        }
        throw new ServicesException("No se ha encontrado un usuario");
    }


    // ======== UPDATE ========

    @Transactional
    public void update(Users user) throws ServicesException{
        create(user);
    }

    // ======== DELETE ========

    @Transactional
    //soft delete (change state to false)
    public void delete(@NotNull Integer id) throws ServicesException{
        Users user = findById(id);
        user.setState(false);
        usersRepository.save(user);
    }
}
