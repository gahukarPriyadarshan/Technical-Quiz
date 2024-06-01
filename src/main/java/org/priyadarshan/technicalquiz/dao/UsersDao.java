package org.priyadarshan.technicalquiz.dao;

import org.priyadarshan.technicalquiz.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsersDao extends JpaRepository<Users, String> {

    Optional<Users> findByEmail(String email);
}