package com.internalworkflow.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "workflow_id")
	private Workflow workflow;

	@ManyToOne(optional = false)
	@JoinColumn(name = "step_id")
	private WorkflowStep step;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskStatus status = TaskStatus.PENDING;

	@ManyToOne
	@JoinColumn(name = "assigned_user")
	private User assignedUser;

	@Column(name = "due_date")
	private LocalDate dueDate;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Workflow getWorkflow() { return workflow; }
	public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
	public WorkflowStep getStep() { return step; }
	public void setStep(WorkflowStep step) { this.step = step; }
	public TaskStatus getStatus() { return status; }
	public void setStatus(TaskStatus status) { this.status = status; }
	public User getAssignedUser() { return assignedUser; }
	public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }
	public LocalDate getDueDate() { return dueDate; }
	public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
