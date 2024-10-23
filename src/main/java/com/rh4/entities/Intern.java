package com.rh4.entities;


import java.sql.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "intern")
public class Intern {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "semester")
    private int semester;

    @Column(name = "domain")
    private String domain;

    @Column(name = "password")
    private String password;

    ///////////////////////////////////////////////

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "date_of_birth")
    @Past(message = "Birth date must be in the past")
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

    @ManyToOne
    private Guide guide;

    @Column(name = "joining_date")
    private Date joiningDate;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "used_resource", columnDefinition = "TEXT")
    private String usedResource;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = true)
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = true)
    private Date updatedAt;

    @Column(name = "cancellation_status")
    private String cancellationStatus;

    @Column(name = "icard_form")
    private String icardForm;

    @Column(name = "registration_form")
    private String registrationForm;

    @Column(name = "security_form")
    private String securityForm;

    @ManyToOne
    private GroupEntity group;

    public Intern() {
        super();
    }

    public Intern(String internId, String firstName, String lastName, String contactNo, String email,
                  String collegeName, String branch, byte[] collegeIcardImage, byte[] nocPdf, byte[] resumePdf, int semester,
                  String permanentAddress, Date dateOfBirth, String gender, String collegeGuideHodName, String degree, Double aggregatePercentage, String projectDefinitionName, String cancellationStatus,
                  Guide guide, String domain, Date joiningDate, Date completionDate, String password, String icardForm, String registrationForm, String securityForm,
                  String usedResource, Date createdAt, Date updatedAt, GroupEntity group, boolean isActive) {
        super();
        this.internId = internId;
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.email = email;
        this.collegeName = collegeName;
        this.branch = branch;
        this.collegeIcardImage = collegeIcardImage;
        this.nocPdf = nocPdf;
        this.resumePdf = resumePdf;
        this.semester = semester;
        this.permanentAddress = permanentAddress;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.password = password;
        this.collegeGuideHodName = collegeGuideHodName;
        this.degree = degree;
        this.aggregatePercentage = aggregatePercentage;
        this.projectDefinitionName = projectDefinitionName;
        this.guide = guide;
        this.securityForm = securityForm;
        this.icardForm = icardForm;
        this.registrationForm = registrationForm;
        this.domain = domain;
        this.joiningDate = joiningDate;
        this.completionDate = completionDate;
        this.usedResource = usedResource;
        this.cancellationStatus = cancellationStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.group = group;
    }

    public Intern(String firstName, String lastName, String contactNo, String email, String collegeName, Date joiningDate, Date completionDate,
                  String branch, String degree, String password, byte[] collegeIcardImage, byte[] nocPdf, byte[] resumePdf, byte[] passportSizeImage, int semester, String domain, GroupEntity group) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.email = email;
        this.collegeName = collegeName;
        this.joiningDate = joiningDate;
        this.completionDate = completionDate;
        this.branch = branch;
        this.degree = degree;
        this.collegeIcardImage = collegeIcardImage;
        this.nocPdf = nocPdf;
        this.resumePdf = resumePdf;
        this.passportSizeImage = passportSizeImage;
        this.semester = semester;
        this.password = password;
        this.domain = domain;
        this.group = group;
    }

    public byte[] getPassportSizeImage() {
        return passportSizeImage;
    }

    public void setPassportSizeImage(byte[] passportSizeImage) {
        this.passportSizeImage = passportSizeImage;
    }

    public String getIcardForm() {
        return icardForm;
    }

    public void setIcardForm(String icardForm) {
        this.icardForm = icardForm;
    }

    public String getRegistrationForm() {
        return registrationForm;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setRegistrationForm(String registrationForm) {
        this.registrationForm = registrationForm;
    }

    public String getSecurityForm() {
        return securityForm;
    }

    public void setSecurityForm(String securityForm) {
        this.securityForm = securityForm;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getInternId() {
        return internId;
    }

    public void setInternId(String internId) {
        this.internId = internId;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
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

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
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

    public String getUsedResource() {
        return usedResource;
    }

    public void setUsedResource(String usedResource) {
        this.usedResource = usedResource;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date(System.currentTimeMillis());
    }

    public Object getGroupEntity() {
        return group;
    }
}