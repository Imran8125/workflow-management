package com.internalworkflow.web.api;

import com.internalworkflow.domain.TaskStatus;
import com.internalworkflow.service.TaskService;
import com.internalworkflow.web.api.dto.Models.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Page<TaskDto> list(
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) Long workflowId,
            @RequestParam(required = false) TaskStatus status,
            Pageable pageable
    ) {
        var all = taskService.findByFilters(assignedTo, workflowId, status).stream().map(TaskDto::from).toList();
        int start = (int) Math.min((long) pageable.getPageNumber() * pageable.getPageSize(), all.size());
        int end = Math.min(start + pageable.getPageSize(), all.size());
        var content = all.subList(start, end);
        return new PageImpl<>(content, pageable, all.size());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @PutMapping("/{id}/status")
    public TaskDto updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return TaskDto.from(taskService.updateStatus(id, request.getStatus()));
    }

    public static class UpdateStatusRequest {
        @NotNull
        private TaskStatus status;

        public TaskStatus getStatus() { return status; }
        public void setStatus(TaskStatus status) { this.status = status; }
    }
}


