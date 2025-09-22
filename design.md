# DESIGN.md  
## 📌 Project: Internal Workflow Automation Platform  

### 1. 🎯 Overview  
This project is a **Spring Boot–based web application** designed to automate business workflows (e.g., leave requests, approvals, expense claims). It provides REST APIs for backend operations, a SQL database for persistence, and a JavaScript/TypeScript frontend for visualization.  

The system focuses on:  
- **Workflow automation**  
- **Role-based access control**  
- **API-first architecture (RESTful services)**  
- **Integration with third-party APIs (e.g., email/Slack notifications)**  

---

### 2. 🛠 Features  

#### Core Features  
- **User Management**  
  - Sign up, login, role assignment (Admin, Manager, Employee).  
  - CRUD APIs for users.  

- **Workflow Management**  
  - Create custom workflows (e.g., "Leave Approval").  
  - Define steps and assign them to users.  
  - Track progress of workflow execution.  

- **Task Management**  
  - Each workflow generates tasks assigned to users.  
  - Task statuses: Pending → In Progress → Completed → Rejected.  

- **Notifications**  
  - Email notifications for task assignments and approvals.  

- **Analytics Dashboard**  
  - Average approval times.  
  - Pending vs completed tasks.  
  - Bottleneck detection.  

---

### 3. 🏗 System Architecture  

- **Frontend**  
  - React JS.  
  - Consumes REST APIs.  
  - Displays workflows, tasks, analytics.  

- **Backend (Spring Boot)**  
  - RESTful APIs for users, workflows, tasks.  
  - **Spring Security with Basic Authentication.**  
  - Spring Data JPA for database interactions.  

- **Database (PostgreSQL)**  
  - Store users, roles, workflows, tasks, and logs.  

- **External Integration**  
  - Email API (SendGrid / JavaMail).  
 

---

### 4. 📂 Database Schema  

**Tables:**  
1. `users`  
   - `id`, `username`, `password`, `email`, `role`  

2. `workflows`  
   - `id`, `name`, `description`, `created_by`, `created_at`  

3. `workflow_steps`  
   - `id`, `workflow_id`, `step_name`, `assigned_to`, `order`  

4. `tasks`  
   - `id`, `workflow_id`, `step_id`, `status`, `assigned_user`, `due_date`  

5. `audit_logs`  
   - `id`, `action`, `user_id`, `timestamp`, `details`  

---

### 5. 📡 API Endpoints  

**Authentication (Basic Auth)**  
- All endpoints are protected with **Basic Authentication**.  
- Users must send `username:password` in the `Authorization` header.  

**User APIs**  
- `GET /users` → List all users (Admin only).  
- `POST /users` → Create user.  
- `PUT /users/{id}` → Update user.  
- `DELETE /users/{id}` → Delete user.  

**Workflow APIs**  
- `GET /workflows` → List workflows.  
- `POST /workflows` → Create workflow.  
- `GET /workflows/{id}` → Workflow details.  

**Task APIs**  
- `GET /tasks` → List tasks by user/workflow.  
- `PUT /tasks/{id}/status` → Update task status.  

**Analytics**  
- `GET /analytics/approval-time`  
- `GET /analytics/pending-vs-completed`  

---

### 6. 📅 Planning & Milestones  

**Phase 1: Setup & Basics**  
- [x] Initialize Spring Boot project with Gradle.  
- [x] Setup database with JPA.  
- [x] Configure **Basic Authentication** with Spring Security.  

**Phase 2: Core Features** 
- [x] User management APIs.  
- [x] Workflow + task APIs.  
- [x] Database relationships.  

**Phase 3: Integration**  
- [?] Add email notifications.  
- [?] Add analytics APIs.  

**Phase 4: Frontend**  
- [ ] Build dashboard UI.  
- [ ] Connect frontend to REST APIs.  

**Optional**
**Phase 5: Testing & Deployment**  
- [ ] Unit + integration tests.  
- [ ] Dockerize app.  
- [ ] Deploy on cloud (AWS).  

---
### Optional
### 7. 🧪 Testing Strategy  

#### Unit Testing  
- JUnit + Mockito for backend logic.  
- Example: Verify workflow creation assigns tasks correctly.  

#### Integration Testing  
- Spring Boot Test + Testcontainers for DB.  
- Example: Test `/users` endpoint with Basic Auth.  

#### API Testing  
- Postman collection for REST APIs.  
- Example: Create workflow → assign tasks → verify completion.  

#### Frontend Testing (if React/TS used)  
- Jest + React Testing Library.  
- Example: Workflow dashboard renders task list correctly.  

#### End-to-End (E2E) Testing  
- Selenium or Cypress.  
- Example: Login with Basic Auth → create workflow → approve task → verify analytics.  

---
