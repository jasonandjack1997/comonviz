package database.model;

public class MetaData {

	private Long id;
	private String databaseVersion;
	public Long getId() {
		return id;
	}
	public String getDatabaseVersion() {
		return databaseVersion;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setDatabaseVersion(String databaseVersion) {
		this.databaseVersion = databaseVersion;
	}
}
