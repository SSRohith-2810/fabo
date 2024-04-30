package com.project.Fabo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "users_roles")
public class UsersRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_roles_id")
    private Long userRolesId;
    
	@ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
	

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	 @Version
	 private Long version;


    @Override
	public String toString() {
		return "UsersRoles [userRolesId=" + userRolesId + ", role=" + role + ", user=" + user + "]";
	}

	public UsersRoles(Long userRolesId, Role role, User user) {
		this.userRolesId = userRolesId;
		this.role = role;
		this.user = user;
	}

	public UsersRoles() {
		// TODO Auto-generated constructor stub
	}

    public Long getUserRolesId() {
		return userRolesId;
	}

	public void setUserRolesId(Long userRolesId) {
		this.userRolesId = userRolesId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}


    // Getters, setters, other fields...
}
