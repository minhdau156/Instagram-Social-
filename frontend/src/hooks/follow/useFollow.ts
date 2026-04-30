import { useMutation, useQueryClient } from "@tanstack/react-query";
import { followUser, unfollowUser } from "../../api/followApi";
import type { UserProfile } from "../../types/user";
import { followKeys } from "./queryKeys";

export const useFollow = (username: string) => {
    const queryClient = useQueryClient();
    const profileKey = followKeys.profile(username);

    const followMutation = useMutation({
        mutationFn: () => followUser(username),
        onMutate: async () => {
            await queryClient.cancelQueries({ queryKey: profileKey });
            const snapshot = queryClient.getQueryData<UserProfile>(profileKey);

            // Apply optimistic update to profile data
            if (snapshot) {
                queryClient.setQueryData<UserProfile>(profileKey, {
                    ...snapshot,
                    isFollowing: true,
                    followerCount: snapshot.followerCount + 1,
                });
            }

            return { snapshot };
        },
        onError: (_err, _vars, context) => {
            queryClient.setQueryData(profileKey, context?.snapshot);
        },
        onSettled: () => {
            queryClient.invalidateQueries({ queryKey: profileKey });
        },
    });

    const unfollowMutation = useMutation({
        mutationFn: () => unfollowUser(username),
        onMutate: async () => {
            await queryClient.cancelQueries({ queryKey: profileKey });
            const snapshot = queryClient.getQueryData<UserProfile>(profileKey);

            // Apply optimistic update to profile data
            if (snapshot) {
                queryClient.setQueryData<UserProfile>(profileKey, {
                    ...snapshot,
                    isFollowing: false,
                    followerCount: Math.max(0, snapshot.followerCount - 1),
                });
            }

            return { snapshot };
        },
        onError: (_err, _vars, context) => {
            queryClient.setQueryData(profileKey, context?.snapshot);
        },
        onSettled: () => {
            queryClient.invalidateQueries({ queryKey: profileKey });
        },
    });

    return { followMutation, unfollowMutation };
};