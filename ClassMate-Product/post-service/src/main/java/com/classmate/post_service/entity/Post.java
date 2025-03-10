package com.classmate.post_service.entity;

import com.classmate.post_service.dto.user.userReputation.ReputationAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity class representing a Post in the forum.
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    /**
     * Unique identifier for the Post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Identifier of the forum to which this post belongs.
     */
    @Column(nullable = false)
    private Long forumId;

    /**
     * Identifier of the author who created this post.
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User author;

    /**
     * Title of the post.
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * Body content of the post.
     */
    @Column(nullable = false, length = 5000)
    private String body;

    @Column
    private int lastMilestone = 0;  // Default to 0, no milestones reached yet


    /**
     * Timestamp indicating when the post was created.
     * This field is automatically populated.
     */
    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;

    @Column
    private Boolean hasBeenEdited = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Attachment> attachments;

    @ElementCollection
    private List<Long> upvotesByUserId = new ArrayList<>();

    @ElementCollection
    private List<Long> downvotesByUserId = new ArrayList<>();

    private Long commentCount = 0L;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delete_request_id")
    private List<DeleteRequest> deleteRequests = new ArrayList<>();


    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachments(List<Long> attachmentIds) {
        Iterator<Attachment> iterator = this.attachments.iterator();
        while (iterator.hasNext()) {
            Attachment attachment = iterator.next();
            if (attachmentIds.contains(attachment.getId())) {
                iterator.remove();
            }
        }
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

    public int getValoration(){
        return this.upvotesByUserId.size() - this.downvotesByUserId.size();
    }

    public void addDeleteRequest(DeleteRequest deleteRequest){
        deleteRequests.add(deleteRequest);
    }

    public void clearDeleteRequests(){
        deleteRequests.clear();
    }

}
