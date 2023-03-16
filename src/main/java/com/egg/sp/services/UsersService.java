package com.egg.sp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.egg.sp.entities.Users;
import com.egg.sp.enums.Provider;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.exceptions.UserNotFoundException;
import com.egg.sp.repositories.UsersRepository;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProfessionService professionService;

    // ======== CREATE ========
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = usersRepository.findByEmail(email);
        if (null == user) {
            throw new UsernameNotFoundException("No se ha encontrado el usuario");
        }

        List<GrantedAuthority> permissions = new ArrayList();
        GrantedAuthority grantedAuth = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
        permissions.add(grantedAuth);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("userSession", user);
        return new User(user.getEmail(), user.getPassword(), permissions);
    }

    @Transactional
    public void create(Users user) throws ServicesException {
        validateFields(user);
        user.setGeneralScore(0D);
        user.setProvider(Provider.LOCAL);
        user.setState(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
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
    public Optional<Users> findByEmail(@NotNull String email) throws ServicesException {
        return (usersRepository.findUserByEmail(email));
    }

    @Transactional(readOnly = true)
    public Optional<Users> findByPhoneNumber(@NotNull String phoneNumber) throws ServicesException {
        return (usersRepository.findByNumberPhone(phoneNumber));
    }

    // ======== UPDATE ========
    @Transactional
    public void update(Users user, String confirm) throws ServicesException {
        if (!new BCryptPasswordEncoder().matches(confirm, user.getPassword())) {
            throw new ServicesException("Contraseña incorrecta, reintente");
        }
        validateFields(user);
        usersRepository.save(user);
    }

    @Transactional
    public void updateGeneralScore(Integer supplierId, Double generalScore) throws ServicesException {
        Users supplier = findSupplierById(supplierId);
        supplier.setGeneralScore(generalScore);
        usersRepository.save(supplier);
    }

    @Transactional
    public void becomeSupplier(Users user) throws ServicesException {
        user.setGeneralScore(0D);
        user.setRol(Rol.SUPPLIER);
        usersRepository.save(user);
    }

    // ======== DELETE ========
    @Transactional
    // soft delete (change state to false)
    public void delete(@NotNull Integer id) throws ServicesException {
        Users user = findById(id);
        user.setState(false);
        usersRepository.save(user);
    }

    // VALIDATIONS
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

    private void validateSupplierFields(String biography, String profession) throws ServicesException {
        if (null == biography || biography.isBlank()) {
            throw new ServicesException("Por favor, ingrese una biografía");
        }

        if (null == profession || !professionService.exists(profession)) {
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

    private Users getFromOptional(Optional<Users> userOpt) throws ServicesException {
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new ServicesException("No se ha encontrado un usuario");
    }

    public void createNewCustomerAfterOAuthLoginSuccess(String lastname, String email, String name, Provider provider) {
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setLastname(lastname);
        user.setProvider(Provider.GOOGLE);
        user.setRol(Rol.CUSTOMER);
        usersRepository.save(user);
    }

    public void updateUserAfterOAuthLoginSuccess(Users users, String name, Provider google) {
        users.setName(name);
        users.setProvider(Provider.GOOGLE);
        usersRepository.save(users);
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {

        Users user = usersRepository.findByEmail(email);

        if (user != null) {
            user.setResetPasswordToken(token);
            usersRepository.save(user);
        } else {
            throw new UserNotFoundException("No se pudo encontrar ningun usuario con este email: " + email);
        }

    }

    public Users get(String resetPasswordToken) {
        return usersRepository.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(Users user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        usersRepository.save(user);
    }

}
