import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { approveRequest, declineRequest, getFollowRequests } from "../../api/followApi";
import { followKeys } from "./queryKeys";

export function useFollowRequests() {
    const queryClient = useQueryClient();

    const query = useQuery({
        queryKey: followKeys.requests,
        queryFn: getFollowRequests,
    });

    const approveMutation = useMutation({
        mutationFn: approveRequest,
    });

    const declineMutation = useMutation({
        mutationFn: declineRequest,
        onSuccess: () => queryClient.invalidateQueries({ queryKey: followKeys.requests }),
    });

    return { ...query, approveMutation, declineMutation };
}
