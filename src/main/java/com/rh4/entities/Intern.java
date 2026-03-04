package com.rh4.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;

@Entity
@Table(name = "intern")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intern {

    @Id
    @Column(name = "intern_id")
    private String internId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_no", unique = true)
    private String contactNo;

    @Column(name = "email_id", unique = true)
    private String email;

    @Column(name = "college_name")
    private String collegeName;

    @Column(name = "branch_name")
    private String branch;

    // ================= FILES (PostgreSQL BYTEA) =================

    @Lob
    @Column(name = "icard_image")
    private byte[] collegeIcardImage;

    @Lob
    @Column(name = "noc_pdf")
    private byte[] nocPdf;

    @Lob
    @Column(name = "resume_pdf")
    private byte[] resumePdf;

    @Lob
    @Column(name = "passport_size_image")
    private byte[] passportSizeImage;

    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Lob
    @Column(name = "icard_form")
    private byte[] icardForm;

    @Lob
    @Column(name = "registration_form")
    private byte[] registrationForm;

    @Lob
    @Column(name = "security_form")
    private byte[] securityForm;

    // ================= BASIC INFO =================

    @Column(name = "semester")
    private int semester;

    @Column(name = "domain")
    private String domain;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    public boolean active = true;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Past(message = "Birth date must be in the past")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "college_guide_hod_name")
    private String collegeGuideHodName;

    @Column(name = "degree")
    private String degree;

    @Column(name = "aggregate_percentage")
    private Double aggregatePercentage;

    @Column(name = "project_definition_name")
    private String projectDefinitionName;

    @Column(name = "joining_date")
    private Date joiningDate;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "used_resource", columnDefinition = "TEXT")
    private String usedResource;

    @Column(name = "cancellation_status")
    private String cancellationStatus;

    // ================= RELATIONSHIPS =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    // ================= AUDIT FIELDS =================

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Intern(String firstName, String lastName, String contactNo, String email, String collegeName, Date joiningDate, Date completionDate, String branch, String degree, String password, byte[] collegeIcardImage, byte[] nocPdf, byte[] resumePdf, byte[] passportSizeImage, int semester, String domain, GroupEntity group) {
    }

    // ================= LIFECYCLE =================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getInternId() {
        return internId;
    }

    public void setInternId(String internId) {
        this.internId = internId;
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

    public byte[] getPassportSizeImage() {
        return passportSizeImage;
    }

    public void setPassportSizeImage(byte[] passportSizeImage) {
        this.passportSizeImage = passportSizeImage;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getIcardForm() {
        return icardForm;
    }

    public void setIcardForm(byte[] icardForm) {
        this.icardForm = icardForm;
    }

    public byte[] getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(byte[] registrationForm) {
        this.registrationForm = registrationForm;
    }

    public byte[] getSecurityForm() {
        return securityForm;
    }

    public void setSecurityForm(byte[] securityForm) {
        this.securityForm = securityForm;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCollegeGuideHodName() {
        return collegeGuideHodName;
    }

    public void setCollegeGuideHodName(String collegeGuideHodName) {
        this.collegeGuideHodName = collegeGuideHodName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Double getAggregatePercentage() {
        return aggregatePercentage;
    }

    public void setAggregatePercentage(Double aggregatePercentage) {
        this.aggregatePercentage = aggregatePercentage;
    }

    public String getProjectDefinitionName() {
        return projectDefinitionName;
    }

    public void setProjectDefinitionName(String projectDefinitionName) {
        this.projectDefinitionName = projectDefinitionName;
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

    public String getUsedResource() {
        return usedResource;
    }

    public void setUsedResource(String usedResource) {
        this.usedResource = usedResource;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
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