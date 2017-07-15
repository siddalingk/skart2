package com.skart.repository;

import org.springframework.data.repository.CrudRepository;

import com.skart.entity.Role;

public interface UserRolesRepository extends CrudRepository<Role, Long> {

	// User findRoleByUser(String name);
}
