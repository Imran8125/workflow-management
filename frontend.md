# FRONTEND.md  
## 📌 Frontend Design for Workflow Management System  

### 1. 🎯 Overview  
The frontend is a **pure JavaScript (ES6) application** that communicates with the Spring Boot backend using **REST APIs**.  
It renders UI pages dynamically with HTML, CSS, and JS, and supports role-based navigation (Admin, Manager, Employee).  

Goals:  
- Simple, modular, and reusable JavaScript components.  
- API-driven: fetch data via REST APIs and render in the DOM.  
- Authentication via **Basic Auth** (username + password).  
- Role-aware navigation and restricted pages.  

---

### 2. 📂 File & Folder Structure  

```
frontend/
│── index.html              # Entry page (Login redirect if not logged in)
│── css/
│   └── styles.css          # Global styles
│── js/
│   ├── app.js              # App initialization, router
│   ├── auth.js             # Login, signup, Basic Auth handling
│   ├── api.js              # Wrapper for fetch() calls
│   ├── ui.js               # Utility for DOM rendering
│   ├── dashboard.js        # Dashboard page logic
│   ├── tasks.js            # My Tasks + Task Detail
│   ├── workflows.js        # Workflows list + detail + create workflow
│   ├── analytics.js        # Analytics charts (stub)
│   ├── users.js            # User management (Admin)
│   ├── audit.js            # Audit logs (Admin)
│   └── profile.js          # Profile page
│── pages/
│   ├── login.html
│   ├── signup.html
│   ├── dashboard.html
│   ├── tasks.html
│   ├── task-detail.html
│   ├── workflows.html
│   ├── workflow-detail.html
│   ├── workflow-create.html
│   ├── analytics.html
│   ├── users.html
│   ├── audit.html
│   ├── profile.html
│   ├── 404.html
│   ├── 403.html
│   └── 500.html
```

---

### 3. 🔐 Authentication Flow (Basic Auth)  
- User logs in with username + password → stored in `localStorage` (Base64 encoded).  
- All `fetch()` requests send `Authorization: Basic base64(username:password)` header.  
- On logout, credentials are removed from `localStorage`.  
- Role (Admin/Manager/Employee) fetched from `/users/me` API and used to control navigation.  

---

### 4. 🖥 Pages & UI Requirements  

#### 1. Login Page (`login.html`)  
- Form with Username + Password.  
- On submit → call `/auth/login` (actually, just validate using Basic Auth with `/users/me`).  
- Redirect to Dashboard if success.  

#### 2. Signup Page (`signup.html`)  
- Only for creating **Employee accounts**.  
- Fields: Username, Email, Password, Confirm Password.  
- Calls `POST /users`.  

#### 3. Dashboard Page (`dashboard.html`)  
- Quick stats (via API): Pending Tasks, Completed Tasks, Total Workflows.  
- Links: My Tasks, Workflows, Analytics (if role).  
- Recent activity feed (stubbed).  

#### 4. My Tasks Page (`tasks.html`)  
- Fetch `/tasks?assignedUser=currentUser`.  
- Table view: Task Name, Workflow, Status, Deadline.  
- Filter by status.  
- Clicking a task → Task Detail page.  

#### 5. Task Detail Page (`task-detail.html`)  
- Show task details: workflow, description, status timeline.  
- Buttons: **In Progress**, **Complete**, **Reject** (update via `PUT /tasks/{id}/status`).  
- Comment box (local only, stub).  

#### 6. Workflows List (`workflows.html`)  
- Fetch `/workflows`.  
- Table: ID, Name, Created By, Created At.  
- Search + Pagination.  
- Admin/Manager → buttons: Create, Edit, Delete.  

#### 7. Workflow Detail (`workflow-detail.html`)  
- Show workflow info.  
- Steps with order, assignees, status.  
- Related tasks listed.  
- Admin/Manager → add new steps dynamically.  

#### 8. Create Workflow (`workflow-create.html`)  
- Form: Workflow Name, Description.  
- Dynamic step builder (JS adds rows for step name, assignee, order).  
- Submit → `POST /workflows`.  

#### 9. Analytics (`analytics.html`) [Admin/Manager]  
- Pending vs Completed chart (`/analytics/pending-vs-completed`).  
- Average approval time metric (`/analytics/approval-time`).  
- Charts with **Chart.js** or vanilla Canvas API.  

#### 10. Users Management (`users.html`) [Admin only]  
- Fetch `/users`.  
- Table with Edit/Delete buttons.  
- Add User modal with role dropdown.  

#### 11. Audit Logs (`audit.html`) [Admin only]  
- Fetch `/audit`.  
- Table with Timestamp, User, Action, Details.  
- Filter/search box.  

#### 12. Profile (`profile.html`)  
- Show username/email.  
- Change password form (calls `PUT /users/{id}`).  
- Logout button.  

#### 13. Error Pages (`404.html`, `403.html`, `500.html`)  
- Simple, friendly messages with a “Go Home” button.  

---

### 5. 📡 API Integration (Fetch Wrapper)  

Example `api.js`:  
```js
function getAuthHeader() {
  const creds = localStorage.getItem("auth");
  return { "Authorization": "Basic " + creds };
}

async function apiGet(url) {
  const res = await fetch(url, { headers: getAuthHeader() });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function apiPost(url, body) {
  const res = await fetch(url, {
    method: "POST",
    headers: { 
      "Content-Type": "application/json", 
      ...getAuthHeader() 
    },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
```

---

### 6. 🎨 UI/UX Guidelines  
- Use **flexbox/grid** for layout.  
- Sidebar navigation (role-aware).  
- Top header shows username + logout.  
- Tables should have search + pagination (JS only).  
- Forms with inline validation + error states.  
- Dark/Light theme toggle (CSS variables).  

---

### 7. 🚀 Frontend Roadmap  

**Phase 1: Setup**  
- Basic HTML skeleton, styles, and `app.js` router.  
- Implement Login + Signup.  

**Phase 2: Core Pages**  
- Dashboard, My Tasks, Workflows List, Task Detail.  

**Phase 3: Role-Based Features**  
- Admin: User Management, Audit Logs.  
- Manager/Admin: Create Workflow, Analytics.  

**Phase 4: UX Enhancements**  
- Pagination, search filters, error pages, theme toggle.  

**Phase 5: Testing**  
- Manual API tests with Postman.  
- Browser testing (Chrome, Firefox).  
- End-to-end with Cypress (optional).  

--- 