package com.rh4.controllers;

import com.rh4.repositories.*;
import com.rh4.services.AdminService;
import com.rh4.services.EmailSenderService;
import com.rh4.services.FieldService;
import com.rh4.services.InternService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.rh4.entities.*;

@Controller
@Validated
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);
    private InternApplicationRepo internApplicationRepo;
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

    public HomeController(InternApplicationRepo internApplicationRepo, EmailSenderService emailService) {
        this.internApplicationRepo = internApplicationRepo;
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
    public String bisag_iternship(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("contactNo") String contactNo,
                                  @RequestParam("email") String email,
                                  @RequestParam("collegeName") String collegeName,
                                  @RequestParam("branch") String branch,
                                  @RequestParam("passportSizeImage") MultipartFile passportSizeImage,
                                  @RequestParam("icardImage") MultipartFile icardImage,
                                  @RequestParam("nocPdf") MultipartFile nocPdf,
                                  @RequestParam("resumePdf") MultipartFile resumePdf,
                                  @RequestParam("semester") int semester,
                                  @RequestParam("password") String password,
                                  @RequestParam("degree") String degree,
                                  @RequestParam("domain") String domain,
                                  @RequestParam("joiningDate") Date joiningDate,
                                  @RequestParam("completionDate") Date completionDate, HttpSession session) {

        try {
            String storageDir = "D:/User/IMS/Springboot_Intern_Management_System/src/main/resources/static/files/Intern Docs/" + email + "/";
            File directory = new File(storageDir);

            // Create directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save files to local storage
            String passportFileName = storageDir + "passportSizeImage.jpg";
            String icardFileName = storageDir + "collegeIcardImage.jpg";
            String nocFileName = storageDir + "nocPdf.pdf";
            String resumeFileName = storageDir + "resumePdf.pdf";

            // Save Passport Size Image
            Files.write(Paths.get(passportFileName), passportSizeImage.getBytes());

            // Save College Icard Image
            Files.write(Paths.get(icardFileName), icardImage.getBytes());

            // Save NOC PDF
            Files.write(Paths.get(nocFileName), nocPdf.getBytes());

            // Save Resume PDF
            Files.write(Paths.get(resumeFileName), resumePdf.getBytes());

            InternApplication internApplication = new InternApplication();
            internApplication.setFirstName(firstName);
            internApplication.setLastName(lastName);
            internApplication.setContactNo(contactNo);
            internApplication.setEmail(email);
            internApplication.setCollegeName(collegeName);
            internApplication.setBranch(branch);
            internApplication.setSemester(semester);
            internApplication.setPassword(password);
            internApplication.setDegree(degree);
            internApplication.setDomain(domain);
            internApplication.setJoiningDate(joiningDate);
            internApplication.setCompletionDate(completionDate);

            internApplication.setPassportSizeImage(passportSizeImage.getBytes());
            internApplication.setCollegeIcardImage(icardImage.getBytes());
            internApplication.setNocPdf(nocPdf.getBytes());
            internApplication.setResumePdf(resumePdf.getBytes());

            internApplicationRepo.save(internApplication);

            MyUser user = new MyUser();
            user.setUsername(email);
            String encryptedPassword = passwordEncoder().encode(password);
            user.setPassword(encryptedPassword);
            user.setEnabled(true);
            user.setUserId(Long.toString(internApplication.getId()));
            user.setRole("UNDERPROCESSINTERN");
            userRepo.save(user);

            // Send success email
            emailService.sendSimpleEmail(
                    internApplication.getEmail(),
                    "Notification: Successful Application for BISAG Internship\r\n" +
                            "\r\n" +
                            "Dear " + internApplication.getFirstName() + ",\r\n" +
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
            session.setAttribute("msg", "Application Submitted Successfully");
            return "redirect:/bisag_internship";
        } catch (Exception e) {
            logger.info("Issue while submitting application: " + e.getMessage());
            session.setAttribute("msg", "Error: " + e.getMessage() + ". Please try again.");
            return "redirect:/bisag_internship";
        }
    }

    @PostMapping("/remove-session-msg")
    @ResponseBody
    public void removeSessionMsg(HttpSession session) {
        session.removeAttribute("msg");  // Remove the 'msg' attribute from the session
    }

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
    public ModelAndView underProcessApplication(HttpSession session) {
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
    public String cancelInternship(@PathVariable("id") long id) {
        Optional<InternApplication> intern = internService.getInternApplication(id);
        internService.cancelInternApplication(intern);
        return "redirect:/under_process_intern";
    }
}