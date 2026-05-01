import { useState } from "react";

import { useFollow } from "../../hooks/follow/useFollow";
import { FollowStatus } from "../../types/follow";
import { Button, CircularProgress } from "@mui/material";

interface FollowButtonProps {
    username: string;
    initialStatus: FollowStatus | null;
}

export function FollowButton({ username, initialStatus }: FollowButtonProps) {

    const { followMutation, unfollowMutation } = useFollow(username);
    const [hovered, setHovered] = useState(false);



    return (
        <>
            {initialStatus === null ?
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => followMutation.mutate()}
                    disabled={followMutation.isPending}
                >
                    {followMutation.isPending ? (
                        <CircularProgress size={20} />
                    ) : (
                        "Follow"
                    )}
                </Button> :
                initialStatus === "PENDING" ?
                    <Button
                        variant="outlined"
                        color="secondary"
                        onClick={() => unfollowMutation.mutate()}
                        disabled={unfollowMutation.isPending}
                    >
                        {unfollowMutation.isPending ? (
                            <CircularProgress size={20} />
                        ) : (
                            "Requested"
                        )}
                    </Button> :
                    initialStatus === "ACCEPTED" ?
                        <Button
                            variant="outlined"
                            color="primary"
                            onClick={() => unfollowMutation.mutate()}
                            disabled={unfollowMutation.isPending}
                            onMouseEnter={() => setHovered(true)}
                            onMouseLeave={() => setHovered(false)}
                        >
                            {unfollowMutation.isPending ? (
                                <CircularProgress size={20} />
                            ) : (
                                hovered === true ? "Unfollow" : "Following"
                            )}
                        </Button> : null
            }
        </>
    )


}