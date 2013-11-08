package database;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class Trackable {
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreationDateTime() {
		return creationDateTime;
	}

	public Timestamp getModificationDateTime() {
		return modificationDateTime;
	}

	public Long getCreationUserId() {
		return creationUserId;
	}

	public Long getModificationUserId() {
		return modificationUserId;
	}

	public Long getDatabaseVersionId() {
		return databaseVersionId;
	}
	@PrePersist
	protected void onCreate(){
		this.creationDateTime = new Timestamp(new Date().getTime());
	}

	@PreUpdate 
	protected void onUpdate(){
		this.modificationDateTime = new Timestamp(new Date().getTime());
	}
	
	public void setCreationDateTime(Timestamp creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	public void setModificationDateTime(Timestamp modificationDateTime) {
		this.modificationDateTime = modificationDateTime;
	}

	public void setCreationUserId(Long creationUserId) {
		this.creationUserId = creationUserId;
	}

	public void setModificationUserId(Long modificationUserId) {
		this.modificationUserId = modificationUserId;
	}

	public void setDatabaseVersionId(Long databaseVersionId) {
		this.databaseVersionId = databaseVersionId;
	}

	private Timestamp creationDateTime;
	private Timestamp modificationDateTime;

	private Long creationUserId;
	private Long modificationUserId;

	private Long databaseVersionId;
}
