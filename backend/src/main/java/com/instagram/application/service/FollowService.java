package com.instagram.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.instagram.domain.exception.AlreadyFollowingException;
import com.instagram.domain.exception.CannotFollowYourselfException;
import com.instagram.domain.exception.FollowRequestNotFoundException;
import com.instagram.domain.exception.UserNotFoundException;
import com.instagram.domain.model.Follow;
import com.instagram.domain.model.FollowStatus;
import com.instagram.domain.model.PrivacyLevel;
import com.instagram.domain.model.User;
import com.instagram.domain.model.UserSummary;
import com.instagram.domain.port.in.follow.*;
import com.instagram.domain.port.out.FollowRepository;
import com.instagram.domain.port.out.UserRepository;
import com.instagram.domain.port.out.UserStatsRepository;

@Service
public class FollowService implements FollowUserUseCase,
        UnfollowUserUseCase,
        ApproveFollowRequestUseCase,
        DeclineFollowRequestUseCase,
        GetFollowRequestsUseCase,
        GetFollowersUseCase,
        GetFollowingUseCase {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;

    public FollowService(FollowRepository followRepository,
            UserRepository userRepository,
            UserStatsRepository userStatsRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
    }

    @Override
    public Follow follow(FollowUserUseCase.Command command) {
        User targetUser = userRepository.findByUsername(command.targetUsername())
                .orElseThrow(() -> new UserNotFoundException(command.targetUsername()));

        if (command.followerId().equals(targetUser.getId())) {
            throw new CannotFollowYourselfException();
        }

        if (followRepository.findByFollowerIdAndFollowingId(command.followerId(), targetUser.getId()).isPresent()) {
            throw new AlreadyFollowingException(targetUser.getUsername());
        }

        FollowStatus status = targetUser.getPrivacyLevel() == PrivacyLevel.PRIVATE ? FollowStatus.PENDING
                : FollowStatus.ACCEPTED;

        Follow follow = Follow.of(command.followerId(), targetUser.getId(), status);
        Follow saved = followRepository.save(follow);

        if (status == FollowStatus.ACCEPTED) {
            userStatsRepository.incrementFollowerCount(targetUser.getId());
            userStatsRepository.incrementFollowingCount(command.followerId());
        }

        return saved;
    }

    @Override
    public void unfollow(UnfollowUserUseCase.Command command) {
        User targetUser = userRepository.findByUsername(command.targetUsername())
                .orElseThrow(() -> new UserNotFoundException(command.targetUsername()));
        Follow follow = followRepository.findByFollowerIdAndFollowingId(command.followerId(), targetUser.getId())
                .orElseThrow(() -> new FollowRequestNotFoundException(command.followerId()));
        followRepository.delete(command.followerId(), targetUser.getId());
        if (follow.getStatus() == FollowStatus.ACCEPTED) {
            userStatsRepository.decrementFollowerCount(targetUser.getId());
            userStatsRepository.decrementFollowingCount(command.followerId());
        }

    }

    @Override
    public Follow approve(ApproveFollowRequestUseCase.Command command) {
        Follow follow = followRepository
                .findByFollowerIdAndFollowingId(command.followRequestId(), command.followingId())
                .orElseThrow(() -> new FollowRequestNotFoundException(command.followRequestId()));
        Follow acceptedFollow = follow.withAccepted();
        followRepository.save(acceptedFollow);
        userStatsRepository.incrementFollowerCount(command.followingId());
        userStatsRepository.incrementFollowingCount(command.followRequestId());
        return acceptedFollow;
    }

    @Override
    public void decline(DeclineFollowRequestUseCase.Command command) {
        Follow follow = followRepository
                .findByFollowerIdAndFollowingId(command.followRequestId(), command.followingId())
                .orElseThrow(() -> new FollowRequestNotFoundException(command.followRequestId()));
        followRepository.delete(command.followRequestId(), command.followingId());
    }

    @Override
    public List<Follow> getFollowRequests(GetFollowRequestsUseCase.Query query) {
        return followRepository.findPendingRequestsByFollowingId(query.userId());
    }

    @Override
    public List<UserSummary> getFollowers(GetFollowersUseCase.Query query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        List<Follow> follows = followRepository.findFollowersByUserId(query.currentUserId(), pageable);
        return follows.stream().map(follow -> {
            User user = userRepository.findById(follow.getFollowerId()).orElseThrow();
            boolean isFollowing = followRepository.findByFollowerIdAndFollowingId(query.currentUserId(), user.getId())
                    .isPresent();
            return new UserSummary(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getProfilePictureUrl(),
                    user.isVerified(),
                    isFollowing);
        }).toList();
    }

    @Override
    public List<UserSummary> getFollowing(GetFollowingUseCase.Query query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        List<Follow> follows = followRepository.findFollowingByUserId(query.currentUserId(), pageable);
        return follows.stream().map(follow -> {
            User user = userRepository.findById(follow.getFollowingId()).orElseThrow();
            return new UserSummary(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getProfilePictureUrl(),
                    user.isVerified(),
                    true);
        }).toList();
    }

}
