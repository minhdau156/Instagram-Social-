import { useState } from "react";
import { Alert, Container, Stack, Typography, TextField, Button, CircularProgress } from "@mui/material";
import { authApi } from "../../api/authApi";

export const ForgotPasswordPage = () => {
    const [email, setEmail] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setIsLoading(true);
            await authApi.requestPasswordReset({ email });
            <Alert severity="success">If that email exists, a reset link has been sent.</Alert>
        } catch (error: any) {
            <Alert severity="error">{error.message}</Alert>
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Container maxWidth={"xs"}>
            <Stack component={"form"} onSubmit={onSubmit} style={{ marginTop: "2rem" }} spacing={3}>
                <Typography variant="h4" align="center">Forgot Password Page</Typography>
                <TextField
                    label="Email"
                    variant="outlined"
                    required
                    fullWidth
                    margin="normal"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <Button
                    variant="contained"
                    fullWidth
                    type="submit"
                    disabled={isLoading}
                >
                    {isLoading ? <CircularProgress color="inherit" size={"25px"} /> : "Submit"}
                </Button>
            </Stack>

            <Stack style={{ marginTop: "1rem" }} justifyContent={"center"} alignItems={"center"}>
                <Typography variant="body2" align="center">Already have an account? <a href="/login">Back to Login</a></Typography>
            </Stack>

        </Container>
    );
};