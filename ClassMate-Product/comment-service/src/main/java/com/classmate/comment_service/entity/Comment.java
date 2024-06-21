package com.classmate.comment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    /**
     * The identifier of the author who created the comment.
     */
    @Column(nullable = false)
    private Long authorId;

    /**
     * The body content of the comment.
     */
    @Column(nullable = false, length = 2000)
    private String body;

    /**
     * The creation date and time of the comment.
     * This field is automatically populated.
     */
    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Attachment> attachments;


    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachments(List<Long> attachmentIds) {
        this.attachments = this.attachments.stream()
                .filter(attachment -> !attachmentIds.contains(attachment.getId()))
                .collect(Collectors.toList());
    }
}
