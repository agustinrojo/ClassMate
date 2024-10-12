package com.classmate.forum_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Entity class representing a forum.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forums")
public class Forum {

    /**
     * The unique identifier of the forum.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The title of the forum.
     */
    @Column(nullable = false)
    private String title;

    /**
     * The unique identifier of the creator of the forum.
     */
    @ManyToOne
    @JoinColumn(name = "creatorId", nullable = false)
    private User creator;

    /**
     * The description of the forum.
     */
    @Column(nullable = false, length = 300)
    private String description;

    /**
     * The list of member IDs associated with the forum.
     */
    @ManyToMany
    @JoinTable(
            name = "forum_user_members",
            joinColumns = @JoinColumn(name = "forum_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    /**
     * The list of admin IDs associated with the forum.
     */
    @ManyToMany
    @JoinTable(
            name = "forum_user_admins",
            joinColumns = @JoinColumn(name = "forum_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> admins;

    /**
     * The creation date and time of the forum.
     * This field is automatically populated.
     */
    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;

    @Column
    private Boolean hasBeenEdited;
    /**
     * Checks if a user is already a member of the forum.
     *
     * @param memberId the ID of the member to check
     * @return true if the user is already a member, false otherwise
     */
    public boolean isAlreadyMember(Long memberId) {
        return members.stream()
                .anyMatch((User user) -> user.getUserId().equals(memberId));
    }

    /**
     * Adds a member to the forum.
     *
     * @param member the ID of the member to add
     */
    public void addMember(User member) {
        if (isAlreadyMember(member.getUserId())) {
            throw new IllegalArgumentException(String.format("User %d is already a member of forum %d.", member.getUserId(), id));
        }
        this.members.add(member);
    }

    /**
     * Removes a member from the forum.
     *
     * @param memberId the ID of the member to remove
     */
    public void removeMember(Long memberId) {
        if (!isAlreadyMember(memberId)) {
            throw new IllegalArgumentException(String.format("User %d is not a member of forum %d.", memberId, id));
        }
        this.members.removeIf((User member) -> member.getUserId().equals(memberId));
    }

    /**
     * Checks if a user is already an admin of the forum.
     *
     * @param adminId the ID of the admin to check
     * @return true if the user is already an admin, false otherwise
     */
    public boolean isAlreadyAdmin(Long adminId) {
        return admins.stream()
                .anyMatch((User admin) -> admin.getUserId().equals(adminId));
    }

    /**
     * Adds an admin to the forum.
     *
     * @param admin the ID of the admin to add
     */
    public void addAdmin(User admin) {
        if (isAlreadyAdmin(admin.getUserId())) {
            throw new IllegalArgumentException(String.format("User %d is already an admin of forum %d.", admin.getUserId(), id));
        }
        this.admins.add(admin);
    }

    /**
     * Removes an admin from the forum.
     *
     * @param adminId the ID of the admin to remove
     */
    public void removeAdmin(Long adminId) {
        if (!isAlreadyAdmin(adminId)) {
            throw new IllegalArgumentException(String.format("User %d is not an admin of forum %d.", adminId, id));
        }
        this.admins.removeIf((User admin) -> admin.getUserId().equals(adminId));
    }
}
