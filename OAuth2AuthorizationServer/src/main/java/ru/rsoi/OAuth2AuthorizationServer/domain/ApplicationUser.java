package ru.rsoi.OAuth2AuthorizationServer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rsoi.OAuth2AuthorizationServer.role.ApplicationUserRole;

import javax.persistence.*;
import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "app_user")
public class ApplicationUser implements UserDetails {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "isAccountNonExpired", nullable = false)
    private boolean isAccountNonExpired;
    @Column(name = "isAccountNonLocked", nullable = false)
    private boolean isAccountNonLocked;
    @Column(name = "isCredentialsNonExpired", nullable = false)
    private boolean isCredentialsNonExpired;
    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ApplicationUserRole.USER.getGrantedAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
