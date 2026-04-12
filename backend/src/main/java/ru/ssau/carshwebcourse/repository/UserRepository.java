package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.carshwebcourse.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getUserByUserId(Long userId);

    Optional<User> findUserByEmail(String email);

}
