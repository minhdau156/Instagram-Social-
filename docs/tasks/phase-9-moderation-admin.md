# Phase 9 — Content Moderation & Admin

> **Depends on:** Phase 1, Phase 2, Phase 4  
> **BRD refs:** NFR-007, Admin User Stories  
> **DB tables:** `reports`, `user_blocks`, `audit_logs`  
> **Branch prefix:** `feat/phase-9-`

---

## Backend

### TASK-9.1 — Domain models
- [ ] Create `backend/.../domain/model/Report.java`
  - Fields: `id`, `reporterId`, `entityType` (`POST`/`COMMENT`/`USER`), `entityId`, `reason`, `description`, `status` (`PENDING`/`REVIEWED`/`RESOLVED`/`DISMISSED`), `createdAt`, `resolvedAt`
  - Business method: `withResolved(String resolution)`, `withDismissed()`
- [ ] Create `backend/.../domain/model/UserBlock.java`
  - Fields: `id`, `blockerId`, `blockedId`, `createdAt`

### TASK-9.2 — Domain exceptions
- [ ] Create `backend/.../domain/exception/ReportNotFoundException.java`
- [ ] Create `backend/.../domain/exception/AlreadyBlockedException.java`
- [ ] Create `backend/.../domain/exception/NotBlockedException.java`

### TASK-9.3 — Out-ports
- [ ] `ModerationRepository.java`
  - `saveReport(Report)`, `findReportById(UUID)`, `findPendingReports(Pageable)`
  - `saveBlock(UserBlock)`, `deleteBlock(UUID blockerId, UUID blockedId)`
  - `isBlocked(UUID blockerId, UUID blockedId) → boolean`
  - `findBlockedUsersByBlockerId(UUID, Pageable)`
- [ ] `AuditLogRepository.java`
  - `log(UUID actorId, String action, String entityType, UUID entityId, String detail)`

### TASK-9.4 — In-ports
- [ ] `ReportContentUseCase.java` — `Command(reporterId, entityType, entityId, reason, description?)`
- [ ] `BlockUserUseCase.java` — `Command(blockerId, targetUsername)`
- [ ] `UnblockUserUseCase.java` — `Command(blockerId, targetUsername)`
- [ ] `GetBlockedUsersUseCase.java` — `Query(userId, page, size)`
- [ ] `ReviewReportUseCase.java` — `Command(adminId, reportId, action: RESOLVE|DISMISS, resolutionNote?)`
- [ ] `SuspendUserUseCase.java` — `Command(adminId, targetUserId, reason)`
- [ ] `UnsuspendUserUseCase.java` — `Command(adminId, targetUserId)`
- [ ] `AdminGetReportsUseCase.java` — `Query(status?, page, size)`

### TASK-9.5 — Domain services
- [ ] Create `backend/.../domain/service/ModerationService.java`
  - Implements user-facing in-ports: `ReportContentUseCase`, `BlockUserUseCase`, `UnblockUserUseCase`, `GetBlockedUsersUseCase`
  - Logging: calls `AuditLogRepository.log(...)` for every report/block action
- [ ] Create `backend/.../domain/service/AdminService.java`
  - Implements admin in-ports: `ReviewReportUseCase`, `SuspendUserUseCase`, `UnsuspendUserUseCase`, `AdminGetReportsUseCase`
  - Suspend: updates `User.status = SUSPENDED`; writes audit log; notifies user (email/notification)
  - All methods validate that caller has `ROLE_ADMIN`

### TASK-9.6 — JPA entities & repositories
- [ ] `ReportJpaEntity.java` — `@Table(name = "reports")`
- [ ] `ReportJpaRepository.java` — `findByStatusOrderByCreatedAtDesc(ReportStatus, Pageable)`
- [ ] `UserBlockJpaEntity.java` — `@Table(name = "user_blocks")`, composite PK (`blocker_id`, `blocked_id`)
- [ ] `UserBlockJpaRepository.java` — `existsByBlockerIdAndBlockedId`, `deleteByBlockerIdAndBlockedId`, `findByBlockerIdOrderByCreatedAtDesc`
- [ ] `AuditLogJpaEntity.java` — `@Table(name = "audit_logs")`: `id`, `actorId`, `action`, `entityType`, `entityId`, `detail`, `createdAt`
- [ ] `AuditLogJpaRepository.java`

### TASK-9.7 — Persistence adapters
- [ ] `ModerationPersistenceAdapter.java` — `implements ModerationRepository`
- [ ] `AuditLogPersistenceAdapter.java` — `implements AuditLogRepository`

### TASK-9.8 — Block filtering
- [ ] Update `FeedJpaQueryAdapter` to exclude posts from users who have blocked the current user or been blocked by them
- [ ] Update `SearchJpaAdapter` to exclude blocked users from search results
- [ ] Add utility `BlockFilter.java` in `infrastructure/` — loads blocked user IDs for the current user from cache/DB

### TASK-9.9 — REST controllers
- [ ] Create `ModerationController.java`
  - `POST /api/v1/reports`
  - `POST /api/v1/users/{username}/block`
  - `DELETE /api/v1/users/{username}/block`
  - `GET /api/v1/users/me/blocked`
- [ ] Create `AdminController.java` — secured with `@PreAuthorize("hasRole('ADMIN')")`
  - `GET /api/v1/admin/reports`
  - `PUT /api/v1/admin/reports/{id}`
  - `PUT /api/v1/admin/users/{id}/suspend`
  - `PUT /api/v1/admin/users/{id}/unsuspend`
  - `GET /api/v1/admin/users` — user list with filters

### TASK-9.10 — DTOs
- [ ] `ReportRequest.java` — `entityType`, `entityId`, `reason` (`@NotBlank`), `description?`
- [ ] `ReportResponse.java` — all report fields + reporter username
- [ ] `ReviewReportRequest.java` — `action` (`RESOLVE`/`DISMISS`), `resolutionNote?`
- [ ] `SuspendUserRequest.java` — `reason`

### TASK-9.11 — Tests
- [ ] `ModerationServiceTest.java` — report, block, double-block guard (Mockito)
- [ ] `AdminServiceTest.java` — review report, suspend user, audit log written (Mockito)
- [ ] `ModerationPersistenceAdapterIT.java` — `@DataJpaTest`
- [ ] `ModerationControllerTest.java`, `AdminControllerTest.java` — MockMvc with role-based access checks

---

## Frontend

### TASK-9.12 — TypeScript types
- [ ] Create `frontend/src/types/moderation.ts`
  - `Report`, `ReportReason`, `UserBlock`, `ReportStatus`, `AuditLog`

### TASK-9.13 — API services
- [ ] Create `frontend/src/api/moderationApi.ts`
  - `submitReport(payload)`, `blockUser(username)`, `unblockUser(username)`, `getBlockedUsers()`
- [ ] Create `frontend/src/api/adminApi.ts`
  - `getReports(status?, page)`, `reviewReport(id, payload)`, `suspendUser(id, reason)`, `unsuspendUser(id)`, `getUsers(filters)`

### TASK-9.14 — User-facing components
- [ ] Create `frontend/src/components/moderation/ReportDialog.tsx`
  - MUI Dialog with `RadioGroup` for report reason
  - Optional description textarea
  - Triggered from kebab menus on posts, comments, and user profiles
- [ ] Create `frontend/src/components/moderation/BlockButton.tsx`
  - Shown in user profile kebab menu
  - Calls `blockUser` / `unblockUser` with confirmation dialog

### TASK-9.15 — User-facing pages
- [ ] Create `frontend/src/pages/settings/BlockedAccountsPage.tsx`
  - List of blocked users with `UserListItem` + Unblock button

### TASK-9.16 — Admin panel pages (route prefix `/admin`)
- [ ] Create `frontend/src/pages/admin/AdminDashboardPage.tsx`
  - Basic stats cards: total users, total posts, pending reports
- [ ] Create `frontend/src/pages/admin/AdminReportsPage.tsx`
  - Table of reports, filterable by status (`PENDING`/`REVIEWED`/`RESOLVED`/`DISMISSED`)
  - Per-row actions: Resolve / Dismiss (opens `ReviewReportDialog`)
- [ ] Create `frontend/src/pages/admin/AdminUsersPage.tsx`
  - Table of users with filters (status, username search)
  - Per-row actions: View Profile, Suspend, Unsuspend
- [ ] Create `frontend/src/components/admin/ReviewReportDialog.tsx` — resolution note textarea + action buttons

### TASK-9.17 — Admin route guard
- [ ] Create `frontend/src/components/common/AdminRoute.tsx`
  - Redirects non-admin users to `/` with a toast error

### TASK-9.18 — Register routes
- [ ] Add `/settings/blocked` → `BlockedAccountsPage` (protected)
- [ ] Add `/admin` → `AdminDashboardPage` (AdminRoute)
- [ ] Add `/admin/reports` → `AdminReportsPage` (AdminRoute)
- [ ] Add `/admin/users` → `AdminUsersPage` (AdminRoute)
