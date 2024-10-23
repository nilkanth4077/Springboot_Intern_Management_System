package com.rh4.entities;

import java.sql.Date;
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
    private String contactNo;

    @Column(name = "email_id", unique=true)
    private String email;

    @Column(name = "college_name")
    private String collegeName;
    
	@Column(name = "branch_name")
    private String branch;

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
	
    @Column(name = "semester")
    private int semester;
    
    @Column(name = "password")
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
	
	@Column(name = "group_created" )
	private Boolean groupCreated = false;
	
	@Column(name="is_active")
	private Boolean isActive = true;
}