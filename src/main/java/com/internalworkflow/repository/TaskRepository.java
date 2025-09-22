package com.internalworkflow.repository;

import com.internalworkflow.domain.Task;
import com.internalworkflow.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedUser_Id(Long userId);
    List<Task> findByWorkflow_Id(Long workflowId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByAssignedUser_IdAndWorkflow_Id(Long userId, Long workflowId);
    List<Task> findByAssignedUser_IdAndStatus(Long userId, TaskStatus status);
    List<Task> findByWorkflow_IdAndStatus(Long workflowId, TaskStatus status);
    List<Task> findByAssignedUser_IdAndWorkflow_IdAndStatus(Long userId, Long workflowId, TaskStatus status);
}
