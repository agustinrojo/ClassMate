package com.classmate.comment_service.entity;

import com.classmate.comment_service.dto.user.userReputation.ReputationAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity class representing a comment in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "comments")
public class Comment {

    /**
     * The unique identifier of the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The identifier of the post to which the comment belongs.
     */
    @Column(nullable = false)
    private Long postId;


    @Column(nullable = false)
    private Long forumId;

    /**
     * The identifier of the author who created the comment.
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User author;

    /**
     * The body content of the comment.
     */
    @Column(nullable = false, length = 2000)
    private String body;

    @Column
    private int lastMilestone = 0;  // Default to 0, no milestones reached yet

    /**
     * The creation date and time of the comment.
     * This field is automatically populated.
     */
    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;

    @Column
    private Boolean hasBeenEdited = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Attachment> attachments;

    @ElementCollection
    private List<Long> upvotesByUserId = new ArrayList<>();

    @ElementCollection
    private List<Long> downvotesByUserId = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delete_request_id")
    private List<DeleteRequest> deleteRequests = new ArrayList<>();

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachments(List<Long> attachmentIds) {
        this.attachments.removeIf(attachment -> attachmentIds.contains(attachment.getId()));
    }

    public void addUpvote(Long userId) {
        if (!upvotesByUserId.contains(userId)) {
            upvotesByUserId.add(userId);
            downvotesByUserId.remove(userId);
        }
    }

    public void addDownvote(Long userId) {
        if (!downvotesByUserId.contains(userId)) {
            downvotesByUserId.add(userId);
            upvotesByUserId.remove(userId);
        }
    }

    public ReputationAction removeVote(Long userId) {
        if(upvotesByUserId.contains(userId)) {
            upvotesByUserId.remove(userId);
            return ReputationAction.REMOVELIKE;
        } else {
            downvotesByUserId.remove(userId);
            return ReputationAction.REMOVEDISLIKE;
        }
    }

    public int getValoration() {
        return (this.upvotesByUserId.size() - this.downvotesByUserId.size());
    }

    public void addDeleteRequest(DeleteRequest deleteRequest){
        deleteRequests.add(deleteRequest);
    }
}
