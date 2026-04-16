import { Card, CardContent, CardActions, Skeleton, Stack, Box } from "@mui/material";

export function SkeletonCard() {
    return (
        <Card sx={{ width: '100%' }}>
            <CardContent>
                <div style={{ display: 'flex', flexDirection: 'row' }}>
                    <Skeleton variant="circular" width={40} height={40} />
                    <div style={{ display: 'flex', flexDirection: 'column', marginLeft: 10 }}>
                        <Skeleton variant="text" width={200} />
                        <Skeleton variant="text" width={200} />
                    </div>
                </div>
                <Skeleton variant="rectangular" height={300} width="100%" />
                <Skeleton variant="text" width="40%" />
                <Skeleton variant="text" width="40%" />
            </CardContent>
        </Card>
    )
}