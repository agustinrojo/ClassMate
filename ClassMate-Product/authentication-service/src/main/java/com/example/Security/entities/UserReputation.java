package com.example.Security.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class UserReputation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long likesAmmount = 0L;
    private Long dislikesAmmount = 0L;

    public void addLike() {
        this.likesAmmount++;
    }

    public void addDislike() {
        this.dislikesAmmount++;
    }

    public void removeLike() {
        this.likesAmmount--;
    }

    public void removeDislike() {
        this.dislikesAmmount--;
    }
}
