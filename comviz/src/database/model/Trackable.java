package database.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import database.model.ontology.OntologyClass;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Trackable {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return "id: " + id + "," + "name: " + name ;
	}

	@Override
	public boolean equals(Object obj) {
		if(((Trackable)obj).getId() == this.getId()){
			return true;
		}
		return false;
	}

	private Timestamp creationDateTime;
	private Timestamp modificationDateTime;

	private Long creationUserId;
	private Long modificationUserId;

	private Long databaseVersionId;
}
