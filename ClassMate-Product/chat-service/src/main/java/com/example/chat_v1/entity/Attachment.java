package com.example.chat_v1.entity;

import jakarta.persistence.*;
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
