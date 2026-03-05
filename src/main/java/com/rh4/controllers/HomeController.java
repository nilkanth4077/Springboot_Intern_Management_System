package com.rh4.controllers;

import com.rh4.dto.LoginRequest;
import com.rh4.enums.Role;
import com.rh4.repositories.*;
import com.rh4.services.EmailSenderService;
import com.rh4.services.FieldService;
import com.rh4.services.InternService;
import com.rh4.entities.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final InternApplicationRepo internApplicationRepo;
    private final EmailSenderService emailService;

    @Autowired
    private InternService internService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public HomeController(InternApplicationRepo internApplicationRepo,
                          EmailSenderService emailService) {
        this.internApplicationRepo = internApplicationRepo;
        this.emailService = emailService;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ==============================
    // 1️⃣ Get Required Fields Data
    // ==============================
    @GetMapping("/internship/fields")
    public ResponseEntity<?> getInternshipFields() {

        return ResponseEntity.ok().body(
                new Object() {
                    public final List<College> colleges = fieldService.getColleges();
                    public final List<Domain> domains = fieldService.getDomains();
                    public final List<Branch> branches = fieldService.getBranches();
                    public final List<Degree> degrees = fieldService.getDegrees();
                }
        );
    }

    // ==============================
    // 2️⃣ Apply for Internship
    // ==============================
    @PostMapping("/internship/apply")
    public ResponseEntity<?> applyInternship(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String contactNo,
            @RequestParam String email,
            @RequestParam String collegeName,
            @RequestParam String branch,
            @RequestParam MultipartFile passportSizeImage,
            @RequestParam MultipartFile icardImage,
            @RequestParam MultipartFile nocPdf,
            @RequestParam MultipartFile resumePdf,
            @RequestParam int semester,
            @RequestParam String password,
            @RequestParam String degree,
            @RequestParam String domain,
            @RequestParam Date joiningDate,
            @RequestParam Date completionDate) {

        try {

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

            // Create User
            MyUser user = new MyUser();
            user.setEmail(email);
            user.setPassword(passwordEncoder().encode(password));
            user.setEnabled(true);
//            user.setUserId(Long.toString(internApplication.getId()));
            user.setRole("INTERN");
            userRepo.save(user);

            // Send Email
            emailService.sendSimpleEmail(
                    email,
                    "Your BISAG Internship Application has been submitted successfully.",
                    "BISAG ADMIN"
            );

            return ResponseEntity.ok("Application submitted successfully.");

        } catch (Exception e) {
            logger.error("Error submitting application", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ==============================
    // 3️⃣ Get Under Process Intern
    // ==============================
    @GetMapping("/internship/under-process/{username}")
    public ResponseEntity<?> getUnderProcessIntern(@PathVariable String username) {

        InternApplication intern = internService.getInternApplicationByUsername(username);

        if (intern == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(intern);
    }

    // ==============================
    // 4️⃣ Cancel Internship
    // ==============================
    @DeleteMapping("/internship/cancel/{id}")
    public ResponseEntity<?> cancelInternship(@PathVariable long id) {

        Optional<InternApplication> intern = internService.getInternApplication(id);

        if (intern.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        internService.cancelInternApplication(intern);

        return ResponseEntity.ok("Internship cancelled successfully.");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpSession session) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            session.setAttribute("username", request.getEmail());
            session.setAttribute("role",
                    authentication.getAuthorities().iterator().next().getAuthority());

            return ResponseEntity.ok("Login successful");

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
}