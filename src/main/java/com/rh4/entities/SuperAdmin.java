package com.rh4.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "super_admin")
public class SuperAdmin {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "super_admin_id")
    private long superAdminId;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_no", unique = true)
    private Long contactNo;

    @Column(name = "email_id", unique = true)
    private String emailId;

    @Column(name="password")
    private String password;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = true)
    private Date createdAt;

    
	public SuperAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SuperAdmin(Long superAdminId, String name, String location, Long contactNo, String emailId, String password,
			Date createdAt) {
		super();
		this.superAdminId = superAdminId;
		this.name = name;
		this.location = location;
		this.contactNo = contactNo;
		this.emailId = emailId;
		this.password = password;
		this.createdAt = createdAt;
	}


	public Long getSuperAdminId() {
		return superAdminId;
	}


	public void setSuperAdminId(Long superAdminId) {
		this.superAdminId = superAdminId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public Long getContactNo() {
		return contactNo;
	}


	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
    
    
    
    
}
