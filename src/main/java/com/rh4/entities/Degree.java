package com.rh4.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "degree")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Degree {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "degree_id")
	private Long degreeId;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}