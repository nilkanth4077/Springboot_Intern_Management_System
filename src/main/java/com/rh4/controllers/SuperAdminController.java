package com.rh4.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.rh4.entities.Admin;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.SuperAdmin;
import com.rh4.repositories.InternApplicationRepo;
import com.rh4.services.AdminService;
import com.rh4.services.EmailSenderService;
import com.rh4.services.SuperAdminService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/super_admin")
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

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {

        adminService.registerAdmin(admin);

        return ResponseEntity.ok().body("Admin registered successfully");
    }

    @GetMapping("/admin_list")
    public ResponseEntity<List<Admin>> adminList() {
        List<Admin> admins = adminService.getAdmin();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/admin_list/{id}")
    public ResponseEntity<?> adminList(@PathVariable("id") long id) {

        Optional<Admin> admin = adminService.getAdmin(id);

        if (admin.isPresent()) {
            return ResponseEntity.ok(admin.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin not found with id: " + id);
        }
    }

    @PutMapping("/update_admin/{id}")
    public ResponseEntity<?> updateAdmin(@RequestBody Admin admin,
                                         @PathVariable("id") long id) {

        Optional<Admin> existingAdmin = adminService.getAdmin(id);

        if (existingAdmin.isPresent()) {

            Admin updatedAdmin = existingAdmin.get();
            String currentPassword = updatedAdmin.getPassword();

            updatedAdmin.setName(admin.getName());
            updatedAdmin.setLocation(admin.getLocation());
            updatedAdmin.setContactNo(admin.getContactNo());
            updatedAdmin.setEmailId(admin.getEmailId());

            if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                String encodedPassword = encodePassword(admin.getPassword());
                if (!currentPassword.equals(encodedPassword)) {
                    updatedAdmin.setPassword(encodedPassword);
                }
            }

            adminService.updateAdmin(updatedAdmin, existingAdmin);

            return ResponseEntity.ok("Admin updated successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Admin not found with id: " + id);
    }

    @DeleteMapping("/admin_list/delete/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("id") long id) {

        adminService.deleteAdmin(id);

        return ResponseEntity.ok("Admin deleted successfully");
    }

}
