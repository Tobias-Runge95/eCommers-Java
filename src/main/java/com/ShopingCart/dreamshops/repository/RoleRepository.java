package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}