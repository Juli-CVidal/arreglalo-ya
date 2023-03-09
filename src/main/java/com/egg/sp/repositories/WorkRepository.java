package com.egg.sp.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.sp.entities.Work;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {

    @Query("SELECT w FROM Work w WHERE w.id = :id")
    Optional<Work> findById(@Param("id") Integer id);

    @Query("SELECT w FROM Work w WHERE w.creationDate = :date")
    Optional<Work> getByDate(@Param("date") Date date);

    @Query("SELECT w FROM Work w WHERE w.supplier.id = :id")
    public List<Work> getFromSupplier(@Param("id") Integer id);

    @Query("SELECT w FROM Work w WHERE w.supplier.id = :id AND w.acceptance = com.egg.sp.enums.Acceptance.ENVIADO")
    public List<Work> getFromSupplierJobOffer(@Param("id") Integer id);

    @Query("SELECT w FROM Work w WHERE w.supplier.id = :id AND w.acceptance = com.egg.sp.enums.Acceptance.ACEPTADO")
    public List<Work> getFromSupplierAcceptWork(@Param("id") Integer id);

    @Query("SELECT w FROM Work w WHERE w.supplier.id = :id AND w.acceptance = com.egg.sp.enums.Acceptance.FINALIZADO")
    public List<Work> getFromSupplierCompletedWork(@Param("id") Integer id);

    @Query("SELECT w FROM Work w WHERE w.user.id = :id")
    public List<Work> getFromCustomer(@Param("id") Integer id);

    @Query("SELECT count(w) FROM Work w WHERE w.user.id = :idUser AND w.supplier.id = :idSupplier")
    public int countContractedTimes(@Param("idUser") Integer idUser, @Param("idSupplier") Integer idSupplier);

    @Query("SELECT COUNT(DISTINCT w.user.id) FROM Work w WHERE w.acceptance = com.egg.sp.enums.Acceptance.FINALIZADO AND w.supplier.id = :idSupplier")
    public int countDistinctCustomers(@Param("idSupplier") Integer idSupplier);

    @Query("SELECT w FROM Work w ORDER BY w.price ASC")
    public List<Work> orderByPriceAsc();

    @Query("SELECT w FROM Work w ORDER BY w.price DESC")
    public List<Work> orderByPriceDesc();

    @Query("SELECT w FROM Work w WHERE w.user.id = :customerId AND w.supplier.id = :supplierId")
    public List<Work> findWorksHistory(@Param("customerId") Integer customerId, @Param("supplierId") Integer supplierId);
}
