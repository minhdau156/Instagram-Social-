import { UploadUrlResponse } from '../types/post';
import { api } from './client';

export const mediaApi = {
    getUploadUrl: async (filename: string, contentType: string): Promise<UploadUrlResponse> => {
        const { data } = await api.post('/api/v1/media/upload-url', { filename, contentType });
        return data.data;
    },

    // Direct PUT to MinIO — no auth headers needed, use plain fetch/axios
    uploadToMinio: async (presignedUrl: string, file: File): Promise<void> => {
        await fetch(presignedUrl, { method: 'PUT', body: file, headers: { 'Content-Type': file.type } });
    },
}