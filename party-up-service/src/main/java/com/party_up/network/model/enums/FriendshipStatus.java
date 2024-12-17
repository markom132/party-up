package com.party_up.network.model.enums;

/**
 * Enum representing the status of friendship.
 * The status can be PENDING, ACCEPTED, REJECTED.
 */
public enum FriendshipStatus {

    /**
     * Indicates that friend request is neither accepted nor rejected.
     */
    PENDING,

    /**
     * Indicates that friend request is accepted.
     */
    ACCEPTED,

    /**
     * Indicates that friend request is rejected.
     */
    REJECTED
}
