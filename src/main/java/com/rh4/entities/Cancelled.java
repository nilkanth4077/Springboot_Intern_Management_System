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
@Table(name = "cancelled")
public class Cancelled {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

	@Column(name = "table_name")
    private String tableName;

	@Column(name = "cancel_id")
	private String cancelId;

	public Cancelled() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cancelled(long id, String tableName, String cancelId) {
		super();
		this.id = id;
		this.tableName = tableName;
		this.cancelId = cancelId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCancelId() {
		return cancelId;
	}

	public void setCancelId(String cancelId) {
		this.cancelId = cancelId;
	}
	
	
}