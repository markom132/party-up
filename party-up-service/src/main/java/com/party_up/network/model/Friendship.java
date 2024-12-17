package com.party_up.network.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.party_up.network.model.enums.FriendshipStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a friendship between users.
 * This class is mapped to the "friendships" table in the database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friendships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_one_id", "user_two_id"}))
public class Friendship {

    /**
     * Unique identifier for the friendships
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents first user of friendship.
     */
    @ManyToOne
    @JoinColumn(name = "user_one_id", nullable = false)
    private User userOne;

    /**
     * Represents second user of friendship.
     */
    @ManyToOne
    @JoinColumn(name = "user_two_id", nullable = false)
    private User userTwo;

    /**
     * Represents a friendship status(PENDING, ACCEPTED, REJECTED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendshipStatus status;

    /**
     * Represents when friend request is sent.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Represents when friendship is changed last time.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Friendship(User userOne, User userTwo, FriendshipStatus status) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
