# Phase 7 — Notifications

> **Depends on:** Phase 4 (Interactions), Phase 6 (Messaging), Phase 3 (Follow)  
> **BRD refs:** FR-0019, FR-0020  
> **DB tables:** `notifications`, `notification_settings`, `device_tokens`  
> **Branch prefix:** `feat/phase-7-`

---

## Backend

### TASK-7.1 — Domain model: Notification
- [ ] Create `backend/.../domain/model/Notification.java`
  - Fields: `id`, `recipientId`, `actorId`, `entityType` (`POST`/`COMMENT`/`FOLLOW`/`MESSAGE`), `entityId`, `type` (`LIKE`/`COMMENT`/`MENTION`/`FOLLOW`/`FOLLOW_REQUEST`/`MESSAGE`), `isRead`, `createdAt`
  - Business method: `withRead()`
  - No framework annotations

### TASK-7.2 — Domain model: NotificationSettings
- [ ] Create `backend/.../domain/model/NotificationSettings.java`
  - Fields: `userId`, `likesEnabled`, `commentsEnabled`, `followsEnabled`, `mentionsEnabled`, `messagesEnabled`, `pushEnabled`

### TASK-7.3 — Domain exceptions
- [ ] Create `backend/.../domain/exception/NotificationNotFoundException.java`

### TASK-7.4 — Out-ports
- [ ] `NotificationRepository.java`
  - `save(Notification)`
  - `findByRecipientId(UUID, Pageable) → List<Notification>`
  - `markAsRead(UUID notificationId)`
  - `markAllAsRead(UUID recipientId)`
  - `getUnreadCount(UUID recipientId) → long`
- [ ] `NotificationSettingsRepository.java`
  - `findByUserId(UUID) → Optional<NotificationSettings>`
  - `save(NotificationSettings)`
- [ ] `PushNotificationPort.java`
  - `sendPush(UUID userId, String title, String body)` — abstract over FCM/APNs

### TASK-7.5 — In-ports
- [ ] `GetNotificationsUseCase.java` — `Query(userId, page, size)`
- [ ] `MarkNotificationReadUseCase.java` — `Command(notificationId, userId)`
- [ ] `MarkAllNotificationsReadUseCase.java` — `Command(userId)`
- [ ] `GetNotificationSettingsUseCase.java` — `Query(userId)`
- [ ] `UpdateNotificationSettingsUseCase.java` — `Command(userId, settings...)`
- [ ] `RegisterDeviceTokenUseCase.java` — `Command(userId, token, platform)`

### TASK-7.6 — Domain service: NotificationService
- [ ] Create `backend/.../domain/service/NotificationService.java`
  - Implements all in-ports from TASK-7.5
  - `createNotification(type, recipientId, actorId, entityId)` — called from other services as a domain event handler
  - Before creating: check `NotificationSettings` to respect user preferences
  - After creating: push via WebSocket (`/user/{recipientId}/topic/notifications`) AND optionally via `PushNotificationPort`

### TASK-7.7 — Domain event integration
- [ ] Create `backend/.../domain/event/NotificationEvent.java` — Spring `ApplicationEvent` subclass
  - Fields: `type`, `recipientId`, `actorId`, `entityId`, `entityType`
- [ ] Update `LikeService` to publish `NotificationEvent` after a like
- [ ] Update `CommentService` to publish `NotificationEvent` after a comment and after a `@mention`
- [ ] Update `FollowService` to publish `NotificationEvent` after a follow / follow request
- [ ] Create `@EventListener NotificationEventHandler.java` in `infrastructure/` that calls `NotificationService.createNotification(...)`

### TASK-7.8 — Device token out-port & adapter
- [ ] Create `DeviceTokenJpaEntity.java` — `@Table(name = "device_tokens")`: `id`, `userId`, `token`, `platform` (`FCM`/`APNS`), `createdAt`
- [ ] Create `DeviceTokenJpaRepository.java`
- [ ] Create `FcmPushAdapter.java` — implements `PushNotificationPort` (stub: log push payload for now)

### TASK-7.9 — JPA entities & repositories
- [ ] `NotificationJpaEntity.java` — `@Table(name = "notifications")`
- [ ] `NotificationJpaRepository.java`
  - `findByRecipientIdOrderByCreatedAtDesc(UUID, Pageable)`
  - `@Modifying @Query` for `markAllAsRead`
  - `countByRecipientIdAndIsReadFalse(UUID)`
- [ ] `NotificationSettingsJpaEntity.java` — `@Table(name = "notification_settings")`
- [ ] `NotificationSettingsJpaRepository.java`

### TASK-7.10 — Persistence adapters
- [ ] `NotificationPersistenceAdapter.java` — `implements NotificationRepository`
- [ ] `NotificationSettingsPersistenceAdapter.java` — `implements NotificationSettingsRepository`

### TASK-7.11 — WebSocket push
- [ ] Update `WebSocketConfig.java` to enable user-destination prefix `/user`
- [ ] In `NotificationService`: inject `SimpMessagingTemplate`; call `convertAndSendToUser(recipientId, "/topic/notifications", notificationPayload)` after saving

### TASK-7.12 — REST controller: NotificationController
- [ ] Create `NotificationController.java`
  - `GET /api/v1/notifications`
  - `PUT /api/v1/notifications/{id}/read`
  - `PUT /api/v1/notifications/read-all`
  - `GET /api/v1/notifications/settings`
  - `PUT /api/v1/notifications/settings`
  - `POST /api/v1/device-tokens`

### TASK-7.13 — DTOs
- [ ] `NotificationResponse.java` — `id`, `type`, `entityType`, `entityId`, `actorUsername`, `actorAvatarUrl`, `isRead`, `createdAt`, factory `from(Notification, User actor)`
- [ ] `NotificationSettingsRequest.java` — all boolean fields
- [ ] `RegisterDeviceTokenRequest.java` — `token`, `platform`

### TASK-7.14 — Tests
- [ ] `NotificationServiceTest.java` — create, mark read, settings check (Mockito)
- [ ] `NotificationEventHandlerTest.java` — verify events trigger notification creation
- [ ] `NotificationPersistenceAdapterIT.java` — `@DataJpaTest`
- [ ] `NotificationControllerTest.java` — MockMvc

---

## Frontend

### TASK-7.15 — TypeScript types
- [ ] Create `frontend/src/types/notification.ts`
  - `Notification`, `NotificationType`, `NotificationSettings`, `RegisterDeviceTokenPayload`

### TASK-7.16 — API services
- [ ] Create `frontend/src/api/notificationsApi.ts`
  - `getNotifications(page)`, `markRead(id)`, `markAllRead()`, `getSettings()`, `updateSettings(settings)`, `registerDeviceToken(token, platform)`

### TASK-7.17 — Custom hooks
- [ ] `useNotifications.ts` — `useInfiniteQuery` for paginated notification list
- [ ] `useNotificationSettings.ts` — `useQuery` + `useMutation`
- [ ] `useUnreadNotifications.ts` — subscribes to `/user/{userId}/topic/notifications` WebSocket topic; increments local unread counter on new message, resets on `markAllRead`

### TASK-7.18 — Notification components
- [ ] Create `frontend/src/components/notifications/NotificationItem.tsx`
  - Actor avatar + generated action text (e.g., "**user1** liked your photo") + post thumbnail + timestamp
  - Unread notification has highlighted background
  - Click → navigate to relevant content (post, profile, conversation)
- [ ] Create `frontend/src/components/notifications/NotificationDropdown.tsx`
  - MUI `Popover` triggered by bell icon in app bar
  - Shows top 10 recent notifications
  - "Mark all as read" button
  - "See all" link → `/notifications`
- [ ] Create `frontend/src/components/notifications/UnreadBadge.tsx`
  - Bell icon with MUI `Badge` showing unread count (from `useUnreadNotifications`)

### TASK-7.19 — Pages
- [ ] Create `frontend/src/pages/notifications/NotificationsPage.tsx`
  - Full list grouped by time buckets ("Today", "This Week", "Earlier")
  - Uses `useNotifications` infinite query
- [ ] Create `frontend/src/pages/notifications/NotificationSettingsPage.tsx`
  - Toggle switches for each notification type using `useNotificationSettings`

### TASK-7.20 — Integrate into app bar
- [ ] Update app bar to include `UnreadBadge` triggering `NotificationDropdown`

### TASK-7.21 — Register routes
- [ ] Add `/notifications` → `NotificationsPage` (protected)
- [ ] Add `/settings/notifications` → `NotificationSettingsPage` (protected)
