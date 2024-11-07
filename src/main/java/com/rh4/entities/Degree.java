package com.rh4.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "degree")
public class Degree {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "degree_id")
    private long degreeId;

    @Column(name = "name")
    private String name;
	public Degree() {
		super();
	}

	public long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(long degreeId) {
		this.degreeId = degreeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}