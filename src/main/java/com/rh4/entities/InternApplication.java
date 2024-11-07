package com.rh4.entities;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "internapplication")
public class InternApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_number", unique = true)
    @Pattern(regexp = "\\d{10}", message = "Contact number must be exactly 10 digits")
    private String contactNo;

    @Column(name = "email_id", unique = true)
    private String email;

    @Column(name = "college_name")
    private String collegeName;

    @Column(name = "branch_name")
    private String branch;

    @Lob
    @Column(name = "passport_size_image", columnDefinition = "LONGBLOB")
    private byte[] passportSizeImage;

    @Lob
    @Column(name = "icard_image", columnDefinition = "LONGBLOB")
    private byte[] collegeIcardImage;

    @Lob
    @Column(name = "noc_pdf", columnDefinition = "LONGBLOB")
    private byte[] nocPdf;

    @Lob
    @Column(name = "resume_pdf", columnDefinition = "LONGBLOB")
    private byte[] resumePdf;

    @Column(name = "semester")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester must be at most 8")
    private int semester;

    @Column(name = "password")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;

    @Column(name = "degree")
    private String degree;

    @Column(name = "status")
    private String status = "pending";

    @Column(name = "final_status")
    private String finalStatus = "pending";

    @Column(name = "domain")
    private String domain;

    @Column(name = "joining_date")
    private Date joiningDate;

    @Column(name = "completion_date")
    @Future(message = "Completion date must be in the future")
    private Date completionDate;

    @AssertTrue(message = "Completion date must be after joining date")
    private boolean isCompletionDateAfterJoiningDate() {
        return completionDate == null || completionDate.after(joiningDate);
    }

    @Column(name = "group_created")
    private Boolean groupCreated = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}