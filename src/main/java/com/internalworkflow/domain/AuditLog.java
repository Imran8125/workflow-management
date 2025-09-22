package com.internalworkflow.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String action;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private OffsetDateTime timestamp = OffsetDateTime.now();

	@Column(length = 2000)
	private String details;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public OffsetDateTime getTimestamp() { return timestamp; }
	public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
	public String getDetails() { return details; }
	public void setDetails(String details) { this.details = details; }
}
