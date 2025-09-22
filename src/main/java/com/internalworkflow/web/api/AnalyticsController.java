package com.internalworkflow.web.api;

import com.internalworkflow.domain.Task;
import com.internalworkflow.domain.TaskStatus;
import com.internalworkflow.repository.TaskRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final TaskRepository taskRepository;

    public AnalyticsController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/pending-vs-completed")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Map<String, Long> pendingVsCompleted() {
        List<Task> tasks = taskRepository.findAll();
        Map<String, Long> result = new HashMap<>();
        result.put("PENDING", tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).count());
        result.put("IN_PROGRESS", tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count());
        result.put("COMPLETED", tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count());
        result.put("REJECTED", tasks.stream().filter(t -> t.getStatus() == TaskStatus.REJECTED).count());
        return result;
    }

    @GetMapping("/approval-time")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Map<String, Object> averageApprovalTime() {
        // Placeholder: we don't track per-task timestamps for state transitions yet
        // Return total completed count as a stub and 0 avg time
        long completed = taskRepository.findAll().stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();
        Map<String, Object> result = new HashMap<>();
        result.put("averageApprovalTimeSeconds", 0);
        result.put("completedCount", completed);
        return result;
    }
}


