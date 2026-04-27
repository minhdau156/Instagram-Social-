import { Box, TextField, Typography, useTheme, Paper, List, ListItem, ListItemButton, ListItemText } from "@mui/material";
import { useState, useEffect } from "react";
import axios from "axios";

const MAX_CHARS = 2200;
const HASHTAG_REGEX = /#(\w+)/g;

interface CaptionEditorProps {
    value: string;
    onChange: (value: string) => void;
}

export const CaptionEditor: React.FC<CaptionEditorProps> = ({ value, onChange }) => {
    const remaining = MAX_CHARS - value.length;
    const theme = useTheme();
    const [mentionSearch, setMentionSearch] = useState<string | null>(null);
    const [users, setUsers] = useState<any[]>([]);

    useEffect(() => {
        const match = value.match(/@(\w+)$/);
        if (match) {
            setMentionSearch(match[1]);
            // Call search API for users
            axios.get(`/api/v1/search?q=${match[1]}&type=users`)
                .then(res => {
                    const data = res.data?.data || res.data?.content || res.data || [];
                    setUsers(Array.isArray(data) ? data : []);
                })
                .catch(() => setUsers([]));
        } else {
            setMentionSearch(null);
            setUsers([]);
        }
    }, [value]);

    const handleMentionSelect = (username: string) => {
        const newValue = value.replace(/@(\w+)$/, `@${username} `);
        onChange(newValue);
        setMentionSearch(null);
    };

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
            {mentionSearch && users.length > 0 && (
                <Paper sx={{ position: 'absolute', bottom: '100%', left: 0, zIndex: 10, minWidth: 250, maxHeight: 200, overflow: 'auto', mb: 1 }}>
                    <List dense>
                        {users.map((u: any, idx: number) => (
                            <ListItem disablePadding key={u.id || u.username || idx}>
                                <ListItemButton onClick={() => handleMentionSelect(u.username)}>
                                    <ListItemText primary={`@${u.username}`} secondary={u.fullName} />
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Paper>
            )}
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