package com.internalworkflow.service;

import com.internalworkflow.domain.Task;
import com.internalworkflow.domain.TaskStatus;
import com.internalworkflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public TaskService(TaskRepository taskRepository, AuditService auditService, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    public List<Task> findAll() { return taskRepository.findAll(); }

    public List<Task> findByFilters(Long assignedToUserId, Long workflowId, TaskStatus status) {
        if (assignedToUserId != null && workflowId != null && status != null) {
            return taskRepository.findByAssignedUser_IdAndWorkflow_IdAndStatus(assignedToUserId, workflowId, status);
        } else if (assignedToUserId != null && workflowId != null) {
            return taskRepository.findByAssignedUser_IdAndWorkflow_Id(assignedToUserId, workflowId);
        } else if (assignedToUserId != null && status != null) {
            return taskRepository.findByAssignedUser_IdAndStatus(assignedToUserId, status);
        } else if (workflowId != null && status != null) {
            return taskRepository.findByWorkflow_IdAndStatus(workflowId, status);
        } else if (assignedToUserId != null) {
            return taskRepository.findByAssignedUser_Id(assignedToUserId);
        } else if (workflowId != null) {
            return taskRepository.findByWorkflow_Id(workflowId);
        } else if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll();
    }
    public Task findByIdOrThrow(Long id) { return taskRepository.findById(id).orElseThrow(); }

    @Transactional
    public Task updateStatus(Long taskId, TaskStatus newStatus) {
        Task task = findByIdOrThrow(taskId);
        // basic transition validation
        TaskStatus current = task.getStatus();
        if (current == TaskStatus.PENDING && (newStatus == TaskStatus.IN_PROGRESS || newStatus == TaskStatus.REJECTED)) {
            task.setStatus(newStatus);
        } else if (current == TaskStatus.IN_PROGRESS && (newStatus == TaskStatus.COMPLETED || newStatus == TaskStatus.REJECTED)) {
            task.setStatus(newStatus);
        } else if (current == newStatus) {
            // no-op
        } else {
            throw new IllegalArgumentException("Invalid status transition: " + current + " -> " + newStatus);
        }
        Task saved = taskRepository.save(task);
        auditService.record("TASK_STATUS_UPDATED", null, "Task id=" + taskId + " status=" + newStatus);
        notificationService.sendTaskStatusChanged(saved);
        return saved;
    }
}


