package main_service.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import main_service.playlist.entity.Playlist;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String username;
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "enabled_at")
    private LocalDateTime enabledAt;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "is_subscribed")
    private boolean subscribed;

    @Column(name = "subscribed_at")
    private LocalDateTime subscribedAt;

    @Column(name = "patron_name")
    private String patronName;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<Playlist> likes;

    @Column(name = "hifi_release_generations")
    private int hiFiReleaseGenerations;

    @Column(name = "lofi_release_generations")
    private int loFiReleaseGenerations;

    @Column(name = "hifi_playlist_generations")
    private int hiFiPlaylistGenerations;

    @Column(name = "lofi_playlist_generations")
    private int loFiPlaylistGenerations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}