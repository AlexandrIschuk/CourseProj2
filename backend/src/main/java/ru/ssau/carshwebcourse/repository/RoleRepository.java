package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.carshwebcourse.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
