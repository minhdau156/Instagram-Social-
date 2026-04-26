import { useQuery } from '@tanstack/react-query';
import { postApi } from '../../api/postApi';

export const usePost = (postId: string) => {
    return useQuery({
        queryKey: ['post', postId],
        queryFn: () => postApi.getPostById(postId),
        enabled: !!postId,
    });
};
