package com.rh4.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rh4.entities.Admin;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.SuperAdmin;
import com.rh4.repositories.InternApplicationRepo;
import com.rh4.services.AdminService;
import com.rh4.services.EmailSenderService;
import com.rh4.services.SuperAdminService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/bisag/super_admin")
public class SuperAdminController {
	private EmailSenderService emailService;
	public SuperAdminController(EmailSenderService emailService) {	
		this.emailService = emailService;
	}
	@Autowired
	private AdminService adminService;
	@Autowired
	private SuperAdminService superAdminService;
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
	@GetMapping("/super_admin_dashboard")
	public ModelAndView superAdmin_Dashboard(HttpSession session, Model model)
	{
			ModelAndView mv = new ModelAndView("super_admin/super_admin_dashboard");

		    // Retrieve the username from the session
		    String username = (String) session.getAttribute("username");

		    // Use the adminService to get the Admin object based on the username
		    SuperAdmin sadmin = superAdminService.getSuperAdminByUsername(username);
		    
		    long adminCount = superAdminService.countAdmin();
	 	    model.addAttribute("adminCount", adminCount);

		    // Set the "id" and "username" attributes in the session
		    session.setAttribute("id", sadmin.getSuperAdminId());
		    session.setAttribute("username", username);

		    // Add the username to the ModelAndView
		    mv.addObject("username", username);

		    return mv;
		//return "super_admin/super_admin_dashboard";
	}
	@GetMapping("/register_admin")
	public String registerAdmin()
	{
		return "super_admin/admin_registration";
	}
	
	@PostMapping("/register_admin")
	public String registerAdmin(@ModelAttribute("admin") Admin admin)
	{
		adminService.registerAdmin(admin);
		emailService.sendSimpleEmail(
				admin.getEmailId(),
				"Notification: Appointment as Administrator\r\n"
				+ "\r\n"
				+ "Dear " + admin.getName() + "\r\n"
				+ "\r\n"
				+ "I trust this email finds you well. We are pleased to inform you that you have been appointed as an administrator within our organization, effective immediately. Your dedication and contributions to the team have not gone unnoticed, and we believe that your new role will bring added value to our operations.\r\n"
				+ "\r\n"
				+ "As an administrator, you now hold a position of responsibility within the organization. We trust that you will approach your duties with diligence, professionalism, and a commitment to upholding the values of our organization.\r\n"
				+ "\r\n"
				+ "It is imperative to recognize the importance of your role and the impact it may have on the functioning of our team. We have confidence in your ability to handle the responsibilities that come with this position and to contribute positively to the continued success of our organization.\r\n"
				+ "\r\n"
				+ "We would like to emphasize the importance of maintaining the highest standards of integrity and ethics in your role. It is expected that you will use your administrative privileges responsibly and refrain from any misuse.\r\n"
				+ "\r\n"
				+ "Should you have any questions or require further clarification regarding your new responsibilities, please do not hesitate to reach out to [Contact Person/Department].\r\n"
				+ "\r\n"
				+ "Once again, congratulations on your appointment as an administrator. We look forward to your continued contributions and success in this elevated role.\r\n"
				+ "\r\n"
				+ "Best regards,\r\n"
				+ "\r\n"
				+ "Your Colleague,\r\n"
				+ "Administrator\r\n"
				+ "1231231231",
				"BISAG ADMINISTRATIVE OFFICE"
		);
		
		return "redirect:/bisag/super_admin/register_admin";
		
	}
	

	//--------------------------------------- Admin List -------------------------------------------//
	
	@GetMapping("/admin_list")
	public ModelAndView adminList()
	{
		ModelAndView mv = new ModelAndView("super_admin/admin_list");
		List<Admin> admins = adminService.getAdmin();
		mv.addObject("admins", admins);
		return mv;
	}
	@GetMapping("/admin_list/{id}")
	public ModelAndView adminList(@PathVariable("id") long id)
	{
		System.out.println("id"+id);
		ModelAndView mv = new ModelAndView();
		Optional<Admin> admin = adminService.getAdmin(id);
		mv.addObject("admin",admin);
		mv.setViewName("super_admin/admin_list_detail");
		return mv;
	}
	
	@PostMapping("/admin_list/ans")
	public String adminListRedirect()
	{
		System.out.println("iddd");
		return "redirect:/bisag/super_admin/admin_list";
	}
	
	//-------------------------------------- Admin Update ------------------------------------------//
	
	@GetMapping("/update_admin/{id}")
	public ModelAndView updateAdmin(@PathVariable("id") long id) {
		ModelAndView mv = new ModelAndView("super_admin/update_admin");
		Optional<Admin> admin = adminService.getAdmin(id);
		mv.addObject("admin", admin.orElse(new Admin()));
		return mv;
	}
	
	@PostMapping("/update_admin/{id}")
	public String updateAdmin(@ModelAttribute("admin") Admin admin, @PathVariable("id") long id) {
		Optional<Admin> existingAdmin = adminService.getAdmin(admin.getAdminId());

	    if (existingAdmin.isPresent()) {
	    	
	    	String currentPassword = existingAdmin.get().getPassword();
	    	Admin updatedAdmin = existingAdmin.get();
	        updatedAdmin.setName(admin.getName());
	        updatedAdmin.setLocation(admin.getLocation());
	        updatedAdmin.setContactNo(admin.getContactNo());
	        updatedAdmin.setEmailId(admin.getEmailId());
	    	if(!currentPassword.equals(encodePassword(admin.getPassword())) && admin.getPassword()!="")
	    	{
	    		updatedAdmin.setPassword(encodePassword(admin.getPassword()));
	    	}
	        // Save the updated admin entity
	        adminService.updateAdmin(updatedAdmin,existingAdmin);
	    }
		return "redirect:/bisag/super_admin/admin_list";
	}

	//-------------------------------------- Admin Delete ------------------------------------------//
	
	// Delete Admin
    @PostMapping("/admin_list/delete/{id}")
    public String deleteAdmin(@PathVariable("id") long id) {
        adminService.deleteAdmin(id);
        return "redirect:/bisag/super_admin/admin_list";
    }
	
	//--------------------------------- Admin Delete Completed -------------------------------------//
    
}
