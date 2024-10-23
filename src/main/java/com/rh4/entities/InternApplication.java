package com.rh4.entities;


import java.sql.Date;

import jakarta.annotation.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "internapplication")
public class InternApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only characters are allowed")
    private String firstName;

    @Column(name = "last_name")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only characters are allowed")
    private String lastName;

    @Column(name = "contact_number", unique = true)
    @Pattern(regexp = "^[0-9+]+$", message = "Only digits and '+' are allowed")
    @Size(min = 10, max = 13, message = "Contact number must be between 10 and 13 characters long")
    private String contactNo;

    @Column(name = "email_id", unique=true)
    private String email;

    @Column(name = "college_name")
    private String collegeName;
    
	@Column(name = "branch_name")
    private String branch;
	
	@Column(name = "passport_size_image", length = 1000)
    private String passportSizeImage;

	@Column(name = "icard_image", length = 1000)
    private String icardImage;
    
	@Column(name = "noc_pdf", length = 1000)
    private String nocPdf;
    
	@Column(name = "resume_pdf", length = 1000)
	private String resumePdf;
	
    @Column(name = "semester")
    @Min(value = 1, message = "Semester must be between 1 and 8")
    @Max(value = 8, message = "Semester must be between 1 and 8")
    private int semester;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "degree")
    private String degree;
	
	@Column(name = "status")
	private String status = "pending";
	
	@Column(name = "final_status")
	private String finalStatus = "pending";
	
	@Column(name = "programming_lang_name")
    private String programmingLangName;
	
	@Column(name = "joining_date")
    private Date joiningDate;

    @Column(name = "completion_date")
    @Future(message = "Completion date must be in the future")
    private Date completionDate;
    
    @AssertTrue(message = "Completion date must be after joining date")
    private boolean isCompletionDateAfterJoiningDate() {
        // Check if completion date is after joining date
        return completionDate == null || completionDate.after(joiningDate);
    }
	
	@Column(name = "group_created" )
	private Boolean groupCreated = false;
	
	@Column(name="is_active")
	private Boolean isActive = true;
	
	public InternApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

  

	public InternApplication(long id, String firstName, String lastName, String contactNo, String email, String passportSizeImage,
			String collegeName, String branch, String icardImage, String nocPdf, String resumePdf, int semester,
			String status, String finalStatus, String programmingLangName, Date joiningDate, Date completionDate,String password,
			Boolean groupCreated,String degree, Boolean isActive) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNo = contactNo;
		this.email = email;
		this.collegeName = collegeName;
		this.branch = branch;
		this.icardImage = icardImage;
		this.passportSizeImage = passportSizeImage;
		this.nocPdf = nocPdf;
		this.resumePdf = resumePdf;
		this.semester = semester;
		this.status = status;
		this.finalStatus = finalStatus;
		this.password = password;
		this.programmingLangName = programmingLangName;
		this.joiningDate = joiningDate;
		this.completionDate = completionDate;
		this.degree = degree;
		this.groupCreated = groupCreated;
		this.isActive = isActive;
	}

	  
	  public String getPassportSizeImage() {
	    return passportSizeImage;
	  }

	  public void setPassportSizeImage(String passportSizeImage) {
	    this.passportSizeImage = passportSizeImage;
	  }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	
	public String getDegree() {
		return degree;
	}



	public void setDegree(String degree) {
		this.degree = degree;
	}



	public String getContactNo() {
		return contactNo;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}



	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	

	public Boolean getIsActive() {
		return isActive;
	}



	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}



	public Date getCompletionDate() {
		return completionDate;
	}



	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
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

	public String getIcardImage() {
		return icardImage;
	}

	public void setIcardImage(String icardImage) {
		this.icardImage = icardImage;
	}

	public String getNocPdf() {
		return nocPdf;
	}

	public void setNocPdf(String nocPdf) {
		this.nocPdf = nocPdf;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getResumePdf() {
		return resumePdf;
	}

	public void setResumePdf(String resumePdf) {
		this.resumePdf = resumePdf;
	}

	public String getFinal_status() {
		return finalStatus;
	}

	public void setFinal_status(String final_status) {
		this.finalStatus = final_status;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public Boolean getGroupCreated() {
		return groupCreated;
	}

	public void setGroupCreated(Boolean groupCreated) {
		this.groupCreated = groupCreated;
	}

	public String getProgrammingLangName() {
		return programmingLangName;
	}

	public void setProgrammingLangName(String programmingLangName) {
		this.programmingLangName = programmingLangName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
    
}