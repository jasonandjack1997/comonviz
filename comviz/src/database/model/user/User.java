package database.model.user;

import unused.TrackableDatabaseEntity;

public class User extends TrackableDatabaseEntity{

	private Long roleId;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
