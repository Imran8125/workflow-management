package com.internalworkflow.web.api;

import com.internalworkflow.domain.User;
import com.internalworkflow.domain.Workflow;
import com.internalworkflow.domain.WorkflowStep;
import com.internalworkflow.repository.UserRepository;
import com.internalworkflow.service.WorkflowService;
import com.internalworkflow.web.api.dto.Models.WorkflowDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final UserRepository userRepository;

    public WorkflowController(WorkflowService workflowService, UserRepository userRepository) {
        this.workflowService = workflowService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Page<WorkflowDto> list(Pageable pageable) {
        var all = workflowService.findAll().stream().map(WorkflowDto::from).toList();
        int start = (int) Math.min((long) pageable.getPageNumber() * pageable.getPageSize(), all.size());
        int end = Math.min(start + pageable.getPageSize(), all.size());
        var content = all.subList(start, end);
        return new PageImpl<>(content, pageable, all.size());
    }

    @GetMapping("/{id}")
    public WorkflowDto get(@PathVariable Long id) {
        return WorkflowDto.from(workflowService.findByIdOrThrow(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public WorkflowDto create(@Valid @RequestBody CreateWorkflowRequest request, Authentication auth) {
        Workflow wf = new Workflow();
        wf.setName(request.getName());
        wf.setDescription(request.getDescription());

        List<WorkflowStep> steps = new ArrayList<>();
        int index = 1;
        for (CreateWorkflowRequest.Step stepReq : request.getSteps()) {
            WorkflowStep step = new WorkflowStep();
            step.setStepName(stepReq.getStepName());
            step.setStepOrder(stepReq.getOrder() != null ? stepReq.getOrder() : index++);
            if (stepReq.getAssignedToUserId() != null) {
                User assignee = userRepository.findById(stepReq.getAssignedToUserId()).orElse(null);
                step.setAssignedTo(assignee);
            }
            steps.add(step);
        }

        return WorkflowDto.from(workflowService.createWorkflow(wf, steps, auth.getName()));
    }

    public static class CreateWorkflowRequest {
        @NotBlank
        @Size(min = 3, max = 200)
        private String name;
        private String description;
        private List<Step> steps = List.of();

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<Step> getSteps() { return steps; }
        public void setSteps(List<Step> steps) { this.steps = steps; }

        public static class Step {
            @NotBlank
            private String stepName;
            private Integer order;
            private Long assignedToUserId;

            public String getStepName() { return stepName; }
            public void setStepName(String stepName) { this.stepName = stepName; }
            public Integer getOrder() { return order; }
            public void setOrder(Integer order) { this.order = order; }
            public Long getAssignedToUserId() { return assignedToUserId; }
            public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }
        }
    }
}


