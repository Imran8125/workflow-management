package com.internalworkflow.web;

import com.internalworkflow.repository.TaskRepository;
import com.internalworkflow.repository.WorkflowRepository;
import com.internalworkflow.service.UserService;
import com.internalworkflow.domain.TaskStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppController {

    private final TaskRepository taskRepository;
    private final WorkflowRepository workflowRepository;
    private final UserService userService;

    public AppController(TaskRepository taskRepository, WorkflowRepository workflowRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.workflowRepository = workflowRepository;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        long pending = taskRepository.findAll().stream().filter(t -> t.getStatus() != null && t.getStatus() == TaskStatus.PENDING).count();
        long completed = taskRepository.findAll().stream().filter(t -> t.getStatus() != null && t.getStatus() == TaskStatus.COMPLETED).count();
        model.addAttribute("pendingCount", pending);
        model.addAttribute("completedCount", completed);
        model.addAttribute("workflowsCount", workflowRepository.count());
        model.addAttribute("username", auth != null ? auth.getName() : "");
        return "app/dashboard";
    }

    @GetMapping("/tasks")
    public String tasks() { return "app/tasks"; }

    @GetMapping("/workflows")
    public String workflows() { return "app/workflows"; }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/analytics")
    public String analytics() { return "app/analytics"; }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String users() { return "app/users"; }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("username", auth != null ? auth.getName() : "");
        return "app/profile";
    }
}


