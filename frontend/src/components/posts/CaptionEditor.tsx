import { Box, TextField, Typography, useTheme } from "@mui/material";

const MAX_CHARS = 2200;
const HASHTAG_REGEX = /#(\w+)/g;

interface CaptionEditorProps {
    value: string;
    onChange: (value: string) => void;
}

export const CaptionEditor: React.FC<CaptionEditorProps> = ({ value, onChange }) => {
    const remaining = MAX_CHARS - value.length;
    const theme = useTheme();

    // Escape HTML first to prevent XSS
    const escapeHtml = (text: string) => {
        return text.replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    };

    const escapedValue = escapeHtml(value);

    // Highlight hashtags in a read-only overlay (positioned over the textarea)
    const highlighted = escapedValue.replace(
        HASHTAG_REGEX,
        '<strong style="color:#1976d2">#$1</strong>'
    ) + (value.endsWith('\n') ? '<br>' : '');

    return (
        <Box sx={{ position: 'relative' }}>
            {value && (
                <Box
                    sx={{
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        padding: '16.5px 14px', // Match MUI default OutlinedInput padding
                        pointerEvents: 'none',
                        overflow: 'hidden',
                        whiteSpace: 'pre-wrap',
                        wordBreak: 'break-word',
                        color: 'text.primary',
                        fontFamily: 'inherit',
                        fontSize: '1rem',
                        lineHeight: '1.5em',
                        zIndex: 1,
                    }}
                    dangerouslySetInnerHTML={{ __html: highlighted }}
                />
            )}
            <TextField
                multiline
                rows={5}
                fullWidth
                value={value}
                onChange={(e) => onChange(e.target.value.slice(0, MAX_CHARS))}
                placeholder="Write a caption… use #hashtags and @mentions"
                inputProps={{ maxLength: MAX_CHARS }}
                sx={{
                    '& .MuiInputBase-root': {
                        alignItems: 'flex-start',
                    },
                    '& .MuiInputBase-input': {
                        color: value ? 'transparent' : 'inherit',
                        caretColor: theme.palette.text.primary,
                        fontSize: '1rem',
                        lineHeight: '1.5em',
                        zIndex: 2,
                    },
                    '& .MuiInputBase-input::placeholder': {
                        color: theme.palette.text.secondary,
                        opacity: 1,
                    }
                }}
            />
            <Typography
                variant="caption"
                color={remaining < 50 ? 'error' : 'text.secondary'}
                sx={{ position: 'absolute', bottom: 8, right: 12 }}
            >
                {remaining}
            </Typography>
        </Box>
    );
};
