package com.rh4.controllers;

import com.rh4.repositories.*;
import com.rh4.services.AdminService;
import com.rh4.services.EmailSenderService;
import com.rh4.services.FieldService;
import com.rh4.services.InternService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.rh4.entities.*;

@Controller
@Validated
public class HomeController {

	private InternApplicationRepo repo;
	private EmailSenderService emailService;
	
	 
	@org.springframework.beans.factory.annotation.Value("${icard.filepath}")
	private String icardfolderpath;
	@org.springframework.beans.factory.annotation.Value("${noc.filepath}")
	private String nocfolderpath;
	@org.springframework.beans.factory.annotation.Value("${resume.filepath}")
	private String resumefolderpath;
	@org.springframework.beans.factory.annotation.Value("${psimage.filepath}")
	private String psimagefolderpath;
	@Autowired
	private AdminService adminService;
	@Autowired
	private InternService internService;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private FieldService fieldService;
	public HomeController(InternApplicationRepo repo, EmailSenderService emailService) {
		this.repo = repo;	
		this.emailService = emailService;
	}
	PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	//////////////////////////////////////////////////////////
	
	@GetMapping("/message")
    public String msg() {
        return "msg";
    }
	//////////////////////////////////////////////////////////

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public void login(Intern intern) {

	}

	@GetMapping("/bisag_internship")
	public ModelAndView bisag_internship() {
		ModelAndView mv = new ModelAndView("internapply");
		List<College> colleges = fieldService.getColleges();
		List<Domain> domains = fieldService.getDomains();
		List<Branch> branches = fieldService.getBranches();
		List<Degree> degrees = fieldService.getDegrees();
		mv.addObject("colleges", colleges);
		mv.addObject("domains", domains);
		mv.addObject("branches", branches);
		mv.addObject("degrees", degrees);
		
		return mv;
	}

	@PostMapping("/bisag_internship")
	public String bisag_iternship(MultipartHttpServletRequest req, InternApplication intern, HttpSession session)
	        throws IllegalStateException, Exception {

	    try {
	        // Upload noc and resume
	       
	        InternApplication savedIntern = repo.save(intern);
	        intern.setIcardImage(uploadfile(req.getFile("icardImageone"), "icard" , intern.getId()));
	        intern.setNocPdf(uploadfile(req.getFile("nocPdfone"), "noc", intern.getId()));
	        intern.setResumePdf(uploadfile(req.getFile("resumePdfone"), "resume", intern.getId()));
	        intern.setPassportSizeImage(uploadfile(req.getFile("passportSizeImageone"), "psimage", intern.getId()));

	        MyUser user = new MyUser();
			user.setUsername(intern.getEmail());
			String password = passwordEncoder().encode(intern.getPassword());
			user.setPassword(password);
			user.setEnabled(true);
			user.setUserId(Long.toString(intern.getId()));
			user.setRole("UNDERPROCESSINTERN");
			userRepo.save(user);
	        // Save intern application
	        
	        // Send success email
	        emailService.sendSimpleEmail(
	                intern.getEmail(),
	                "Notification: Successful Application for BISAG Internship\r\n" +
	                        "\r\n" +
	                        "Dear " + intern.getFirstName() + ",\r\n" +
	                        "\r\n" +
	                        "Congratulations! We are pleased to inform you that your application for the BISAG internship has been successful. Your enthusiasm, qualifications, and potential have stood out, and we believe that you will make valuable contributions to our team.\r\n" +
	                        "\r\n" +
	                        "As an intern, you will have the opportunity to learn, grow, and gain hands-on experience in a dynamic and innovative environment. We trust that your time with us will be rewarding, and we look forward to seeing your skills and talents in action.\r\n" +
	                        "\r\n" +
	                        "Please find attached detailed information about the internship program, including your start date, orientation details, and any additional requirements. If you have any questions or need further assistance, feel free to contact [Contact Person/Department].\r\n" +
	                        "\r\n" +
	                        "Once again, congratulations on being selected for the BISAG internship program. We are excited to welcome you to our team and wish you a fulfilling and successful internship experience.\r\n" +
	                        "\r\n" +
	                        "Best regards,\r\n" +
	                        "\r\n" +
	                        "Your Colleague,\r\n" +
	                        "Internship Coordinator\r\n" +
	                        "BISAG INTERNSHIP PROGRAM\r\n" +
	                        "1231231231",
	                "BISAG ADMINISTRATIVE OFFICE"
	        );

	        // Check if save operation was successful
	        if (savedIntern != null) {
	            // Set success message in session
	            session.setAttribute("msg", "Applied successfully");
	        } else {
	            // Set error message in session
	            session.setAttribute("msg", "Something wrong");
	        }

	        return "redirect:/bisag_internship";
	    } catch (Exception e) {
	        // Log the exception
	        e.printStackTrace();

	        // Set error message in session
	        session.setAttribute("msg", "Error: Failed to apply. Please try again.");

	        return "redirect:/bisag_internship";
	    }
	}

	public String uploadfile(MultipartFile file, String object, long id) throws Exception, IllegalStateException, IOException {
		try {
			if(object == "icard")
			{
				File myDir = new File(icardfolderpath);
				if (!myDir.exists())
					myDir.mkdirs();
				
				if (!file.isEmpty()) {
					file.transferTo(Paths.get(myDir.getAbsolutePath(), id + "_icard.pdf"));
					return id + "_icard.pdf";
				} else {
					return null;
				}
			}
			else if (object == "psimage")
		      {
		        File myDir = new File(psimagefolderpath);
		        if (!myDir.exists())
		          myDir.mkdirs();
		       
		        if (!file.isEmpty()) {
		          file.transferTo(Paths.get(myDir.getAbsolutePath(), id + "_psimage.pdf"));
		          return id + "_psimage.pdf";
		        } else {
		        	return null;
		        }
		      }
			else if (object == "noc")
			{
				File myDir = new File(nocfolderpath);
				if (!myDir.exists())
					myDir.mkdirs();
				
				if (!file.isEmpty()) {
					file.transferTo(Paths.get(myDir.getAbsolutePath(), id + "_noc.pdf"));
					return id + "_noc.pdf";
				} else {
					return null;
				}
			}
			else if (object.equals("resume")) {
				File myDir = new File(resumefolderpath);
				if (!myDir.exists())
					myDir.mkdirs();
				
				if (!file.isEmpty()) {
					file.transferTo(Paths.get(myDir.getAbsolutePath(), id + "_resume.pdf"));
					return id + "_resume.pdf";
				} else {
					return null;
				}
			}

			else 
			{
				System.out.println("nothing is true");
				return "redirect:/";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/";
		}
		
	}
	//Logout
		@GetMapping("/logout")
		public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	        // Perform any additional logout logic if needed
			 if (authentication != null) {
		            new SecurityContextLogoutHandler().logout(request, response, authentication);
		        }
	        return "redirect:/login?logout"; // Redirect to the login page with a logout parameter
	    }

	//Cancel internship 
		@GetMapping("/under_process_intern")
		public ModelAndView underProcessApplication(HttpSession session)
		{
			ModelAndView mv = new ModelAndView("under_process_application");
			String username = (String) session.getAttribute("username");
			InternApplication intern = internService.getInternApplicationByUsername(username);
			session.setAttribute("id", intern.getId());
		    session.setAttribute("username", username);
		    mv.addObject("internApplication", intern);
		    mv.addObject("username", username);
			return mv;
		}
		@GetMapping("/cancel/{id}")
		public String cancelInternship(@PathVariable("id") long id)
		{
			Optional<InternApplication> intern = internService.getInternApplication(id);
			internService.cancelInternApplication(intern);
			return "redirect:/under_process_intern";
		}
}