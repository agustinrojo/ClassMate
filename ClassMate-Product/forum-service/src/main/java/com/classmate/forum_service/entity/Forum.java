package com.classmate.forum_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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
    @Column(nullable = false)
    private Long creatorId;

    /**
     * The description of the forum.
     */
    @Column(nullable = false)
    private String description;

    /**
     * The list of member IDs associated with the forum.
     */
    @ElementCollection
    @CollectionTable(name = "forum_members", joinColumns = @JoinColumn(name = "forum_id"))
    @Column(name = "member_id")
    private List<Long> memberIds;

    /**
     * The list of admin IDs associated with the forum.
     */
    @ElementCollection
    @CollectionTable(name = "forum_admins", joinColumns = @JoinColumn(name = "forum_id"))
    @Column(name = "admin_id")
    private List<Long> adminIds;

    /**
     * The creation date and time of the forum.
     * This field is automatically populated.
     */
    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;

    /**
     * Adds a member to the forum.
     *
     * @param memberId the ID of the member to add
     */
    public void addMember(Long memberId) {
        this.memberIds.add(memberId);
    }

    /**
     * Removes a member from the forum.
     *
     * @param memberId the ID of the member to remove
     */
    public void removeMember(Long memberId) {
        this.memberIds.remove(memberId);
    }

    /**
     * Adds an admin to the forum.
     *
     * @param adminId the ID of the admin to add
     */
    public void addAdmin(Long adminId) {
        this.adminIds.add(adminId);
    }

    /**
     * Removes an admin from the forum.
     *
     * @param adminId the ID of the admin to remove
     */
    public void removeAdmin(Long adminId) {
        this.adminIds.remove(adminId);
    }
}
