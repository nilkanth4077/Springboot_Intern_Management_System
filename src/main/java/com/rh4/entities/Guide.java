package com.rh4.entities;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "guide")
public class Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "guide_id")
	private long guideId;

	@NotNull
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Only characters are allowed")
	@Column(name = "name")
	private String name;

	@NotNull
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Only characters are allowed")
	@Column(name = "location")
	private String location;

	@NotNull
	@Digits(integer = 10, fraction = 0, message = "Contact number must be a numeric value with up to 10 digits")
	@Column(name = "floor")
	private long floor;

	@NotNull
	@Digits(integer = 10, fraction = 0, message = "Contact number must be a numeric value with up to 10 digits")
	@Column(name = "lab_no")
	private long labNo;

	@NotNull
	@Digits(integer = 10, fraction = 0, message = "Contact number must be a numeric value with up to 10 digits")
	@Column(name = "contact_no", unique = true)
	private long contactNo;

	@Email
	@Column(name = "email_id", unique = true)
	private String emailId;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = true)
	private Date createdAt;

	@Column(name = "password")
	private String password;

	@OneToMany(mappedBy = "guide")
	private List<GroupEntity> groups;

	public List<GroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupEntity> groups) {
		this.groups = groups;
	}

	public Guide() {
		super();
	}

	public Guide(long guideId,
			@NotNull @Size(min = 2, max = 20) @Pattern(regexp = "^[a-zA-Z ]+$", message = "Only characters are allowed") String name,
			@NotNull @Size(min = 2, max = 20) @Pattern(regexp = "^[a-zA-Z ]+$", message = "Only characters are allowed") String location,
			@NotNull @Digits(integer = 10, fraction = 0, message = "Contact number must be a numeric value with up to 10 digits") long floor,
			@NotNull @Digits(integer = 10, fraction = 0, message = "Contact number must be a numeric value with up to 10 digits") long labNo,
			@NotNull @Digits(integer = 10, fraction = 0, message = "Contact number must be a numeric value with up to 10 digits") long contactNo,
			@Email String emailId, Date createdAt, String password) {
		super();
		this.guideId = guideId;
		this.name = name;
		this.location = location;
		this.floor = floor;
		this.labNo = labNo;
		this.contactNo = contactNo;
		this.emailId = emailId;
		this.createdAt = createdAt;
		this.password = password;
	}

	public long getGuideId() {
		return guideId;
	}

	public void setGuideId(long guideId) {
		this.guideId = guideId;
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

	public long getFloor() {
		return floor;
	}

	public void setFloor(long floor) {
		this.floor = floor;
	}

	public long getLabNo() {
		return labNo;
	}

	public void setLabNo(long labNo) {
		this.labNo = labNo;
	}

	public long getContactNo() {
		return contactNo;
	}

	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}