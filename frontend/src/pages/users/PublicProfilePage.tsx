import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { usersApi } from "../../api/usersApi";
import { Container, Alert, Avatar, Box, Button, Stack, Typography } from "@mui/material";
import { PageLoader } from "../../components/common/PageLoader";

export const PublicProfilePage = () => {
    const { username } = useParams();
    const { data, isLoading, error } = useQuery({
        queryKey: ["users", username],
        queryFn: () => usersApi.getUserByUsername(username!),
    });
    return (
        <Container maxWidth={"md"}>
            {isLoading ? (
                <PageLoader />
            ) : error ? (
                <Alert severity="error">{error.message}</Alert>
            ) : (
                <>
                    <Container maxWidth={"sm"} style={{ marginTop: "2rem" }}>
                        <Stack direction="row" style={{ marginTop: "2rem" }} spacing={4}>
                            <Avatar src={data?.user?.avatarUrl ?? undefined} sx={{ width: 150, height: 150 }} />

                            <Stack spacing={1}>
                                <Typography variant="h6" >{data?.user?.username}</Typography>
                                <Typography variant="body1" >{data?.user?.fullName}</Typography>
                                <Stack direction="row" spacing={2}>
                                    <Typography variant="body2" >{data?.postCount ?? 0} posts</Typography>
                                    <Typography variant="body2">{data?.followerCount ?? 0} followers</Typography>
                                    <Typography variant="body2">{data?.followingCount ?? 0} following</Typography>
                                </Stack>

                                <Button disabled>
                                    Follow
                                </Button>
                            </Stack>

                        </Stack>
                    </Container>

                    <Box style={{ marginTop: "5rem", borderTop: "1px solid #e5e5e5" }}>
                        No post yet.
                    </Box>
                </>
            )}
        </Container>

    );
};