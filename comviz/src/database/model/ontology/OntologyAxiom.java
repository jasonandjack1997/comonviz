package database.model.ontology;

import java.sql.Timestamp;

import database.model.Trackable;
import database.model.TrackableDatabaseEntity;

public class OntologyAxiom extends TrackableDatabaseEntity{
	private String iri;
	public String getIri() {
		return iri;
	}
	public void setIri(String iri) {
		this.iri = iri;
	}
	
}
