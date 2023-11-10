package com.daim.blog.repository;

import com.daim.blog.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);

    @Query("SELECT id FROM UserEntity WHERE username = :username")
    String findUserIdByUsername(@Param("username") String username);
}
