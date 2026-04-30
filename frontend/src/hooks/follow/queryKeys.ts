/**
 * Centralized cache key factory for the social-graph feature.
 * Use these constants everywhere instead of inline string arrays
 * to prevent typos and ensure consistent cache invalidation.
 */
export const followKeys = {
  profile: (username: string) => ['profile', username] as const,
  followers: (username: string) => ['followers', username] as const,
  following: (username: string) => ['following', username] as const,
  requests: ['followRequests'] as const,
};
