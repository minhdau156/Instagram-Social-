export enum FollowStatus {
    PENDING = 'PENDING',
    ACCEPTED = 'ACCEPTED',
}

export interface Follow {
    followerId: string;
    followingId: string;
    status: FollowStatus;
    createdAt: string;
}

export interface FollowRequest extends Follow {
    follower?: UserSummary;
}

export interface UserSummary {
    id: string;
    username: string;
    fullName: string;
    profilePictureUrl: string | null;
    isVerified: boolean;
    isFollowing: boolean;
}

export interface UserSummaryPage {
    content: UserSummary[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    last: boolean;
}