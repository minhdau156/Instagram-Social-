import { useState } from "react";
import { Alert, Button, CircularProgress, Container, Stack, TextField, Typography } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { useAuth } from "../../hooks/useAuth";
import { useNavigate } from "react-router-dom";

export const RegisterPage = () => {
    const [apiError, setApiError] = useState("");
    const { control, handleSubmit, watch, formState: { errors } } = useForm({
        defaultValues: {
            email: "",
            phone: "",
            username: "",
            password: "",
            confirmPassword: "",
            fullName: ""
        },
        mode: "onChange"
    });
    const { isLoading, register } = useAuth();
    const navigate = useNavigate();

    const password = watch("password");

    const onSubmit = async (data: any) => {
        try {
            setApiError("");
            await register({
                username: data.username,
                email: data.email,
                password: data.password,
                fullName: data.fullName,
            });
            console.log("Register success");
            navigate("/");
        } catch (error: any) {
            setApiError(error.message || "Registration failed");
            console.log(error);
        }
    };

    return (
        <Container maxWidth={"xs"} style={{ paddingBottom: "2rem" }}>
            <Typography variant="h4" align="center" style={{ marginTop: "2rem" }}>Register Page</Typography>
            {apiError && (
                <Alert severity="error" style={{ marginTop: "1rem" }}>
                    {apiError}
                </Alert>
            )}
            <Stack component={"form"} onSubmit={handleSubmit(onSubmit)} style={{ marginTop: "2rem" }} spacing={3}>
                <Controller
                    name="email"
                    control={control}
                    rules={{
                        required: "Email is required",
                        pattern: { value: /\S+@\S+\.\S+/, message: "Email is invalid" }
                    }}
                    render={({ field }) => (
                        <TextField
                            {...field}
                            label="Email"
                            variant="outlined"
                            type="email"
                            fullWidth
                            error={!!errors.email}
                            helperText={errors.email?.message}
                        />
                    )}
                />
                <Controller
                    name="phone"
                    control={control}
                    rules={{
                        required: "Phone number is required",
                        minLength: { value: 10, message: "Phone number must be at least 10 characters" },
                        pattern: { value: /^[0-9]+$/, message: "Phone number must contain only numbers" }
                    }}
                    render={({ field }) => (
                        <TextField
                            {...field}
                            label="Phone Number"
                            variant="outlined"
                            fullWidth
                            error={!!errors.phone}
                            helperText={errors.phone?.message}
                        />
                    )}
                />
                <Controller
                    name="username"
                    control={control}
                    rules={{
                        required: "Username is required",
                        minLength: { value: 3, message: "Username must be at least 3 characters" },
                        maxLength: { value: 30, message: "Username must be at most 30 characters" }
                    }}
                    render={({ field }) => (
                        <TextField
                            {...field}
                            label="Username"
                            variant="outlined"
                            fullWidth
                            error={!!errors.username}
                            helperText={errors.username?.message}
                        />
                    )}
                />
                <Controller
                    name="fullName"
                    control={control}
                    rules={{ 
                        required: "Full name is required",
                        pattern: { value: /^[a-zA-Z\s]+$/, message: "Full name can only contain letters and spaces" }
                    }}
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
                    name="password"
                    control={control}
                    rules={{
                        required: "Password is required",
                        minLength: { value: 8, message: "Password must be at least 8 characters" }
                    }}
                    render={({ field }) => (
                        <TextField
                            {...field}
                            label="Password"
                            variant="outlined"
                            type="password"
                            fullWidth
                            error={!!errors.password}
                            helperText={errors.password?.message}
                        />
                    )}
                />
                <Controller
                    name="confirmPassword"
                    control={control}
                    rules={{
                        required: "Confirm password is required",
                        validate: value => value === password || "Passwords do not match"
                    }}
                    render={({ field }) => (
                        <TextField
                            {...field}
                            label="Confirm Password"
                            variant="outlined"
                            type="password"
                            fullWidth
                            error={!!errors.confirmPassword}
                            helperText={errors.confirmPassword?.message}
                        />
                    )}
                />
                <Button
                    variant="contained"
                    fullWidth
                    type="submit"
                    disabled={isLoading}
                >
                    {isLoading ? <CircularProgress color="inherit" size={"25px"} /> : "Register"}
                </Button>
            </Stack>
            <Stack style={{ marginTop: "1rem" }} justifyContent={"center"} alignItems={"center"}>
                <Typography variant="body2" align="center">Already have an account? <a href="/login">Back to Login</a></Typography>
            </Stack>
        </Container>
    );
};