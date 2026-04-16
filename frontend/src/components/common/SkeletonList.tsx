import { Stack } from "@mui/material";
import { SkeletonCard } from "./SkeletonCard";

interface SkeletonListProps {
    count?: number;
}

export function SkeletonList({ count = 3 }: SkeletonListProps) {
    return (
        <Stack spacing={2}>
            {Array.from({ length: count }).map((_, i) => (
                <SkeletonCard key={i} />
            ))}
        </Stack>
    );
}