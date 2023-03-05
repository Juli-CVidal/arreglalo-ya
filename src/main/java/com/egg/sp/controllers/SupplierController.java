package com.egg.sp.controllers;

import com.egg.sp.entities.Review;
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
import com.egg.sp.entities.Users;
import com.egg.sp.entities.Work;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ReviewService;
import com.egg.sp.services.SupplierService;
import com.egg.sp.services.WorkService;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	
	@Autowired
    private SupplierService supplierService;
        
         @Autowired
    private WorkService workService;

    @Autowired
    private ReviewService reviewService;
	
	@GetMapping("/{id}")
   public String getProfile(@PathVariable("id") Integer id, HttpSession session, ModelMap model) throws ServicesException {

        Users user = (Users) session.getAttribute("usserSesion");
        Integer userId = user.getId();

        try {
            Supplier supplier = supplierService.findById(id);
            
            model.put("supplier", supplier);
            
            
            //Use this method to validate if the user can leave a review
            //If the method returns a number less a than one, the review form is hidden
            /*
            <div th:if="${numberCustomers > 1}">
            (form)
            </div>
            */
            model.put("numberCustomers", workService.countDistinctCustomers(id));

            //this is the average score
            model.put("averageRating", reviewService.averageRating(id));

            
            List<Work> workRequest = workService.getFromSupplierJobOffer(id);
            List<Work> activeWork = workService.getFromSupplierAcceptWork(id);
            List<Work> workCompleted = workService.getFromSupplierCompletedWork(id);

            model.put("workRequest", workRequest);
            model.put("activeWork", activeWork);
            model.put("workCompleted", workCompleted);

        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index";
        }
        return "profile";
    }
   
   @PostMapping("/{id}/create-work")
    public String createWork(HttpSession session, ModelMap model, Work work, Integer supplierId) throws ServicesException {

        Users user = (Users) session.getAttribute("usserSesion");
        Supplier supplier = supplierService.findById(supplierId);

        try {
            workService.CreateAcceptRefuseCompletedJob(work, user, supplier, "Enviado");

            model.put("supplier", supplier);
            model.put("success", "¡Solicitud de trabajo realizada correctamente!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("work", work);
            return "profile";
        }

        return "redirect:/supplier/{id}";
    }

    @PostMapping("/{id}/accept-work")
    public String acceptJob(HttpSession session, ModelMap model, Work work, Integer supplierId) throws ServicesException {

        Users user = (Users) session.getAttribute("usserSesion");
        Supplier supplier = supplierService.findById(supplierId);

        try {
            workService.CreateAcceptRefuseCompletedJob(work, user, supplier, "Aceptado");

            model.put("supplier", supplier);
            model.put("success", "¡Este trabajo se ha añadido a su lista de trabajos!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("work", work);
            return "profile";
        }

        return "redirect:/supplier/{id}";
    }

    @PostMapping("/{id}/refuse-work")
    public String refuseJob(HttpSession session, ModelMap model, Work work, Integer supplierId) throws ServicesException {

        Users user = (Users) session.getAttribute("usserSesion");
        Supplier supplier = supplierService.findById(supplierId);

        try {
            workService.CreateAcceptRefuseCompletedJob(work, user, supplier, "Rechazado");

            model.put("supplier", supplier);
            model.put("success", "¡El trabajo fue rechazado!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("work", work);
            return "profile";
        }

        return "redirect:/supplier/{id}";
    }

    @PostMapping("/{id}/complete-work")
    public String completeJob(HttpSession session, ModelMap model, Work work, Integer supplierId) throws ServicesException {

        Users user = (Users) session.getAttribute("usserSesion");
        Supplier supplier = supplierService.findById(supplierId);

        try {
            workService.CreateAcceptRefuseCompletedJob(work, user, supplier, "Finalizado");

            model.put("supplier", supplier);
            model.put("success", "¡El trabajo fue dado como finalizado!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("work", work);
            return "profile";
        }

        return "redirect:/supplier/{id}";
    }

    //revisar validacion
    @PostMapping("/{id}/review")
    public String createReview(@Valid Review review, ModelMap model, HttpSession session, Integer supplierId) {

        Users user = (Users) session.getAttribute("usserSesion");
        Integer userId = user.getId();
        try {
            reviewService.validateContratacion(userId, supplierId);
            reviewService.create(review);

            model.put("success", "¡Reseña añadida correctamente!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("review", review);
            return "profile";
        }
        return "redirect:/supplier/{id}";
    }

    @PostMapping("/delete_review/{id}")
    public String deleteReview(Integer id, ModelMap model) {

        try {
            reviewService.delete(id);
            model.put("success", "¡Reseña eliminada correctamente!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/review";
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
