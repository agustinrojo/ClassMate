package com.example.Security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue//auto-incremented
    private Long id;
    private String firstName;
    private String lastName;
    private String legajo;
    private String carrera;
    private String email;
    private String password;
    private boolean locked = false;
    private boolean enabled = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @ElementCollection
    @CollectionTable(name = "forums_created", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "forum_id")
    private List<Long> forumsCreated;

    @ElementCollection
    @CollectionTable(name = "forums_subscribed", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "forum_id")
    private List<Long> forumsSubscribed;

    @ElementCollection
    @CollectionTable(name = "forums_admin", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "forum_id")
    private List<Long> forumsAdmin;

    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<JWTToken> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAlreadySubscribedToForum(Long forumId) {
        return this.forumsSubscribed.contains(forumId);
    }

    public void subscribeToForum(Long forumId) {
        if (isAlreadySubscribedToForum(forumId)) {
            throw new IllegalArgumentException(String.format("User %d is already subscribed to forum %d.", id, forumId));
        }
        forumsSubscribed.add(forumId);
    }

    public void removeSubscriptionFromForum(Long forumId) {
        if (!isAlreadySubscribedToForum(forumId)) {
            throw new IllegalArgumentException(String.format("User %d is not subscribed to forum %d.", id, forumId));
        }
        forumsSubscribed.remove(forumId);
    }

    public boolean isAlreadyAdminOfForum(Long forumId) {
        return this.forumsAdmin.contains(forumId);
    }

    public void addAdminToForum(Long forumId) {
        if (isAlreadyAdminOfForum(forumId)) {
            throw new IllegalArgumentException(String.format("User %d is already an admin of forum %d.", id, forumId));
        }
        forumsAdmin.add(forumId);
    }

    public void removeAdminFromForum(Long forumId) {
        if (!isAlreadyAdminOfForum(forumId)) {
            throw new IllegalArgumentException(String.format("User %d is not an admin of forum %d.", id, forumId));
        }
        forumsAdmin.remove(forumId);
    }

    public void addForumAsCreator(Long forumId) {
        if (this.forumsCreated.contains(forumId)) {
            throw new IllegalArgumentException(String.format("User %d is already the creator of forum %d.", id, forumId));
        }
        this.forumsCreated.add(forumId);
        this.addAdminToForum(forumId);
        this.subscribeToForum(forumId);
    }

    public void removeForumSubscription(Long forumId){
        this.forumsSubscribed.removeIf(id -> id.equals(forumId));
    }

    public void removeForumCreated(Long forumId){
        this.forumsCreated.removeIf(id -> id.equals(forumId));
    }
}
