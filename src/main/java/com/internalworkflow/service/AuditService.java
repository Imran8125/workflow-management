package com.internalworkflow.service;

import com.internalworkflow.domain.AuditLog;
import com.internalworkflow.domain.User;
import com.internalworkflow.repository.AuditLogRepository;
import com.internalworkflow.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public void record(String action, String username, String details) {
        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username).orElse(null);
        }
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setUser(user);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}


