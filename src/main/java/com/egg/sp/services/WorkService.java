package com.egg.sp.services;

import com.egg.sp.entities.Supplier;
import com.egg.sp.entities.Users;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.sp.entities.Work;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.WorkRepository;
import com.egg.sp.enums.Acceptance;
import java.util.List;

@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;

    @Transactional
    public void createWork(Work work) throws ServicesException {

        work.setCreationDate(new Date(System.currentTimeMillis()));

        workRepository.save(work);
    }

    public List<Work> getWorksSupplier(Integer id) {
        return workRepository.getFromSupplier(id);
    }

    public List<Work> getWorksUser(Integer id) {
        return workRepository.getFromCustomer(id);
    }

    public List<Work> getFromSupplierJobOffer(Integer id) {
        return workRepository.getFromSupplierJobOffer(id);
    }

    public List<Work> getFromSupplierAcceptWork(Integer id) {
        return workRepository.getFromSupplierAcceptWork(id);
    }

    public List<Work> getFromSupplierCompletedWork(Integer id) {
        return workRepository.getFromSupplierCompletedWork(id);
    }

    public List<Work> getWorksByPriceAsc() {
        return workRepository.orderByPriceAsc();
    }

    public List<Work> getWorksByPriceDesc() {
        return workRepository.orderByPriceDesc();
    }

    public Optional<Work> getByDate(Date date) {
        return workRepository.getByDate(date);
    }

    public int countDistinctCustomers(int supplierId) {
        return workRepository.countDistinctCustomers(supplierId);
    }

    @Transactional
    public Work CreateAcceptRefuseCompletedJob(Work work, Users user, Supplier supplier, String acceptance) throws ServicesException {

        work.setCreationDate(new Date(System.currentTimeMillis()));

        work.setUser(user);
        work.setSupplier(supplier);

        switch (acceptance) {
            case "Enviado":
                work.setAcceptance(Acceptance.ENVIADO);
                break;
            case "Aceptado":
                work.setAcceptance(Acceptance.ACEPTADO);
                break;
            case "Rechazado":
                work.setAcceptance(Acceptance.RECHAZADO);
                break;
            case "Finalizado":
                work.setAcceptance(Acceptance.FINALIZADO);
                break;
            default:
                break;
        }

        workRepository.save(work);
        return work;
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
