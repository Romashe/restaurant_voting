package com.github.romashe.restvoting.repository;

import com.github.romashe.restvoting.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Tag(name = "User Controller")
@CacheConfig(cacheNames = "users")
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    @Cacheable(value = "users", key = "#email")
    Optional<User> findByEmailIgnoreCase(String email);
}