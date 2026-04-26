import { Box, Button, Stack, Typography } from "@mui/material";
import React, { useState } from "react";

interface MediaPickerProps {
    onFilesSelected: (files: File[]) => void;
    maxFiles?: number;
}

export const MediaPicker: React.FC<MediaPickerProps> = ({ onFilesSelected, maxFiles = 10 }) => {
    const [previews, setPreviews] = useState<string[]>([]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = Array.from(e.target.files ?? []).slice(0, maxFiles);
        setPreviews(files.map(f => URL.createObjectURL(f)));
        onFilesSelected(files);
    };

    return (
        <Box
            onDragOver={(e) => e.preventDefault()}
            onDrop={(e) => { e.preventDefault(); handleChange({ target: e.dataTransfer } as any); }}
            sx={{ border: '2px dashed grey', borderRadius: 2, p: 3, textAlign: 'center' }}
        >
            <Button component="label" variant="contained">
                Select Photos/Videos
                <input hidden type="file" multiple accept="image/*,video/*" onChange={handleChange} />
            </Button>
            <Stack direction="row" flexWrap="wrap" gap={1} mt={2}>
                {previews.map((src, i) => (
                    <Box key={i} component="img" src={src}
                        sx={{ width: 80, height: 80, objectFit: 'cover', borderRadius: 1 }} />
                ))}
            </Stack>
        </Box>
    );
};