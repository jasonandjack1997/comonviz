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
@PrimaryKeyJoinColumn(name = "ID")
public class OntologyClass extends Trackable {
	private Long branchId;
	@Column(columnDefinition = "TEXT")
	private String discription;
	private String iri;
	private int level;
	private String name;
	private int siblingRank;

	public Long getBranchId() {
		return branchId;
	}

	public String getDiscription() {
		return discription;
	}

	public String getIri() {
		return iri;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public int getSiblingRank() {
		return siblingRank;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSiblingRank(int order) {
		this.siblingRank = order;
	}
	public String toString(){
		return super.toString() + ", iri: " + iri;
	}

}
