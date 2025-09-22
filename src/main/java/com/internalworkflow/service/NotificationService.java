package com.internalworkflow.service;

import com.internalworkflow.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendTaskAssigned(Task task) {
        if (task.getAssignedUser() == null || task.getAssignedUser().getEmail() == null) return;
        log.info("[MAIL-DEV] Task assigned to {} for step '{}'", task.getAssignedUser().getEmail(),
                task.getStep() != null ? task.getStep().getStepName() : "-");
    }

    public void sendTaskStatusChanged(Task task) {
        if (task.getAssignedUser() == null || task.getAssignedUser().getEmail() == null) return;
        log.info("[MAIL-DEV] Task status changed to {} for {}", task.getStatus(), task.getAssignedUser().getEmail());
    }
}


