package com.project.Fabo.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    
    @OneToMany(mappedBy = "role")
    private Set<UsersRoles> usersRoles;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UsersRoles> getUsersRoles() {
		return usersRoles;
	}

	public void setUsersRoles(Set<UsersRoles> usersRoles) {
		this.usersRoles = usersRoles;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", usersRoles=" + usersRoles + "]";
	}

	public Role(Long id, String name, Set<UsersRoles> usersRoles) {
		super();
		this.id = id;
		this.name = name;
		this.usersRoles = usersRoles;
	}

	public Role() {
    	
    }
    
	
}