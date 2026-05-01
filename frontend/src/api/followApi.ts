
import type { Follow, UserSummary } from "../types/follow";
import { api } from "./client";

const BASE = "/api/v1"

/** Follow a user by username. Returns the created Follow object. */
export async function followUser(username: string): Promise<Follow> {
    const { data } = await api.post(`${BASE}/users/${username}/follow`);
    return data.data;
}

/** Unfollow a user by username. */
export async function unfollowUser(username: string): Promise<void> {
    await api.delete(`${BASE}/users/${username}/follow`);
}

/** Get paginated followers of a user. */
export async function getFollowers(username: string, page: number = 0, size: number = 20): Promise<UserSummary[]> {
    const { data } = await api.get(`${BASE}/users/${username}/followers`, {
        params: { page, size }
    });
    return data.data;
}

/** Get paginated following list of a user. */
export async function getFollowing(username: string, page: number = 0, size: number = 20): Promise<UserSummary[]> {
    const { data } = await api.get(`${BASE}/users/${username}/following`, {
        params: { page, size }
    });
    return data.data;
}

/** Get all pending follow requests for the current user. */
export async function getFollowRequests(): Promise<UserSummary[]> {
    const { data } = await api.get(`${BASE}/follow-requests`);
    return data.data;
}

/** Approve a follow request by its ID. */
export async function approveRequest(requestId: string): Promise<Follow> {
    const { data } = await api.post(`${BASE}/follow-requests/${requestId}/approve`);
    return data.data;
}

/** Decline a follow request by its ID. */
export async function declineRequest(requestId: string): Promise<void> {
    await api.delete(`${BASE}/follow-requests/${requestId}/decline`);
}