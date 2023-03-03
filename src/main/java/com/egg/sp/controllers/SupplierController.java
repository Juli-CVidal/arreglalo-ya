package com.egg.sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.sp.entities.Supplier;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.SupplierService;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	
	@Autowired
    private SupplierService supplierService;
	
	@GetMapping("/{id}")
    public String getProfile(@PathVariable("id") Integer id, ModelMap model){

        try{
        	Supplier supplier = supplierService.findById(id);
            model.put("supplier", supplier);
        } catch(ServicesException se){
            model.put("error", se.getMessage());
            return "index";
        }
        return "profile";
    }
	
	@GetMapping("/all")
    public String getAllSuppliers(ModelMap model){
        model.put("supplierList", supplierService.getAll());
        return "all-suppliers";
    }
	
	@GetMapping("/update/{id}")
    public String getForm(@PathVariable("id") Integer id, ModelMap model){
        try{
            Supplier supplier = supplierService.findById(id);
            model.put("supplier",supplier);
            return "profile-form";
        }catch (ServicesException se){
            model.put("error",se.getMessage());
            return "index.html";
        }
    }
	
	@PostMapping("/{id}")
    public String updateProfile(@ModelAttribute("supplier") Supplier supplier, BindingResult result, ModelMap model){
        if (result.hasErrors()){
            model.put("errors",result.getAllErrors());
            model.put("supplier",supplier);
            return "profile-form";
        }
        try {
            supplierService.update(supplier);
        }catch(ServicesException se){
            model.put("error",se.getMessage());
            model.put("supplier",supplier);
            return "profile-form";
        }
        model.put("success", "El perfil de proveedor ha sido actualizado!");
        return "redirect:/" + supplier.getId();
    }

}
