import React, { useState, useCallback } from 'react';
import Cropper, { Area } from 'react-easy-crop';
import { Box, Button, Stack } from '@mui/material';

interface MediaCropEditorProps {
    imageSrc: string;
    onSave: (blob: Blob) => void;
    onCancel: () => void;
}

const getCroppedImg = async (imageSrc: string, crop: Area): Promise<Blob> => {
    const image = new Image();
    image.src = imageSrc;
    await new Promise((resolve) => (image.onload = resolve));

    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');

    if (!ctx) throw new Error('No 2d context');

    canvas.width = crop.width;
    canvas.height = crop.height;

    ctx.drawImage(
        image,
        crop.x,
        crop.y,
        crop.width,
        crop.height,
        0,
        0,
        crop.width,
        crop.height
    );

    return new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
            if (blob) resolve(blob);
            else reject(new Error('Canvas is empty'));
        }, 'image/jpeg');
    });
};

export const MediaCropEditor: React.FC<MediaCropEditorProps> = ({ imageSrc, onSave, onCancel }) => {
    const [crop, setCrop] = useState({ x: 0, y: 0 });
    const [zoom, setZoom] = useState(1);
    const [croppedAreaPixels, setCroppedAreaPixels] = useState<Area | null>(null);

    const onCropComplete = useCallback((_croppedArea: Area, croppedAreaPixels: Area) => {
        setCroppedAreaPixels(croppedAreaPixels);
    }, []);

    const handleSave = async () => {
        if (croppedAreaPixels) {
            try {
                const croppedBlob = await getCroppedImg(imageSrc, croppedAreaPixels);
                onSave(croppedBlob);
            } catch (e) {
                console.error('Error cropping image:', e);
            }
        }
    };

    return (
        <Box sx={{ width: '100%', height: '100%', minHeight: 400, position: 'relative', display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ position: 'relative', flex: 1, backgroundColor: '#333' }}>
                <Cropper
                    image={imageSrc}
                    crop={crop}
                    zoom={zoom}
                    aspect={1}
                    onCropChange={setCrop}
                    onZoomChange={setZoom}
                    onCropComplete={onCropComplete}
                />
            </Box>
            <Stack direction="row" justifyContent="flex-end" spacing={2} sx={{ p: 2, bgcolor: 'background.paper' }}>
                <Button variant="outlined" onClick={onCancel}>Cancel</Button>
                <Button variant="contained" onClick={handleSave}>Save Crop</Button>
            </Stack>
        </Box>
    );
};