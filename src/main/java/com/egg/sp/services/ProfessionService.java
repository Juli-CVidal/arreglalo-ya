package com.egg.sp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.sp.entities.Profession;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.ProfessionRepository;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProfessionService {

    @Autowired
    private ProfessionRepository professionRepository;

    // ======== CREATE ========
    @Transactional
    public void create(Profession profession) throws ServicesException {
        validateServiceType(profession.getName());
        professionRepository.save(profession);
    }

    // ======== READ ========
    @Transactional(readOnly = true)
    public List<Profession> findAll() {
        return professionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Profession findById(Integer professionId) throws ServicesException {
        return getFromOptional(professionRepository.findById(professionId));
    }

    @Transactional(readOnly = true)
    public String getNameById(Integer professionId) throws ServicesException {
        Profession profession = getFromOptional(professionRepository.findById(professionId));
        return profession.getName();
    }

    public List<String> returnJobTypes() {

        List<Profession> listProffesions = findAll();
        List<String> nameProfession = new ArrayList<>();

        for (Profession profession : listProffesions) {
            nameProfession.add(profession.getName());
        }
        return nameProfession;
    }

    // ======== VALIDATE ========
    public void validateServiceType(String name) throws ServicesException {
        if (name == null || name.isEmpty()) {
            throw new ServicesException("Debe ingresar el tipo de servicio que ofrece");
        }
    }

    // ======== AUXILIAR METHOD ========
    private Profession getFromOptional(Optional<Profession> professionOpt) throws ServicesException {
        if (professionOpt.isEmpty()) {
            throw new ServicesException("No se encontró el trabajo");
        }
        return professionOpt.get();
    }
}
