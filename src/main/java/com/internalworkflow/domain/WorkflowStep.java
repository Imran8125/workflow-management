package com.internalworkflow.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "workflow_steps")
public class WorkflowStep {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "workflow_id")
	@JsonBackReference
	private Workflow workflow;

	@Column(name = "step_name", nullable = false)
	private String stepName;

	@ManyToOne
	@JoinColumn(name = "assigned_to")
	private User assignedTo;

	@Column(name = "step_order", nullable = false)
	private Integer stepOrder;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Workflow getWorkflow() { return workflow; }
	public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
	public String getStepName() { return stepName; }
	public void setStepName(String stepName) { this.stepName = stepName; }
	public User getAssignedTo() { return assignedTo; }
	public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
	public Integer getStepOrder() { return stepOrder; }
	public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
}
