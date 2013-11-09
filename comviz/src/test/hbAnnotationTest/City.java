package test.hbAnnotationTest;


import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name="CITY")
public class City {

	private long id;
	
	private Date created;

	@PrePersist
	protected void onCreate(){
		this.created = new Date();
	}
	

	public void setCreated(Date created) {
		this.created = created;
	}

	
	
	@Column(name="creation_time", nullable=false)
	protected Date getCreated(){
		//this.created = new Date();
		return this.created;
	}
	
	private String name;

	public City(String name) {
		super();
		this.name = name;
	}

	public City() {}

	@Id
	@GeneratedValue
	@Column(name="CITY_ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="CITY_NAME", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}