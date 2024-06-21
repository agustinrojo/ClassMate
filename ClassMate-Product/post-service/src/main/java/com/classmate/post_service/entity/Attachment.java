package com.classmate.post_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attachments")
public class Attachment {
    @Id
    private Long id;

    @Column
    private String originalFilename;

    @Column
    private String contentType;

    @Column
    private Long size;
}
