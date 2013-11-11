package database.model.ontology;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


//@Entity
public class OntologyClass{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIri() {
		return iri;
	}
	public int getLevel() {
		return level;
	}
	public int getOrder() {
		return order;
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
	public void setOrder(int order) {
		this.order = order;
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
	private String discription;

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	private String iri;
	private int level;
	private int order;
	private Long branchId;
	
}
