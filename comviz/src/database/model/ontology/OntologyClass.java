package database.model.ontology;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;

import database.model.Trackable;


@Entity
@PrimaryKeyJoinColumn(name="ID")
public class OntologyClass  extends Trackable{
	public String getIri() {
		return iri;
	}
	public int getLevel() {
		return level;
	}
	public int getSiblingRank() {
		return siblingRank;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setIri(String iri) {
		this.iri = iri;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setSiblingRank(int order) {
		this.siblingRank = order;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="TEXT")
	private String discription;

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	private String iri;
	private int level;
	private int siblingRank;
	private Long branchId;
	
}
