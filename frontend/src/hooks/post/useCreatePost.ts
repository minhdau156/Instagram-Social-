import { useMutation, useQueryClient } from "@tanstack/react-query";
import { MediaType } from "../../types/post";
import { mediaApi } from "../../api/mediaApi";
import { postApi } from "../../api/postApi";


export const useCreatePost = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (data: {
            payload: { location?: string; caption?: string };
            file: File
        }) => {
            //get the pre-signed url from the backend
            const { presignedUrl, mediaKey } = await mediaApi.getUploadUrl(data.file.name,
                data.file.type);
            //upload the file to minio
            await mediaApi.uploadToMinio(presignedUrl, data.file);
            //create the post
            return postApi.createPost({
                ...data.payload,
                mediaItems: [{
                    mediaKey,
                    mediaType: data.file.type.startsWith('video/') ? 'VIDEO' : 'IMAGE' as MediaType,
                    sortOrder: "0"
                }]
            });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['posts'] });
        }
    })
}   