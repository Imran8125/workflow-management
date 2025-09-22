package com.internalworkflow.web.api.dto;

import com.internalworkflow.domain.*;

import java.time.OffsetDateTime;
import java.util.List;

public class Models {

    public record UserDto(Long id, String username, String email, UserRole role) {
        public static UserDto from(User u) { return u == null ? null : new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRole()); }
    }

    public record WorkflowStepDto(Long id, String stepName, UserDto assignedTo, Integer stepOrder) {
        public static WorkflowStepDto from(WorkflowStep s) {
            return new WorkflowStepDto(s.getId(), s.getStepName(), UserDto.from(s.getAssignedTo()), s.getStepOrder());
        }
    }

    public record WorkflowDto(Long id, String name, String description, UserDto createdBy, OffsetDateTime createdAt, List<WorkflowStepDto> steps) {
        public static WorkflowDto from(Workflow w) {
            List<WorkflowStepDto> steps = w.getSteps().stream().map(WorkflowStepDto::from).toList();
            return new WorkflowDto(w.getId(), w.getName(), w.getDescription(), UserDto.from(w.getCreatedBy()), w.getCreatedAt(), steps);
        }
    }

    public record TaskDto(Long id, Long workflowId, Long stepId, String status, UserDto assignedUser) {
        public static TaskDto from(Task t) {
            return new TaskDto(
                    t.getId(),
                    t.getWorkflow() != null ? t.getWorkflow().getId() : null,
                    t.getStep() != null ? t.getStep().getId() : null,
                    t.getStatus().name(),
                    UserDto.from(t.getAssignedUser())
            );
        }
    }
}


