import { Avatar, ListItem, ListItemAvatar, ListItemText, Link } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { FollowStatus, UserSummary } from "../../types/follow";
import { FollowButton } from "./FollowButton";


interface UserListItemProps {
    user: UserSummary;
    currentUsername?: string;
}

export const UserListItem = ({ user, currentUsername }: UserListItemProps) => {
    return (
        <ListItem >
            <ListItemAvatar>
                <Link component={RouterLink} to={`/${user.username}`} underline="none">
                    <Avatar src={user.profilePictureUrl || undefined} alt={user.username} />
                </Link>
            </ListItemAvatar>
            <ListItemText 
                primary={
                    <Link component={RouterLink} to={`/${user.username}`} underline="none" color="inherit" fontWeight="bold">
                        {user.username}
                    </Link>
                } 
                secondary={user.fullName} 
            />
            {
                user.username !== currentUsername && <FollowButton username={user.username} initialStatus={user.isFollowing ? FollowStatus.ACCEPTED : null} />
            }
        </ListItem>
    )
}