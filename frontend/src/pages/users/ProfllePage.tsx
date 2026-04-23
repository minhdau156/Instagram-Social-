import { Box, Button, CircularProgress, Container, Dialog, DialogActions, DialogContent, DialogTitle, Grid, Stack, TextField, Typography } from "@mui/material";
import { useAuth } from "../../hooks/useAuth";
import Avatar from "@mui/material/Avatar";
import { PageLoader } from "../../components/common/PageLoader";
import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { authApi } from "../../api/authApi";
import { usersApi } from "../../api/usersApi";

export const ProfilePage = () => {

    const { user, isLoading } = useAuth();

    const [open, setOpen] = useState(false);
    const { control, handleSubmit, formState: { errors } } = useForm({
        defaultValues: {
            fullName: user?.fullName || "",
            bio: user?.bio || "",
            isPrivate: user?.isPrivate || false,
        }
    });

    const onSubmit = async (data: any) => {
        try {
            usersApi.updateMe({
                fullName: data.fullName,
                bio: data.bio || null,
                isPrivate: data.isPrivate || false
            });
            setOpen(false);
        } catch (error) {
            console.error("Update profile failed:", error);
        }
    };

    if (isLoading) {
        return <PageLoader />;
    }

    return (
        <>
            <Container maxWidth={"md"}>
                <Container maxWidth={"sm"} style={{ marginTop: "2rem" }}>
                    <Stack direction="row" style={{ marginTop: "2rem" }} spacing={4}>
                        <Avatar src={user?.avatarUrl ?? undefined} sx={{ width: 150, height: 150 }} />

                        <Stack spacing={1}>
                            <Typography variant="h6" >{user?.username}</Typography>
                            <Typography variant="body1" >{user?.fullName}</Typography>
                            <Stack direction="row" spacing={2}>
                                <Typography variant="body2" >0 posts</Typography>
                                <Typography variant="body2">0 followers</Typography>
                                <Typography variant="body2">0 following</Typography>
                            </Stack>

                            <Button size="small" variant="outlined" onClick={() => setOpen(true)}>
                                Edit Profile
                            </Button>
                        </Stack>

                    </Stack>
                </Container>

                <Box style={{ marginTop: "5rem", borderTop: "1px solid #e5e5e5" }}>
                    No post yet.
                </Box>

            </Container >

            <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm">
                <DialogTitle>Edit Profile</DialogTitle>
                <DialogContent>
                    <Stack component={"form"} id="edit-profile-form" onSubmit={handleSubmit(onSubmit)} style={{ marginTop: "1rem" }} spacing={3}>
                        <Controller
                            name="fullName"
                            control={control}
                            rules={{ required: "Email or username is required" }}
                            render={({ field }) => (
                                <TextField
                                    {...field}
                                    label="Full Name"
                                    variant="outlined"
                                    fullWidth
                                    error={!!errors.fullName}
                                    helperText={errors.fullName?.message}
                                />
                            )}
                        />
                        <Controller
                            name="bio"
                            control={control}
                            rules={{ required: "Bio is required" }}
                            render={({ field }) => (
                                <TextField
                                    {...field}
                                    label="Bio"
                                    variant="outlined"
                                    type="text"
                                    fullWidth
                                    multiline
                                    rows={4}
                                    error={!!errors.bio}
                                    helperText={errors.bio?.message}
                                />
                            )}
                        />
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => { setOpen(false) }} disabled={isLoading}>Cancel</Button>
                    <Button type="submit" form="edit-profile-form" disabled={isLoading}>
                        {isLoading ? <CircularProgress color="inherit" size="20px" /> : "Save"}
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
};