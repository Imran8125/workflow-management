package com.internalworkflow.service;

import com.internalworkflow.domain.User;
import com.internalworkflow.domain.Task;
import com.internalworkflow.domain.TaskStatus;
import com.internalworkflow.domain.Workflow;
import com.internalworkflow.domain.WorkflowStep;
import com.internalworkflow.repository.UserRepository;
import com.internalworkflow.repository.WorkflowRepository;
import com.internalworkflow.repository.WorkflowStepRepository;
import com.internalworkflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkflowService {
    private final WorkflowRepository workflowRepository;
    private final WorkflowStepRepository workflowStepRepository;
    private final TaskRepository taskRepository;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public WorkflowService(WorkflowRepository workflowRepository,
                           WorkflowStepRepository workflowStepRepository,
                           UserRepository userRepository,
                           TaskRepository taskRepository,
                           AuditService auditService,
                           NotificationService notificationService) {
        this.workflowRepository = workflowRepository;
        this.workflowStepRepository = workflowStepRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    public List<Workflow> findAll() { return workflowRepository.findAll(); }
    public Workflow findByIdOrThrow(Long id) { return workflowRepository.findById(id).orElseThrow(); }

    @Transactional
    public Workflow createWorkflow(Workflow workflow, List<WorkflowStep> steps, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername).orElseThrow();
        workflow.setCreatedBy(creator);
        Workflow saved = workflowRepository.save(workflow);
        int order = 1;
        for (WorkflowStep step : steps) {
            step.setWorkflow(saved);
            if (step.getStepOrder() == null) {
                step.setStepOrder(order++);
            }
            workflowStepRepository.save(step);
        }

        // Auto-create initial task for the first step
        if (!steps.isEmpty()) {
            WorkflowStep firstStep = steps.stream()
                    .sorted((a, b) -> Integer.compare(a.getStepOrder(), b.getStepOrder()))
                    .findFirst().orElse(null);
            if (firstStep != null) {
                Task task = new Task();
                task.setWorkflow(saved);
                task.setStep(firstStep);
                task.setAssignedUser(firstStep.getAssignedTo());
                task.setStatus(TaskStatus.PENDING);
                Task created = taskRepository.save(task);
                auditService.record("TASK_CREATED", creatorUsername, "Task id=" + created.getId());
                notificationService.sendTaskAssigned(created);
            }
        }
        auditService.record("WORKFLOW_CREATED", creatorUsername, "Workflow id=" + saved.getId());
        return saved;
    }
}


