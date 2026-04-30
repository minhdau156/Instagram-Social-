import { useInfiniteQuery } from "@tanstack/react-query";
import { getFollowers } from "../../api/followApi";
import { followKeys } from "./queryKeys";

export function useFollowers(username: string) {
    return useInfiniteQuery({
        queryKey: followKeys.followers(username),
        queryFn: ({ pageParam = 0 }) => getFollowers(username, pageParam),
        getNextPageParam: (lastPage, allPages) =>
            lastPage.length < 20 ? undefined : allPages.length,
        initialPageParam: 0,
    });
}