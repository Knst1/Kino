package ru.rsoi.OAuth2AuthorizationServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsoi.OAuth2AuthorizationServer.domain.ApplicationUser;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface ApplicationUserDao extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findFirstByUsername(@Nonnull String username);

}
