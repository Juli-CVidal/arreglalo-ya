package com.egg.sp.services;

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

    @Autowired
    private ProfessionService professionService;

    //======== CREATE ========

    @Transactional
    public void create(Work work) throws ServicesException {
        if (!professionService.exists(work.getType())) {
            throw new ServicesException("La solicitud no contiene un tipo de servicio actual");
        }

        work.setCreationDate(new Date(System.currentTimeMillis()));
        work.setAcceptance(Acceptance.ENVIADO);
        workRepository.save(work);
    }

    //======== READ =======

    @Transactional(readOnly = true)
    public Work findById(Integer id) throws ServicesException {
        return getFromOptional(workRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public List<Work> getWorksSupplier(Integer id) {
        return workRepository.getFromSupplier(id);
    }

    @Transactional(readOnly = true)
    public List<Work> getWorksUser(Integer id) {
        return workRepository.getFromCustomer(id);
    }

    @Transactional(readOnly = true)
    public List<Work> getFromSupplierJobOffer(Integer id) {
        return workRepository.getFromSupplierJobOffer(id);
    }

    @Transactional(readOnly = true)
    public List<Work> getFromSupplierAcceptWork(Integer id) {
        return workRepository.getFromSupplierAcceptWork(id);
    }

    @Transactional(readOnly = true)
    public List<Work> getFromSupplierCompletedWork(Integer id) {
        return workRepository.getFromSupplierCompletedWork(id);
    }


    @Transactional(readOnly = true)
    public List<Work> findWorksHistory(Integer customerId, Integer supplierId) {
        return workRepository.findWorksHistory(customerId, supplierId);
    }

    @Transactional(readOnly = true)
    public List<Work> getWorksByPriceAsc() {
        return workRepository.orderByPriceAsc();
    }

    @Transactional(readOnly = true)
    public List<Work> getWorksByPriceDesc() {
        return workRepository.orderByPriceDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Work> getByDate(Date date) {
        return workRepository.getByDate(date);
    }

    @Transactional(readOnly = true)
    public int countDistinctCustomers(int supplierId) {
        return workRepository.countDistinctCustomers(supplierId);
    }


    // ======== UPDATE ========
    @Transactional
    public void modifyState(Integer id, Acceptance state) throws ServicesException {
        Work work = findById(id);
        work.setAcceptance(state);

        workRepository.save(work);

    }

    @Transactional
    public void acceptWork(Integer id, Double price) throws ServicesException {
        Work work = findById(id);
        work.setAcceptance(Acceptance.ACEPTADO);

        if (price <= 0) {
            throw new ServicesException("No se ha ingresado un precio válido");
        }
        work.setPrice(price);
        workRepository.save(work);
    }

    private Work getFromOptional(Optional<Work> workOpt) throws ServicesException {
        if (workOpt.isEmpty()) {
            throw new ServicesException("No se ha encontrado el supplier");
        }
        return workOpt.get();
    }


    //======== DELETE ========
    //It is not necessary, we change the status to REJECTED, and we can show it in the history.
}
