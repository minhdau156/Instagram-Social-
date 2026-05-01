import { useFollowRequests } from "../../hooks/follow/useFollowRequests";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import List from "@mui/material/List";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import CircularProgress from "@mui/material/CircularProgress";
import PersonAddDisabledOutlined from "@mui/icons-material/PersonAddDisabledOutlined";

import { Alert, Avatar, Link, ListItem, ListItemAvatar, ListItemText } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { useEffect, useState } from "react";

export default function FollowRequestsPage() {
    const { data: requests, error, isLoading, approveMutation, declineMutation } = useFollowRequests();
    const [actionStatus, setActionStatus] = useState<Record<string, 'ACCEPTED'>>({});

    const handleApprove = (id: string) => {
        approveMutation.mutate(id, {
            onSuccess: () => {
                setActionStatus(prev => ({ ...prev, [id]: 'ACCEPTED' }));
            }
        });
    }

    useEffect(() => {
        document.title = "Follow Requests | Instagram";
    }, []);

    if (error) {
        return (
            <Container maxWidth="sm" sx={{ py: 2 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Follow Requests
                </Typography>
                <Alert severity="error">
                    {error instanceof Error ? error.message : "Failed to fetch follow requests"}
                </Alert>
            </Container>
        );
    }
    return (
        <Container maxWidth="sm" sx={{ py: 2 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Follow Requests
            </Typography>
            {isLoading ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
                    <CircularProgress />
                </Box>
            ) : requests && requests.length > 0 ? (
                <List>
                    {requests.map((request) => (
                        <ListItem key={request.id}>
                            <ListItemAvatar>
                                <Avatar src={request.profilePictureUrl || undefined} alt={request.username} />
                            </ListItemAvatar>
                            <ListItemText
                                primary={
                                    <Link component={RouterLink} to={`/${request.username}`} underline="none" color="inherit" fontWeight="bold">
                                        {request.username}
                                    </Link>
                                }
                                secondary={request.fullName || undefined}
                            />
                            <Box sx={{ display: 'flex', gap: 1 }}>
                                {actionStatus[request.id] === 'ACCEPTED' ? (
                                    <Button variant="outlined" color="primary" disabled>Accepted</Button>
                                ) : (
                                    <>
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={() => handleApprove(request.id)}
                                            disabled={approveMutation.isPending && approveMutation.variables === request.id}
                                        >
                                            {(approveMutation.isPending && approveMutation.variables === request.id) ? <CircularProgress size={20} /> : "Accept"}
                                        </Button>
                                        <Button
                                            variant="contained"
                                            color="error"
                                            onClick={() => declineMutation.mutate(request.id)}
                                            disabled={declineMutation.isPending && declineMutation.variables === request.id}
                                        >
                                            {(declineMutation.isPending && declineMutation.variables === request.id) ? <CircularProgress size={20} /> : "Decline"}
                                        </Button>
                                    </>
                                )}
                            </Box>
                        </ListItem>
                    ))}
                </List>
            ) : (
                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', py: 4, gap: 2 }}>
                    <PersonAddDisabledOutlined sx={{ fontSize: 60, color: 'text.secondary' }} />
                    <Typography variant="h6" color="text.secondary">
                        No pending follow requests
                    </Typography>
                </Box>
            )}
        </Container>
    );
}