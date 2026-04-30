import { useInfiniteQuery } from "@tanstack/react-query";
import { getFollowing } from "../../api/followApi";
import { followKeys } from "./queryKeys";

export function useFollowing(username: string) {
    return useInfiniteQuery({
        queryKey: followKeys.following(username),
        queryFn: ({ pageParam = 0 }) => getFollowing(username, pageParam),
        getNextPageParam: (lastPage, allPages) =>
            lastPage.length < 20 ? undefined : allPages.length,
        initialPageParam: 0,
    });
}