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
        // deberia devolver una lista de roles
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

    public boolean isAlreadySubscribedToForum(Long forumId){
        return this.forumsSubscribed.stream()
                .anyMatch(f -> f.equals(forumId));
    }

    public void subscribeToForum(Long forumId){
        forumsSubscribed.add(forumId);
    }

    public boolean isAlreadyAdminOfForum(Long forumId) {
        return this.forumsAdmin.stream()
                .anyMatch(f -> f.equals(forumId));
    }

    public void addAdminToForum(Long forumId) {
        forumsAdmin.add(forumId);
    }

    public void removeAdminFromForum(Long forumId) {
        forumsAdmin.remove(forumId);
    }
}
