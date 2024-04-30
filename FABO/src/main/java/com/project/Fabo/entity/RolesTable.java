package com.project.Fabo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RolesTable {
	
	@Id
	private Long tableId;
	private String roleName;
	
	@Override
	public String toString() {
		return "RolesTable [tableId=" + tableId + ", roleName=" + roleName + "]";
	}
	public RolesTable(Long tableId, String roleName) {
		this.tableId = tableId;
		this.roleName = roleName;
	}
	public Long getTableId() {
		return tableId;
	}
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
