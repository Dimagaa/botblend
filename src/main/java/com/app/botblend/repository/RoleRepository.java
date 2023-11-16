package com.app.botblend.repository;

import com.app.botblend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(Role.RoleName roleName);
}
