package database.model.ontology;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class OntologyAxiom{
	private String discription;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String iri;

	private String name;

	public String getDiscription() {
		return discription;
	}

	public Long getId() {
		return id;
	}

	public String getIri() {
		return iri;
	}
	public String getName() {
		return name;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
