import { Avatar, ListItem, ListItemText } from "@mui/material";
import { FollowStatus, UserSummary } from "../../types/follow";
import { FollowButton } from "./FollowButton";


interface UserListItemProps {
    user: UserSummary;
    currentUsername?: string;
}

export const UserListItem = ({ user, currentUsername }: UserListItemProps) => {
    return (
        <ListItem >
            <Avatar src={user.profilePictureUrl || undefined} alt={user.username} />
            <ListItemText primary={user.username} secondary={user.fullName} />
            {
                user.username !== currentUsername && <FollowButton username={user.username} initialStatus={user.isFollowing ? FollowStatus.ACCEPTED : null} />
            }
        </ListItem>
    )
}