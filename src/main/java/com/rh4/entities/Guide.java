package com.rh4.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "guide")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "guide_id")
	private Long guideId;

	@NotNull
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Only characters are allowed")
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Only characters are allowed")
	@Column(name = "location", nullable = false)
	private String location;

	@NotNull
	@Digits(integer = 10, fraction = 0, message = "Floor must be numeric with up to 10 digits")
	@Column(name = "floor", nullable = false)
	private Long floor;

	@NotNull
	@Digits(integer = 10, fraction = 0, message = "Lab number must be numeric with up to 10 digits")
	@Column(name = "lab_no", nullable = false)
	private Long labNo;

	@NotNull
	@Digits(integer = 10, fraction = 0, message = "Contact number must be numeric with up to 10 digits")
	@Column(name = "contact_no", unique = true, nullable = false)
	private Long contactNo;

	@Email
	@Column(name = "email_id", unique = true)
	private String emailId;

	@Column(name = "password", nullable = false)
	private String password;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "guide")
	private List<GroupEntity> groups;

	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
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

	public Long getFloor() {
		return floor;
	}

	public void setFloor(Long floor) {
		this.floor = floor;
	}

	public Long getLabNo() {
		return labNo;
	}

	public void setLabNo(Long labNo) {
		this.labNo = labNo;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<GroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupEntity> groups) {
		this.groups = groups;
	}
}