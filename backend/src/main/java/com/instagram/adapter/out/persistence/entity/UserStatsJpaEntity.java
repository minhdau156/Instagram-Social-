package com.instagram.adapter.out.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_stats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsJpaEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "follower_count")
    private int followerCount;

    @Column(name = "following_count")
    private int followingCount;

    @Column(name = "post_count")
    private int postCount;

}
