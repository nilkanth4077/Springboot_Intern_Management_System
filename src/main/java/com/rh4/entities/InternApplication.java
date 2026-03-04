package com.rh4.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "internapplication")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_number", unique = true)
    @Pattern(regexp = "\\d{10}", message = "Contact number must be exactly 10 digits")
    private String contactNo;

    @Column(name = "email_id", unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "college_name")
    private String collegeName;

    @Column(name = "branch_name")
    private String branch;

    // ================= FILES (PostgreSQL BYTEA auto-mapped) =================

    @Lob
    @Column(name = "passport_size_image")
    private byte[] passportSizeImage;

    @Lob
    @Column(name = "icard_image")
    private byte[] collegeIcardImage;

    @Lob
    @Column(name = "noc_pdf")
    private byte[] nocPdf;

    @Lob
    @Column(name = "resume_pdf")
    private byte[] resumePdf;

    // ================= ACADEMIC DETAILS =================

    @Column(name = "semester")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester must be at most 8")
    private int semester;

    @Column(name = "password")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must be at least 8 characters long, contain uppercase, lowercase, number and special character."
    )
    private String password;

    @Column(name = "degree")
    private String degree;

    @Column(name = "status")
    @Builder.Default
    private String status = "pending";

    @Column(name = "final_status")
    @Builder.Default
    private String finalStatus = "pending";

    @Column(name = "domain")
    private String domain;

    // ================= DATES =================

    @Column(name = "joining_date")
    private Date joiningDate;

    @Column(name = "completion_date")
    @Future(message = "Completion date must be in the future")
    private Date completionDate;

    @AssertTrue(message = "Completion date must be after joining date")
    public boolean isCompletionDateAfterJoiningDate() {
        if (joiningDate == null || completionDate == null) {
            return true;
        }
        return completionDate.after(joiningDate);
    }

    // ================= FLAGS =================

    @Column(name = "group_created")
    @Builder.Default
    private Boolean groupCreated = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    // ================= AUDIT =================

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ================= LIFECYCLE METHODS =================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public byte[] getPassportSizeImage() {
        return passportSizeImage;
    }

    public void setPassportSizeImage(byte[] passportSizeImage) {
        this.passportSizeImage = passportSizeImage;
    }

    public byte[] getCollegeIcardImage() {
        return collegeIcardImage;
    }

    public void setCollegeIcardImage(byte[] collegeIcardImage) {
        this.collegeIcardImage = collegeIcardImage;
    }

    public byte[] getNocPdf() {
        return nocPdf;
    }

    public void setNocPdf(byte[] nocPdf) {
        this.nocPdf = nocPdf;
    }

    public byte[] getResumePdf() {
        return resumePdf;
    }

    public void setResumePdf(byte[] resumePdf) {
        this.resumePdf = resumePdf;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Boolean getGroupCreated() {
        return groupCreated;
    }

    public void setGroupCreated(Boolean groupCreated) {
        this.groupCreated = groupCreated;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}