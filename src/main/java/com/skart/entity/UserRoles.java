package com.skart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
public class UserRoles {

	@Id@GeneratedValue
	@Column(name="id")
	private Long id;
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "roles_role_id")
	private Long roleId;

	public UserRoles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRoles(Long userId, Long roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
