package com.hyl.userapi.dao;

import com.hyl.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserDao extends JpaRepository<User, Long> {
}
