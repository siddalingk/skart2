package com.skart.repository;

import org.springframework.data.repository.CrudRepository;

import com.skart.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String name);

}
