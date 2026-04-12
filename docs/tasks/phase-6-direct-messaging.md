# Phase 6 — Direct Messaging

> **Depends on:** Phase 1, Phase 2  
> **Blocks:** Phase 7 (message notifications)  
> **BRD refs:** FR-0017, FR-0018  
> **DB tables:** `conversations`, `conversation_members`, `messages`, `message_reads`  
> **Branch prefix:** `feat/phase-6-`

---

## Backend

### TASK-6.1 — Domain models
- [ ] Create `backend/.../domain/model/Conversation.java`
  - Fields: `id`, `name` (for group chats), `isGroup`, `createdAt`
  - Business method: `withUpdatedName(String name)`
- [ ] Create `backend/.../domain/model/ConversationMember.java`
  - Fields: `conversationId`, `userId`, `role` (`OWNER` / `MEMBER`), `joinedAt`
- [ ] Create `backend/.../domain/model/Message.java`
  - Fields: `id`, `conversationId`, `senderId`, `content`, `messageType` (`TEXT`/`IMAGE`/`VIDEO`/`POST_SHARE`), `mediaUrl`, `sharedPostId`, `status` (`SENT`/`DELIVERED`/`READ`), `createdAt`
  - Business method: `withRead()`

### TASK-6.2 — Domain exceptions
- [ ] Create `backend/.../domain/exception/ConversationNotFoundException.java`
- [ ] Create `backend/.../domain/exception/NotConversationMemberException.java`
- [ ] Create `backend/.../domain/exception/MessageNotFoundException.java`

### TASK-6.3 — Out-ports
- [ ] `ConversationRepository.java`
  - `save(Conversation)`, `findById(UUID)`, `findByMemberId(UUID userId, Pageable)`
  - `addMember(UUID conversationId, UUID userId)`, `removeMember(UUID conversationId, UUID userId)`
- [ ] `MessageRepository.java`
  - `save(Message)`, `findById(UUID)`
  - `findByConversationId(UUID, UUID cursor, int limit) → List<Message>` (cursor-paginated, newest first)
  - `markAsRead(UUID conversationId, UUID userId, OffsetDateTime readAt)`
  - `getUnreadCount(UUID conversationId, UUID userId) → int`

### TASK-6.4 — In-ports (one file each)
- [ ] `CreateConversationUseCase.java` — `Command(creatorId, participantIds: List<UUID>, name?, isGroup)`
- [ ] `GetConversationsUseCase.java` — `Query(userId, page, size)`
- [ ] `GetMessagesUseCase.java` — `Query(conversationId, requesterId, cursor: UUID?, limit)`
- [ ] `SendMessageUseCase.java` — `Command(conversationId, senderId, content, messageType, mediaUrl?, sharedPostId?)`
- [ ] `MarkReadUseCase.java` — `Command(conversationId, userId)`
- [ ] `AddGroupMemberUseCase.java` — `Command(conversationId, requesterId, newMemberIds)`
- [ ] `LeaveConversationUseCase.java` — `Command(conversationId, userId)`

### TASK-6.5 — Domain service: MessagingService
- [ ] Create `backend/.../domain/service/MessagingService.java`
  - Implements all in-ports from TASK-6.4
  - Before sending: verify `requesterId` is a member of the conversation
  - `createConversation`: for 1-to-1, check if conversation already exists between the two users; reuse if so
  - After `sendMessage`: push WebSocket event to conversation topic

### TASK-6.6 — JPA entities
- [ ] `ConversationJpaEntity.java` — `@Table(name = "conversations")`
- [ ] `ConversationMemberJpaEntity.java` — `@Table(name = "conversation_members")`, composite PK
- [ ] `MessageJpaEntity.java` — `@Table(name = "messages")`
- [ ] `MessageReadJpaEntity.java` — `@Table(name = "message_reads")`, composite PK

### TASK-6.7 — JPA repositories
- [ ] `ConversationJpaRepository.java`
  - `findByMembersUserIdOrderByLatestMessageAtDesc(UUID, Pageable)` — custom JPQL with join on `conversation_members`
  - `findExisting1to1(UUID userId1, UUID userId2)` — native SQL or JPQL to find existing conversation
- [ ] `MessageJpaRepository.java`
  - `findByConversationIdAndIdLessThanOrderByCreatedAtDesc(UUID, UUID cursor, Pageable)`
- [ ] `MessageReadJpaRepository.java` — `countUnreadByConversationIdAndUserId(UUID, UUID)`

### TASK-6.8 — Persistence adapters
- [ ] `ConversationPersistenceAdapter.java` — `implements ConversationRepository`
- [ ] `MessagePersistenceAdapter.java` — `implements MessageRepository`

### TASK-6.9 — WebSocket configuration
- [ ] Create `backend/.../infrastructure/config/WebSocketConfig.java`
  - `@EnableWebSocketMessageBroker`
  - In-memory STOMP broker on `/topic`
  - App destination prefix `/app`
  - SockJS fallback endpoint `/ws`
  - Authenticate WebSocket connections using JWT (in `configureClientInboundChannel`)

### TASK-6.10 — WebSocket controller
- [ ] Create `backend/.../adapter/in/messaging/MessageWebSocketController.java`
  - `@MessageMapping("/chat.send")` → calls `SendMessageUseCase`, then broadcasts to `/topic/conversations/{conversationId}`
  - `@MessageMapping("/chat.typing")` → broadcasts typing indicator to `/topic/conversations/{conversationId}/typing`
  - Returns `MessageResponse` payload via `SimpMessagingTemplate`

### TASK-6.11 — REST controller: MessageController
- [ ] Create `MessageController.java`
  - `GET /api/v1/conversations` — conversation list with last message preview
  - `POST /api/v1/conversations` — create conversation
  - `GET /api/v1/conversations/{id}/messages` — message history
  - `POST /api/v1/conversations/{id}/messages` — REST fallback for sending
  - `PUT /api/v1/conversations/{id}/read` — mark read
  - `POST /api/v1/conversations/{id}/members` — add members (group)
  - `DELETE /api/v1/conversations/{id}/members/me` — leave group

### TASK-6.12 — DTOs
- [ ] `CreateConversationRequest.java` — `participantIds`, `name?`, `isGroup`
- [ ] `SendMessageRequest.java` — `content`, `messageType`, `mediaUrl?`, `sharedPostId?`
- [ ] `ConversationResponse.java` — `id`, `name`, `isGroup`, `members`, `lastMessage`, `unreadCount`
- [ ] `MessageResponse.java` — `id`, `conversationId`, `senderId`, `senderUsername`, `senderAvatarUrl`, `content`, `messageType`, `mediaUrl`, `createdAt`, `status`

### TASK-6.13 — Tests
- [ ] `MessagingServiceTest.java` — create conversation, send message, mark read (Mockito)
- [ ] `ConversationPersistenceAdapterIT.java` — `@DataJpaTest`
- [ ] `MessageControllerTest.java` — REST endpoints (MockMvc)

---

## Frontend

### TASK-6.14 — TypeScript types
- [ ] Create `frontend/src/types/messaging.ts`
  - `Conversation`, `ConversationMember`, `Message`, `MessageType`, `SendMessagePayload`, `CreateConversationPayload`

### TASK-6.15 — API services
- [ ] Create `frontend/src/api/messagingApi.ts`
  - `getConversations()`, `createConversation(payload)`, `getMessages(conversationId, cursor?)`, `sendMessage(conversationId, payload)`, `markRead(conversationId)`

### TASK-6.16 — WebSocket hook
- [ ] Create `frontend/src/hooks/useWebSocket.ts`
  - Connects to `/ws` using SockJS + `@stomp/stompjs`
  - Subscribes to `/topic/conversations/{id}` when a conversation is open
  - On incoming message: updates React Query cache with `queryClient.setQueryData`
  - Reconnects on disconnect with exponential backoff
  - Sends JWT in STOMP connect headers

### TASK-6.17 — Custom hooks
- [ ] `useConversations.ts` — `useQuery` for conversation list; subscribes to global unread count WebSocket topic
- [ ] `useMessages.ts` — `useInfiniteQuery` for message history (loads older messages upward)
- [ ] `useSendMessage.ts` — `useMutation` with optimistic message insertion

### TASK-6.18 — Message components
- [ ] Create `frontend/src/components/messaging/MessageBubble.tsx`
  - Variants: text, image, video, post-share card
  - Right-aligned for own messages, left-aligned for others
  - Sent / delivered / read status indicator for own messages
- [ ] Create `frontend/src/components/messaging/ConversationListItem.tsx`
  - Avatar + name + last message preview + timestamp + unread badge
- [ ] Create `frontend/src/components/messaging/TypingIndicator.tsx`
  - Animated 3-dot bubble shown when other user is typing

### TASK-6.19 — Pages
- [ ] Create `frontend/src/pages/messaging/InboxPage.tsx`
  - Left panel: scrollable `ConversationListItem` list from `useConversations`
  - Right panel: selected `ChatPage` (desktop split view) or navigate to `/messages/:id` (mobile)
- [ ] Create `frontend/src/pages/messaging/ChatPage.tsx`
  - Infinite scroll upward for message history (`useMessages`)
  - Message input bar with emoji picker, attachment button (uploads media before sending)
  - Shows `TypingIndicator` when other user is composing
  - Auto-scrolls to bottom on new message
- [ ] Create `frontend/src/components/messaging/NewConversationDialog.tsx`
  - User search input → shows `UserListItem` results → select → `createConversation`
- [ ] Create `frontend/src/components/messaging/GroupChatDialog.tsx`
  - Multi-select user search → group name input → `createConversation(isGroup: true)`

### TASK-6.20 — Unread badge in navigation
- [ ] Update the navigation/app bar component to show unread message count badge on the inbox icon
  - Subscribe to `/user/{userId}/topic/unread-count` WebSocket topic
  - Fallback: derive count from `useConversations` data

### TASK-6.21 — Register routes
- [ ] Add `/messages` → `InboxPage` (protected)
- [ ] Add `/messages/:conversationId` → mobile `ChatPage` (protected)
